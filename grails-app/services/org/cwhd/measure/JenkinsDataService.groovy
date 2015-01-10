package org.cwhd.measure

import grails.transaction.Transactional
import org.apache.commons.logging.LogFactory

@Transactional
class JenkinsDataService {
    private static final logger = LogFactory.getLog(this)
    def grailsApplication
    def httpRequestService

    /**
     * TODO Notes - this is a WIP
     * start with getting all the projects in Jenkins
     * once we have that list we can check each project
     * we can see how projects are linked together by looking at the downstreamProjects and upstreamProjects nodes
     */

    def getJobs() {
        logger.info("CALLING JENKINS")
        def url = grailsApplication.config.jenkins.url
        def path = "/api/json"
        def json = httpRequestService.callRestfulUrl(url, path, null, false)

        def jobs = []

        logger.info(json)

        for(def i : json.jobs) {
            logger.info(i)
            builds.add(i.name)
            logger.info("$i.name")
        }

        return jobs
    }

    def getData(startAt, maxResults, project, fromDate) {
        if(!startAt) {
            startAt = 0
        }
        if(!maxResults) {
            maxResults = 100
        }
        def fromQuery = ""
        if(fromDate) {
            fromQuery = " AND updatedDate>$fromDate"
        }

        def url = grailsApplication.config.asgard.url
        def path = "/rest/api/2/search"
        def jiraQuery = "project=$project$fromQuery"
        def query = [jql: jiraQuery, expand:"changelog",startAt: startAt, maxResults: maxResults, fields:"*all"]

        def json = httpRequestService.callRestfulUrl(url, path, query, true)
        def keepGoing = false
        if(json.total) {
            logger.info("----------------------------------")
            logger.info("$json.total records in $project")
            logger.info("----------------------------------")
        } else {
            logger.info("----------------------------------")
            logger.info("no results for $project!")
            logger.info("----------------------------------")
        }

    }
}
