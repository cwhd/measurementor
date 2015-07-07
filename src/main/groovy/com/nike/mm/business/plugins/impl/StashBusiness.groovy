package com.nike.mm.business.plugins.impl

import com.google.common.collect.Lists
import com.nike.mm.business.plugins.IStashBusiness
import com.nike.mm.dto.HttpRequestDto
import com.nike.mm.dto.JobRunResponseDto
import com.nike.mm.entity.internal.JobHistory
import com.nike.mm.entity.plugins.Stash
import com.nike.mm.repository.es.plugins.IStashEsRepository
import com.nike.mm.repository.ws.IStashWsRepository
import com.nike.mm.service.IUtilitiesService
import groovy.util.logging.Slf4j
import groovyx.net.http.HTTPBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@Service
class StashBusiness extends AbstractBusiness implements IStashBusiness {

    static final String STASH_PLUGIN_NAME = "Stash"

    static final String MISSING_URL = "Missing url"

    static final String INVALID_URL = "Invalid url"

    @Autowired
    IStashWsRepository stashWsRepository

    @Autowired
    IStashEsRepository stashEsRepository

    @Autowired
    IUtilitiesService utilitiesService

    @Override
    String type() {
        return STASH_PLUGIN_NAME
    }

    @Override
    String validateConfig(final Object config) {
        final List<String> errorMessages = Lists.newArrayList()
        if (config.url) {
            try {
                new HTTPBuilder(config.url).get(path: '') { response ->
                    response.statusLine.statusCode == 200
                }
            }
            catch (e) {
                errorMessages.add(INVALID_URL)
            }
        } else {
            errorMessages.add(MISSING_URL)
        }

        return buildValidationErrorString(errorMessages)
    }

    @Override
    JobRunResponseDto updateDataWithResponse(final Date lastRunDate, final Object configInfo) {
        final String path = "/rest/api/1.0/projects";
        final def proxyDto = this.getProxyDto(configInfo)
        final HttpRequestDto dto = new HttpRequestDto(url: configInfo.url, path: path,
                query: [start: 0, limit: 300], credentials: configInfo.credentials,
                proxyDto: proxyDto)
        for (final String projectKey : this.stashWsRepository.findAllProjects(dto)) {
            this.updateProject(projectKey, configInfo, lastRunDate)
        }
        //todo deal with errors and count
        return new JobRunResponseDto(type: type(), status: JobHistory.Status.success, recordsCount: 0)
    }

    void updateProject(final String projectKey, final Object configInfo, final Date fromDate) {

        final def path = "/rest/api/1.0/projects/$projectKey/repos"
        final HttpRequestDto dto = [url             : configInfo.url, path: path, query: [start: 0, limit: 300], credentials:
                configInfo
                        .credentials, proxyDto: this.getProxyDto(configInfo)] as HttpRequestDto
        for (final Expando expando : this.stashWsRepository.findAllReposForProject(projectKey, dto)) {
            this.updateCommitDataForRepo(expando, configInfo, fromDate)
            this.updatePullDataForRepo(expando, configInfo, fromDate)
        }
    }

    void updateCommitDataForRepo(final Expando expando, final Object configInfo, final Date fromDate) {
        final def path = "/rest/api/1.0/projects/$expando.projectKey/repos/$expando.repo/commits"
        final HttpRequestDto dto = new HttpRequestDto(url: configInfo.url, path: path, query: [start: 0, limit: 300],
                credentials: configInfo.credentials, proxyDto: this.getProxyDto(configInfo))
        this.updateCommitDataForRepoRecusive(expando, dto, fromDate)
    }

    void updateCommitDataForRepoRecusive(final Expando expando, final HttpRequestDto dto, final Date fromDate) {
        final def json = this.stashWsRepository.findAllCommits(dto)
        def hitFromDate = false
        if (json) {
            for (def i : json.values) {
                final Date updatedDate = new Date(i.authorTimestamp)
                if (updatedDate >= fromDate) {
                    def locDelta = this.getChangedLinesOfCode(expando, dto, i.id)
                    def stashData = this.stashEsRepository.findOne(i.id)
                    if (stashData) {
                        stashData.linesRemoved = locDelta.removedLOC
                        stashData.linesAdded = locDelta.addedLOC
                        stashData.repo = this.utilitiesService.makeNonTokenFriendly(expando.repo)
                        stashData.stashProject = this.utilitiesService.makeNonTokenFriendly(expando.project)
                        stashData.commitCount = 1
                    } else {
                        //if we have a commit already i guess it would never change, no need to update it
                        stashData = new Stash(
                                id          : i.id,
                                created     : new Date(i.authorTimestamp), //comes back as epoch time, which sucks
                                author      : this.utilitiesService.cleanEmail(i.author.emailAddress),
                                stashProject: this.utilitiesService.makeNonTokenFriendly(expando.projectKey),
                                repo        : this.utilitiesService.makeNonTokenFriendly(expando.repo),
                                scmAction   : "commit",
                                dataType    : "SCM",
                                linesAdded  : locDelta.addedLOC,
                                linesRemoved: locDelta.removedLOC,
                                commitCount : 1)
                    }
                    this.stashEsRepository.save(stashData)
                } else {
                    hitFromDate = true
                }
            }
            if (!json.isLastPage && !hitFromDate) {
                dto.query.start += dto.query.limit
                this.updateCommitDataForRepoRecusive(expando, dto, fromDate)
            }
        }
    }

    void updatePullDataForRepo(final Expando expando, final Object configInfo, final fromDate) {
        final def path = "/rest/api/1.0/projects/$expando.projectKey/repos/$expando.repo/pull-requests"
        final def proxyDto = this.getProxyDto(configInfo)
        HttpRequestDto dto = new HttpRequestDto(url: configInfo.url, path: path, query: [start: 0, limit: 300, state:
                "all"],
                credentials: configInfo.credentials, proxyDto: proxyDto)
        this.updatePullDataForRepoRecursively(expando, dto, fromDate)
    }

    void updatePullDataForRepoRecursively(final Expando expando, final HttpRequestDto dto, final fromDate) {
        final def json = this.stashWsRepository.findAllPullRequests(dto)
        def hitFromDate = false
        if (json) {
            for (def i : json.values) {
                log.debug("UPDATE DATE: " + i.updatedDate)
                final Date updatedDate = new Date(i.updatedDate)
                if (updatedDate >= fromDate) {
                    this.stashEsRepository.save(createStashDataObject(expando, dto, i))
                } else {
                    hitFromDate = true
                }
            }
            if (!json.isLastPage && !hitFromDate) {
                dto.query.start += dto.query.limit
                this.updatePullDataForRepoRecursively(expando, dto, fromDate)
            }
        }
    }

    private List getListOfReviewers(def i) {
        final def reviewers = []
        i.reviewers.each { def r ->
            //TODO would be great to get WHAT they did here...
            reviewers.add(this.utilitiesService.cleanEmail(r.user.emailAddress))
        }
        return reviewers
    }

    private Stash createStashDataObject(final Expando expando, final HttpRequestDto dto, final def i) {
        final Date createdDate = new Date(i.createdDate)
        log.debug("Key: $createdDate.time-$i.author.user.id")
        final def commitCount = this.getCommitCount(dto, i.id)
        log.debug("CommitCount: $commitCount")
        Stash stashData = this.stashEsRepository.findOne("$createdDate.time-$i.author.user.id")
        if (stashData) {
            stashData.created = createdDate
            stashData.updated = new Date(i.updatedDate)
            stashData.author = this.utilitiesService.cleanEmail(i.author.user.emailAddress)
            stashData.reviewers = this.getListOfReviewers(i)
            stashData.stashProject = this.utilitiesService.makeNonTokenFriendly(expando.project)
            stashData.repo = this.utilitiesService.makeNonTokenFriendly(expando.repo)
            stashData.commentCount = Integer.parseInt(i.attributes.commentCount[0])
            stashData.scmAction = "pull-request"
            stashData.dataType = "SCM"
            stashData.state = i.state
            stashData.timeOpen = this.utilitiesService.getDifferenceBetweenDatesInHours(i.createdDate, i.updatedDate)
//                            stashData.commitCount = commitCount
        } else {
            stashData = new Stash(
                    id          : "$createdDate.time-$i.author.user.id",
                    created     : createdDate,
                    updated     : new Date(i.updatedDate),
                    author      : this.utilitiesService.cleanEmail(i.author.user.emailAddress),
                    reviewers   : this.getListOfReviewers(i),
                    stashProject: this.utilitiesService.makeNonTokenFriendly(expando.project),
                    repo        : this.utilitiesService.makeNonTokenFriendly(expando.repo),
                    scmAction   : "pull-request",
                    commentCount: Integer.parseInt(i.attributes.commentCount[0]),
                    dataType    : "SCM",
                    state       : i.state,
                    timeOpen    : this.utilitiesService.getDifferenceBetweenDatesInHours(i.createdDate, i.updatedDate)
//                                    commitCount : commitCount
            )
        }
    }

    /**
     * this will return the number of commits for this PR.  the way the stash API works you have to make another
     * call to get it
     * @param prNumber the ID from the request to the PR
     * @return the count of commits on this PR
     */
    def getCommitCount(final HttpRequestDto dto, prNumber) {
//        final HttpRequestDto countdto = new HttpRequestDto(url: dto.url, path: "$dto.path/$prNumber/commits",
//                query: [state: "all", start: 0, limit: 300], credentials: dto.credentials, proxyDto: dto.proxyDto)
        final def json = this.stashWsRepository.findCommitCount(dto)
        if (json) {
            return json.size
        } else {
            return 0
        }
    }

    def getChangedLinesOfCode(final Expando expando, final HttpRequestDto dto, final String sha) {
        def path = "/rest/api/1.0/projects/$expando.projectKey/repos/$expando.repo/commits/$sha/diff"
        HttpRequestDto shadto = [url  : dto.url, path: path, query: [start: 0, limit: 300], credentials: dto
                .credentials, proxyDto: dto.proxyDto] as HttpRequestDto
        def json = this.stashWsRepository.findCommitDataFromSha(shadto)
        def addedLOC = 0
        def removedLOC = 0
        if (json?.diffs) {
            if (json.diffs.hunks) {
                for (def e in json.diffs.hunks) {
//                    if ("segments" == e.key ) {
                        for (def s in e.segments) {
                            String e1 = ""
//                        for (def s in e.segments) {
                            if ("ADDED" == s.type) {
                                addedLOC += s.lines.size()
                            } else if ("REMOVED" == s.type) {
                                removedLOC += s.lines.size()
                            }
//                        }
                        }
//                    }
                }
            }
        }
        return [addedLOC: addedLOC, removedLOC: removedLOC]
    }

//    private def extractAttribute(final def input, final String key) {
//        if (input) {
//            if (isCollectionOrArray(input)) {
//                String isCollection = "true"
//            } else {
//                if (Map.isAssignableFrom(input.getClass())) {
//                    if (input.key) {
//                        String test = ""
//                    }
//                }
//            }
//        }
//    }
//
//    private boolean isCollectionOrArray(Object value) {
//        [Collection, Object[]].any { it.isAssignableFrom(value.getClass()) }
//    }
}
