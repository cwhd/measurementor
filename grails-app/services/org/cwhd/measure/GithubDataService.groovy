package org.cwhd.measure

import grails.transaction.Transactional
import org.apache.commons.logging.LogFactory

@Transactional
class GithubDataService {

    private static final logger = LogFactory.getLog(this)
    def grailsApplication
    def httpRequestService

    def getAll(fromDate) {
        logger.info("----")
        logger.info("GETTING GITHUB DATA")
        logger.info("----")
        //TODO get all the repos we care about
        //https://api.github.com/users/cwhd/repos
        //for each record that comes back, get json.url
        def start = 0
        def limit = 300
        def path = "/users/cwhd/repos" ///get all the repos for a user...
        def json = makeGitHubRequest(path, [start: start, limit: limit])
        //TODO need to figure out paging
//        if(!json.isLastPage) {
//            logger.info("PAGING PROJECTS...")
//            getAll(path, [start: start+limit, limit: limit])
//        }
        def projectList = []
        for (def i : json) {
            projectList.add(i.name) //add to the list to get off this thread
            logger.info("GITHUB REPO: $i.name")
        }
        for(def repo in projectList) {
            //getProjectData(project, fromDate, start, limit)
            logger.info("GETTING DATA FOR REPO $repo")
            getCommits(repo, start, limit, fromDate)
        }
    }

    //TODO i don't think i need this for GitHub...
    def getProjectData(projectKey, fromDate, start, limit) {
        //TODO for each repo get all pull requests and commits
        //https://api.github.com/repos/cwhd/measurementor
    }

    def getCommits(repo, start, limit, fromDate) {
        // https://api.github.com/repos/cwhd/measurementor/commits
        def path = "/repos/cwhd/$repo/commits"
        logger.info("GETTING ALL COMMITS FOR THE REPO...")
        def json = makeGitHubRequest("/repos/cwhd/$repo/commits", [start: start, limit: limit])
        for(def c : json) {
//            def updatedDate = UtilitiesService.cleanGithubDate(c.commit.committer.date)
//            if(updatedDate >= fromDate) {
            logger.info("GETTING ALL INFO FOR THIS COMMIT...$c.sha")
            //TODO get each commit
            def commit = makeGitHubRequest("/repos/cwhd/$repo/commits/$c.sha", [start: start, limit: limit])
            def githubData = GithubData.findBySha(c.sha)
            if(githubData) {
                githubData.created = commit.commit.committer.date
                githubData.linesAdded = commit.stats.additions
                githubData.linesRemoved = commit.stats.deletions
                githubData.author = commit.commit.committer.name
            } else {
                githubData = new GithubData(
                        sha: c.sha,
                        created: commit.commit.committer.date,
                        linesAdded: commit.stats.additions,
                        linesRemoved: commit.stats.deletions,
                        author: commit.commit.committer.name
                )
            }
            githubData.save(flush: true, failOnError: true)
//            } else {
//                logger.info("HIT THE FROM DATE: $updatedDate")
//            }
        }
    }

    def getPullRequests(project, repo, start, limit, fromDate) {
        //TODO get all the pull-requests for a repo from the fromDate
        //https://api.github.com/repos/cwhd/measurementor/pulls
    }

    def makeGitHubRequest(path, query) {
        def credentials = grailsApplication.config.github.credentials
        def url = grailsApplication.config.github.url //this is defined in application.properties
        try {
            return httpRequestService.callRestfulUrl(url, path, query, false, null, credentials)
            logger.info("GOT DATA BACK FROM GITHUB")
        } catch (Exception ex) {
            //TODO handle this!!!
            logger.info("FAIL: $ex.message")
        }
    }
}