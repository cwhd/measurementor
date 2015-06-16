package org.cwhd.measure

import grails.transaction.Transactional
import org.apache.commons.logging.LogFactory

@Transactional
class AsgardDataService {
    private static final logger = LogFactory.getLog(this)
    def grailsApplication
    def httpRequestService

    /**
     * for some reason pain-in-the-asgard is returning an HTML page first.  I'll figure this out later...
     headers.'Set-Cookie' = 'region=us-west-2; Path=/; Expires=Sun, 08 Feb 2015 20:55:37 GMT'

     some stuff to read about later:
     https://github.com/Netflix/asgard/wiki/REST-API
     http://groovy.codehaus.org/modules/http-builder/apidocs/groovyx/net/http/HttpResponseDecorator.html
     https://gist.github.com/SpOOnman/4452815

     /task/list.json will get a list of tasks asgard has done, this can get filtered down to the deploy tasks

     */

    def getApplications() {
        def credentials = grailsApplication.config.asgard.credentials
        logger.info("CALLING ASGARD")
        def url = grailsApplication.config.asgard.url
        def path = "/application/list.json"
        //TODO need to handle when the cookie expires...
        def cookies = httpRequestService.getAuthCookies(url, path, false)
        def json = httpRequestService.callRestfulUrl(url, path, null, false, cookies, credentials)

        def applications = []

        logger.info(json)

        for(def i : json) {
            logger.info(i)
            applications.add(i.name)
            logger.info("$i.name")
        }

        return applications
    }

    def getData(startAt, maxResults, project, fromDate, cookies) {
        def credentials = grailsApplication.config.asgard.credentials
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

        def json = httpRequestService.callRestfulUrl(url, path, query, true, cookies, credentials)
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
