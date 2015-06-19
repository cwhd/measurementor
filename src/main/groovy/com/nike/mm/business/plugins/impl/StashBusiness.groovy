package com.nike.mm.business.plugins.impl

import com.nike.mm.business.plugins.IStashBusiness
import com.nike.mm.dto.HttpRequestDto
import com.nike.mm.dto.ProxyDto
import com.nike.mm.entity.Stash
import com.nike.mm.repository.es.plugins.IStashEsRepository
import com.nike.mm.repository.ws.IStashWsRepository
import com.nike.mm.service.IUtilitiesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by rparr2 on 6/18/15.
 */
@Service
class StashBusiness implements IStashBusiness {

    @Autowired IStashWsRepository stashWsRepository

    @Autowired IStashEsRepository stashEsRepository

    @Autowired IUtilitiesService utilitiesService

    @Override
    String type() {
        return "Stash"
    }

    @Override
    boolean validateConfig(final Object config) {
        return true
    }

    @Override
    void updateData(final Object configInfo) {
        //TODO get last from date.
        Date fromDate = Date.parse( 'dd-MM-yyyy', "01-01-2001" )
        String path = "/rest/api/1.0/projects";
        HttpRequestDto dto = [url: configInfo.url, path: path, query:[start: 0, limit: 300], credentials: configInfo.credentials, proxyDto:[]as ProxyDto] as HttpRequestDto
        for ( String projectKey: this.stashWsRepository.findAllProjects(dto) ) {
            this.updateProject(projectKey, configInfo, fromDate)
        }
    }

    void updateProject(final String projectKey, final Object configInfo, final Date fromDate) {

        def path = "/rest/api/1.0/projects/$projectKey/repos"
        HttpRequestDto dto = [url: configInfo.url, path: path, query:[start: 0, limit: 300], credentials: configInfo.credentials, proxyDto:[]as ProxyDto] as HttpRequestDto
        for ( Expando expando: this.stashWsRepository.findAllReposForProject(projectKey, dto)) {
            this.updateCommitDataForRepo(expando, configInfo, fromDate)
            this.updatePullDataForRepo(expando, configInfo, fromDate)
        }
    }

    void updateCommitDataForRepo(final Expando expando, final Object configInfo, final Date fromDate) {
        def path = "/rest/api/1.0/projects/$expando.projectKey/repos/$expando.repo/commits"
        HttpRequestDto dto = [url: configInfo.url, path: path, query:[start: 0, limit: 300], credentials: config.credentials, proxyDto:[]as ProxyDto] as HttpRequestDto
        this.updateCommitDataForRepo(expando, dto, fromDate)
    }

    void updateCommitDataForRepo(final Expando expando, HttpRequestDto dto, final fromDate) {
        def json = this.stashWsRepository.findAllCommits(dto)
        def hitFromDate = false
        if (json) {
            for (def i : json.values) {
                final Date updatedDate = new Date(i.authorTimestamp)
                if (updatedDate >= fromDate) {
                    def locDelta = this.getChangedLinesOfCode(project, repo, i.id)
                    def stashData = this.stashEsRepository.findOne(i.id)
                    if (!stashData) { //if we have a commit already i guess it would never change, no need to update it
                        stashData = [
                                id:             i.id,
                                created:        new Date(i.authorTimestamp), //comes back as epoch time, which sucks
                                author:         this.utilitiesService.cleanEmail(i.author.emailAddress),
                                stashProject:   this.utilitiesService.makeNonTokenFriendly(project),
                                repo:           this.utilitiesService.makeNonTokenFriendly(repo),
                                scmAction:      "commit",
                                dataType:       "SCM",
                                linesAdded:     locDelta.addedLOC,
                                linesRemoved:   locDelta.removedLOC,
                                commitCount:    1
                        ] as Stash
                    } else {
                        stashData.linesRemoved  = locDelta.removedLOC
                        stashData.linesAdded    = locDelta.addedLOC
                        stashData.repo          = this.utilitiesService.makeNonTokenFriendly(repo)
                        stashData.stashProject  = this.utilitiesService.makeNonTokenFriendly(project)
                        stashData.commitCount   = 1
                    }
                    this.stashEsRepository.save(stashData)
                } else {
                    hitFromDate = true
                }
            }
            if (!json.isLastPage && !hitFromDate) {
                dto.query.start += dto.query.limit
                this.updateCommitDataForRepo(expando, dto, fromDate)
            }
        }
    }

    void updatePullDataForRepo(final Expando expando, final Object configInfo, final fromDate) {
        def path = "/rest/api/1.0/projects/$expando.projectKey/repos/$expando.repo/pull-requests"
        HttpRequestDto dto = [url: configInfo.url, path: path, query:[start: 0, limit: 300], credentials: config.credentials, proxyDto:[]as ProxyDto] as HttpRequestDto
        this.updatePullDataForRepo(expando, dto, fromDate)
    }

    void updatePullDataForRepo(final Expando expando, HttpRequestDto dto, final fromDate) {
        def json = this.stashWsRepository.findAllPullRequests(dto)
        def hitFromDate = false
        if (json) {
            for (def i : json.values) {
                def updatedDate = new Date(i.updatedDate)
                if(updatedDate >= fromDate) {
                    def reviewers = []
                    for (def r in i.reviewers) {
                        if (r.user.emailAddress) {
                            //TODO would be great to get WHAT they did here...
                            reviewers.add(this.utilitiesService.cleanEmail(r.user.emailAddress))
                        }

                        int commentCount = 0
                        if (i.attributes.commentCount?.size() > 0) {
                            commentCount = Integer.parseInt(i.attributes.commentCount[0])
                        }

                        def timeOpen = this.utilitiesService.getDifferenceBetweenDatesInHours(i.createdDate, i.updatedDate)
                        def commitCount = this.getCommitCount(dto, i.id)

                        Stash stashData = this.stashEsRepository.findOne("$i.createdDate-$i.author.user.id")
                        if (stashData) {
                            stashData.created           = new Date(i.createdDate)
                            stashData.updated           = new Date(i.updatedDate)
                            stashData.author            = this.utilitiesService.cleanEmail(i.author.user.emailAddress)
                            stashData.reviewers         = reviewers
                            stashData.stashProject      = this.utilitiesService.makeNonTokenFriendly(project)
                            stashData.repo              = this.utilitiesService.makeNonTokenFriendly(repo)
                            stashData.commentCount      = commentCount
                            stashData.scmAction         = "pull-request"
                            stashData.dataType          = "SCM"
                            stashData.state             = i.state
                            stashData.timeOpen          = timeOpen
                            stashData.commitCount       = commitCount
                        } else {
                            stashData = [
                                    id:             "$i.createdDate-$i.author.user.id",
                                    created:        new Date(i.createdDate),
                                    updated:        new Date(i.updatedDate),
                                    author:         this.utilitiesService.cleanEmail(i.author.user.emailAddress),
                                    reviewers:      reviewers,
                                    stashProject:   this.utilitiesService.makeNonTokenFriendly(project),
                                    repo:           this.utilitiesService.makeNonTokenFriendly(repo),
                                    scmAction:      "pull-request",
                                    commentCount:   commentCount,
                                    dataType:       "SCM",
                                    state:          i.state,
                                    timeOpen:       timeOpen,
                                    commitCount:    commitCount
                            ] as Stash
                        }
                        this.stashEsRepository.save(stashData)
                    }
                } else {
                    hitFromDate = true
                }
            }
            if (!json.isLastPage && !hitFromDate) {
                dto.query.start += dto.query.limit
                this.updatePullDataForRepo(expando, dto, fromDate)
            }
        }
    }

    /**
     * this will return the number of commits for this PR.  the way the stash API works you have to make another
     * call to get it
     * @param prNumber the ID from the request to the PR
     * @return the count of commits on this PR
     */
    def getCommitCount(HttpRequestDto dto, prNumber){
        try {
            HttpRequestDto countdto = [url: dto.url, path: "$dto.path/$prNumber/commits", query:[start: 0, limit: 300], credentials: dto.credentials, proxyDto:dto.proxyDto] as HttpRequestDto
            def json = this.stashWsRepository.findCommitCount(dto)
            if(json) {
                return json.size
            } else {
                return 0
            }
        } catch (Exception e) {
            return 0
        }
    }

    def getChangedLinesOfCode(final Expando expando, final Object configInfo, final String sha) {
        try {
            def path = "/rest/api/1.0/projects/$expando.projectKey/repos/$expando.repo/commits/$sha/diff"
            HttpRequestDto dto = [url: configInfo.url, path: path, query:[start: 0, limit: 300], credentials: configInfo.credentials, proxyDto:[]as ProxyDto] as HttpRequestDto
            def json = this.stashWsRepository.findCommitDataFromSha(dto)
            def addedLOC = 0
            def removedLOC = 0
            for(def d in json.diffs) {
                for(def h in d.hunks) {
                    for(def s in h.segments) {
                        if(s.type == "ADDED") {
                            addedLOC += s.lines.size()
                        } else if (s.type == "REMOVED") {
                            removedLOC += s.lines.size()
                        }
                    }
                }
            }
            return [addedLOC: addedLOC, removedLOC: removedLOC]
        } catch (Exception e) {
            return [addedLOC: 0, removedLOC: 0]
        }
    }
}
