package org.cwhd.measure

import grails.transaction.Transactional
import org.apache.commons.logging.LogFactory

@Transactional
class GithubDataService {

    private static final logger = LogFactory.getLog(this)
    def grailsApplication
    def httpRequestService

    def getAll(fromDate) {

    }

    def getProjectData(projectKey, fromDate, start, limit) {

    }

    def getCommits(project, repo, start, limit, fromDate) {

    }

    def getPullRequests(project, repo, start, limit, fromDate) {

    }

    def makeGitHubRequest(path, query) {
        def credentials = grailsApplication.config.github.credentials
        def url = grailsApplication.config.github.url //this is defined in application.properties
        try {
            return httpRequestService.callRestfulUrl(url, path, query, false, null, credentials)
            logger.debug("GOT DATA BACK FROM GITHUB")
        } catch (Exception ex) {
            //TODO handle this!!!
            logger.error("FAIL: $ex.message")
        }
    }



}