package org.cwhd.measure

import grails.transaction.Transactional
import org.apache.commons.logging.LogFactory

/**
 * this gets data out of stash and scrubs it a bit
 */
//Stash API: https://developer.atlassian.com/static/rest/stash/3.3.1/stash-rest.html#idp1779536
@Transactional
class StashDataService {

    private static final logger = LogFactory.getLog(this)
    def grailsApplication
    def httpRequestService

    /***
     * 1) get all the projects in Stash
     */
    def getAll() {
        def path = "/rest/api/1.0/projects" ///get all the projects in stash...
        def json = makeStashRequest(path, null)
        def projectList = []
        for (def i : json.values) {
            projectList.add(i.key) //add to the list to get off this thread
        }
        //TODO for now i should just hard code the projects i care about
        projectList = ["SQA"] //BP
        for(def project in projectList) {
            getProjectData(project)
        }
    }

    /***
     * 2) get all the repos in a project
     * @param projectKey
     */
    def getProjectData(projectKey) {
        def path = "/rest/api/1.0/projects/$projectKey/repos" ///get all the projects in stash...
        def json = makeStashRequest(path, null)
        def repoList = []
        for (def i : json.values) {
            repoList.add(new Expando(projectKey:projectKey, repo: i.slug, start:0, limit:100))
        }
        for(def repo in repoList){
            logger.info("GETTING DATA FOR $repo.projectKey : $repo.repo")
            getPullRequests(repo.projectKey, repo.repo, repo.start, repo.limit) //for each one of these, get pull requests & commits
            getCommits(repo.projectKey, repo.repo, 0, 100)
        }
        //TODO i should probably save some data on the repo...i can aggregate those into project stats
    }

    /**
     * get all the commits for a repo
     * @param project
     * @param repo
     */
    def getCommits(project, repo, start, limit) {
        def path = "/rest/api/1.0/projects/$project/repos/$repo/commits"
        def json = makeStashRequest(path, null)
        if (json) {
            for (def i : json.values) {
                //if(!json.isLastPage) { //if there are more than 100 records then recurse
                //    getCommits(project, repo, start+limit, limit)
                //}
                def locDelta = getCLOC(project, repo, i.id)
                def stashData = StashData.findByKey(i.id)
                if(!stashData) { //if we have a commit already i guess it would never change, no need to update it
                    stashData = new StashData(
                            key: i.id,
                            created: new Date(i.authorTimestamp), //comes back as epoch time, which sucks
                            author: UtilitiesService.cleanEmail(i.author.emailAddress),
                            stashProject: UtilitiesService.makeNonTokenFriendly(project),
                            repo: UtilitiesService.makeNonTokenFriendly(repo),
                            scmAction: "commit",
                            dataType: "SCM",
                            linesAdded: locDelta.addedLOC,
                            linesRemoved: locDelta.removedLOC
                    )
                } else {
                    stashData.linesRemoved = locDelta.removedLOC
                    stashData.linesAdded = locDelta.addedLOC
                    stashData.repo = UtilitiesService.makeNonTokenFriendly(repo)
                    stashData.stashProject = UtilitiesService.makeNonTokenFriendly(project)
                }
                stashData.save(flush: true, failOnError: true)
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
    def getPullRequests(project, repo, start, limit) {
        def path = "/rest/api/1.0/projects/$project/repos/$repo/pull-requests" ///pull-requests
        def json = makeStashRequest(path, [state: "all", start: start, limit: limit])

        for (def i : json.values) {
            def reviewers = []
            if(!json.isLastPage) { //if there are more than 100 records then recurse
                getPullRequests(project, repo, start+limit, limit)
            }

            for(def r in i.reviewers) {
                if(r.user.emailAddress){
                    //TODO would be great to get WHAT they did here...
                    //reviewers.add(name: r.user.emailAddress.replace("@nike.com",""), approved: r.approved)
                    reviewers.add(UtilitiesService.cleanEmail(r.user.emailAddress))
                }
            }

            int commentCount = 0
            logger.info("comment count: $i.attributes.commentCount")
            if(i.attributes.commentCount?.size() > 0) {
                commentCount = Integer.parseInt(i.attributes.commentCount[0])
            }

            def stashData = StashData.findByKey("$i.createdDate-$i.author.user.id")
            if(stashData) {
                stashData.updated = new Date(i.updatedDate)
                stashData.reviewers = reviewers
                stashData.repo = UtilitiesService.makeNonTokenFriendly(repo)
                stashData.stashProject = UtilitiesService.makeNonTokenFriendly(project)
                stashData.commentCount = commentCount
            } else {
                stashData = new StashData(
                        key: "$i.createdDate-$i.author.user.id",
                        created: new Date(i.createdDate),
                        updated: new Date(i.updatedDate),
                        author: UtilitiesService.cleanEmail(i.author.user.emailAddress),
                        reviewers: reviewers,
                        stashProject: project,
                        repo: UtilitiesService.makeNonTokenFriendly(repo),
                        scmAction: "pull-request",
                        commentCount: commentCount,
                        dataType: "SCM"
                )
            }
            stashData.save(flush: true, failOnError: true)
        }
    }

    //TODO need richer pull request review data
    /**
     //TODO to get the CLOC it looks like i'll have to get to a commit/{sha}/diff and count the
     //number of changed lines
     here is a good example:
     http://stash.nikedev.com/rest/api/1.0/projects/SQA/repos/humulo/commits/d333c1e0e1ee050f68a46f15cf8c78456a2c01b0/diff
     **/
    def getCLOC(project, repo, sha) {
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
    }


    /***
     * all the http stuff in one place
     * @param path
     * @param query
     * @return
     */
    def makeStashRequest(path, query) {
        def url = grailsApplication.config.stash.url //this is defined in application.properties
        try {
            return httpRequestService.callRestfulUrl(url, path, query, false)
        } catch (Exception ex) {
            //TODO handle this!!!
            logger.info("FAIL: $ex.message")
        }
    }
}