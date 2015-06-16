package org.cwhd.measure

import grails.transaction.Transactional
import org.apache.commons.logging.LogFactory

import java.util.concurrent.TimeUnit

/**
 * this gets data out of stash and scrubs it a bit
 */
//Stash API: https://developer.atlassian.com/static/rest/stash/3.3.1/stash-rest.html#idp1779536
@Transactional
class StashDataService {

    private static final logger = LogFactory.getLog(this)
    def grailsApplication
    def httpRequestService
    def couchConnectorService

    /***
     * 1) get all the projects in Stash
     */
    def getAll(fromDate) {
        logger.info("----")
        logger.info("GETTING STASH DATA")
        logger.info("----")
        def start = 0
        def limit = 300
        def path = "/rest/api/1.0/projects" ///get all the projects in stash...
        def json = makeStashRequest(path, [start: start, limit: limit])
        if(!json.isLastPage) {
            logger.debug("PAGING PROJECTS...")
            getAll(path, [start: start+limit, limit: limit])
        }
        def projectList = []
        for (def i : json.values) {
            projectList.add(i.key) //add to the list to get off this thread
            logger.debug("STASH PROJECT: $i.key")
        }
        for(def project in projectList) {
            getProjectData(project, fromDate, start, limit)
        }
    }

    /***
     * 2) get all the repos in a project
     * @param projectKey
     */
    def getProjectData(projectKey, fromDate, start, limit) {
        def path = "/rest/api/1.0/projects/$projectKey/repos" ///get all the projects in stash...
        def json = makeStashRequest(path, [start: start, limit: limit])
        if(!json.isLastPage) {
            logger.debug("PAGING REPOS...")
            getProjectData(projectKey, fromDate, [start: start+limit, limit: limit])
        }

        def repoList = []
        for (def i : json.values) {
            repoList.add(new Expando(projectKey:projectKey, repo: i.slug, start:0, limit:300))
        }
        for(def repo in repoList){
            logger.info("GETTING DATA FOR $repo.projectKey : $repo.repo : since: $fromDate")
            getPullRequests(repo.projectKey, repo.repo, repo.start, repo.limit, fromDate) //for each one of these, get pull requests & commits
            getCommits(repo.projectKey, repo.repo, repo.start, repo.limit, fromDate)
        }
        //TODO i should probably save some data on the repo...i can aggregate those into project stats
    }

    /**
     * get all the commits for a repo
     * @param project
     * @param repo
     */
    def getCommits(project, repo, start, limit, fromDate) {
        def path = "/rest/api/1.0/projects/$project/repos/$repo/commits"
        def json = makeStashRequest(path, [start: start, limit: limit])
        def hitFromDate = false
        logger.debug("GOT JSON")

        if (json) {
            logger.debug("JSON IS NOT FALSE")
            for (def i : json.values) {
                logger.debug("JSON VALUE...")
                def updatedDate = new Date(i.authorTimestamp)
                logger.debug("COMMITS: HERE ARE THE DATES updated: $updatedDate fromDate: $fromDate")
                if(updatedDate >= fromDate) {   //we only need to do stuff if this was modified on or after the fromDate
                    logger.debug("HIT THE WAY BACK DATE; author timestamp: $updatedDate fromDate: $fromDate")

                    def locDelta = getCLOC(project, repo, i.id)
                    def stashData = StashData.findByKey(i.id)
                    if (!stashData) { //if we have a commit already i guess it would never change, no need to update it
                        logger.debug("INSERT COMMIT")
                        stashData = new StashData(
                                key: i.id,
                                created: new Date(i.authorTimestamp), //comes back as epoch time, which sucks
                                author: UtilitiesService.cleanEmail(i.author.emailAddress),
                                stashProject: UtilitiesService.makeNonTokenFriendly(project),
                                repo: UtilitiesService.makeNonTokenFriendly(repo),
                                scmAction: "commit",
                                dataType: "SCM",
                                linesAdded: locDelta.addedLOC,
                                linesRemoved: locDelta.removedLOC,
                                commitCount: 1
                        )
                    } else {
                        logger.debug("UPDATE COMMIT")
                        stashData.linesRemoved = locDelta.removedLOC
                        stashData.linesAdded = locDelta.addedLOC
                        stashData.repo = UtilitiesService.makeNonTokenFriendly(repo)
                        stashData.stashProject = UtilitiesService.makeNonTokenFriendly(project)
                        stashData.commitCount = 1
                    }

                    if(grailsApplication.config.sendDataToCouch == "true") {
                        def couchReturn = couchConnectorService.saveToCouch(stashData)
                        logger.debug("RETURNED FROM COUCH: $couchReturn")
                        stashData.couchId = couchReturn
                    }

                    stashData.save(flush: true, failOnError: true)
                } else {
                    hitFromDate = true
                }
            }
            if(!json.isLastPage && !hitFromDate) { //if there are more than 100 records then recurse
                def newLimit = start + limit
                logger.debug("GETTING COMMITS: $start : and : $newLimit")
                getCommits(project, repo, start+limit, limit, fromDate)
            }
        }
    }

    //if i can pass a method into this method, then i can delegate the parsing.  that would cut
    //code way down and make this a lot more testable
    /***
     * 3) Get all the pull requests for the repo
     * @param project
     * @param repo
     * @param start
     * @param limit
     * @return
     */
    def getPullRequests(project, repo, start, limit, fromDate) {
        def path = "/rest/api/1.0/projects/$project/repos/$repo/pull-requests" ///pull-requests
        def json = makeStashRequest(path, [state: "all", start: start, limit: limit])
        def hitFromDate = false

        for (def i : json.values) {
            def updatedDate = new Date(i.updatedDate)
            logger.debug("PRS: HERE ARE THE DATES updated: $updatedDate fromDate: $fromDate")
            if(updatedDate >= fromDate) {
                logger.debug("DID NOT HIT THE WAY BACK DATE; updated: $updatedDate fromDate: $fromDate")

                def reviewers = []
                for (def r in i.reviewers) {
                    if (r.user.emailAddress) {
                        //TODO would be great to get WHAT they did here...
                        reviewers.add(UtilitiesService.cleanEmail(r.user.emailAddress))
                    }
                }

                int commentCount = 0
                if (i.attributes.commentCount?.size() > 0) {
                    commentCount = Integer.parseInt(i.attributes.commentCount[0])
                }

                def timeOpen = UtilitiesService.getDifferenceBetweenDatesInHours(i.createdDate, i.updatedDate)
                def commitCount = getCommitCount(path, i.id)

                logger.debug("HERE IS THE STASH KEY:")
                logger.debug("$i.createdDate-$i.author.user.id")

                def stashData = StashData.findByKey("$i.createdDate-$i.author.user.id")
                if (stashData) {
                    logger.debug("UPDATING...")
                    stashData.created = new Date(i.createdDate)
                    stashData.updated = new Date(i.updatedDate)
                    stashData.author = UtilitiesService.cleanEmail(i.author.user.emailAddress)
                    stashData.reviewers = reviewers
                    stashData.stashProject = UtilitiesService.makeNonTokenFriendly(project)
                    stashData.repo = UtilitiesService.makeNonTokenFriendly(repo)
                    stashData.commentCount = commentCount
                    stashData.scmAction = "pull-request"
                    stashData.dataType = "SCM"
                    stashData.state = i.state
                    stashData.timeOpen = timeOpen
                    stashData.commitCount = commitCount
                } else {
                    logger.debug("INSERTING...")
                    stashData = new StashData(
                            key: "$i.createdDate-$i.author.user.id",
                            created: new Date(i.createdDate),
                            updated: new Date(i.updatedDate),
                            author: UtilitiesService.cleanEmail(i.author.user.emailAddress),
                            reviewers: reviewers,
                            stashProject: UtilitiesService.makeNonTokenFriendly(project),
                            repo: UtilitiesService.makeNonTokenFriendly(repo),
                            scmAction: "pull-request",
                            commentCount: commentCount,
                            dataType: "SCM",
                            state: i.state,
                            timeOpen: timeOpen,
                            commitCount: commitCount
                    )
                }

                if(grailsApplication.config.sendDataToCouch == "true") {
                    def couchReturn = couchConnectorService.saveToCouch(stashData)
                    logger.debug("ID RETURNED FROM COUCH: $couchReturn")
                    stashData.couchId = couchReturn
                }

                stashData.save(flush: true, failOnError: true)
            } else {
                hitFromDate = true
            }
        }
        //TODO need to not call another page if i hit the waybackdate
        if(!json.isLastPage && !hitFromDate) { //if there are more than 100 records then recurse
            def newLimit = start + limit
            logger.debug("GETTING PULL REQUEST: $start : and : $newLimit")
            getPullRequests(project, repo, start+limit, limit, fromDate)
        }
    }

    /**
     * this will return the number of commits for this PR.  the way the stash API works you have to make another
     * call to get it
     * @param prNumber the ID from the request to the PR
     * @return the count of commits on this PR
     */
    def getCommitCount(currentPath, prNumber){
        try {
            def json = makeStashRequest("$currentPath/$prNumber/commits", null)
            if(json) {
                logger.debug("PR HAD " + json.size + " COMMITS!")
                return json.size
            } else {
                return 0
            }
        } catch (Exception e) {
            logger.error("FAIL GETTING COMMIT COUNT! :" + e.getStackTrace())
            return 0
        }
    }

    //TODO need richer pull request review data
    /**
     //number of changed lines
     here is a good example:
     http://stash.nikedev.com/rest/api/1.0/projects/SQA/repos/humulo/commits/d333c1e0e1ee050f68a46f15cf8c78456a2c01b0/diff
     **/
    def getCLOC(project, repo, sha) {
        try {
            def path = "/rest/api/1.0/projects/$project/repos/$repo/commits/$sha/diff"
            def json = makeStashRequest(path, null)
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
            logger.debug([addedLOC: addedLOC, removedLOC: removedLOC])
            return [addedLOC: addedLOC, removedLOC: removedLOC]
        } catch (Exception e) {
            logger.error("FAIL CLOC! " + e.getStackTrace())
            return [addedLOC: 0, removedLOC: 0]
        }
    }


    /***
     * all the http stuff in one place
     * @param path
     * @param query
     * @return
     */
    def makeStashRequest(path, query) {
        def credentials = grailsApplication.config.stash.credentials
        def url = grailsApplication.config.stash.url //this is defined in application.properties
        try {
            return httpRequestService.callRestfulUrl(url, path, query, false, null, credentials)
            logger.debug("GOT DATA BACK FROM STASH")
        } catch (Exception ex) {
            //TODO handle this!!!
            logger.error("FAIL: $ex.message")
        }
    }
}