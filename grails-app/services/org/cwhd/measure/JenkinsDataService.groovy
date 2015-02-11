package org.cwhd.measure

import grails.transaction.Transactional
import org.apache.commons.logging.LogFactory

import javax.swing.text.Utilities

@Transactional
class JenkinsDataService {
    private static final logger = LogFactory.getLog(this)
    def grailsApplication
    def httpRequestService
    def couchConnectorService

    /**
     * TODO Notes - this is a WIP
     * start with getting all the folders(projects) in Jenkins
     * once we have the list of folders we can check each project
     * - we can see how projects are linked together by looking at the downstreamProjects and upstreamProjects nodes
     * each project has jobs
     */

    def getJobs(url, path) {
        logger.info("CALLING JENKINS")
        if(!url) {                  //the default URL is set in the application.properties
            url = grailsApplication.config.jenkins.url
        }
        path += "api/json"      //add this to the end of everything to get a JSON response
        logger.info("ABOUT TO CALL $url AND $path")
        def json = httpRequestService.callRestfulUrl(url, path, null, false)
        def jobs = []
        if(json) {
            def newPath = ""
            for (def i : json.jobs) {

                logger.info("----------------------------------")
                logger.info("$i.name")
                logger.info("$i.url")
                logger.info("----------------------------------")
                newPath = UtilitiesService.getPathFromUrl(i.url)
                getJobs(i.url, newPath)
                jobs.add(i.name)
            }
            for (def b in json.builds) {
                logger.info("----------------------------------")
                logger.info("GETTING BUILDS FOR $b.url")
                logger.info("----------------------------------")
                newPath = UtilitiesService.getPathFromUrl(b.url)
                getBuild(b.url, newPath)
            }

        }
        return jobs
    }

    def getBuild(url, path) {
        def json = httpRequestService.callRestfulUrl(url, path + "/api/json", null, false)
        if(json) {
            def causedBy = ""
            def remoteUrl = ""
            def lastBuiltRevision = ""
            logger.info("----------------------------------")
            logger.info("buildName: $json.fullDisplayName")
            logger.info("buildId: $json.id")
            logger.info("result: $json.result")
            logger.info("buildNumber: $json.number")
            logger.info("timstamp: $json.timestamp") //TODO it would be better to make this a real date
            logger.info("URL: $json.url")
            logger.info(json.changeset)
            for(def a in json.actions) {
                if(a.causes) {
                    for(def c in a.causes) {
                        logger.info("cause User Id: $c.userId")
                        causedBy = c.userId
                    }
                }
                if(a.remoteUrls) {
                    for(def r in a.remoteUrls) { //TODO i should make sure to comma separate these...
                        logger.info("remoteUrls: $r")
                        remoteUrl += r
                    }
                }
                if(a.lastBuildRevision) {    //TODO not sure why this doesn't work yet...
                    logger.info("lastBuiltRevision: $a.lastBuiltRevision.SHA1")
                    lastBuiltRevision = a.lastBuiltRevision.SHA1
                }
            }
            logger.info("----------------------------------")
            def cleanTimestamp = null
            if(json.timestamp) {
                cleanTimestamp = new Date((long)json.timestamp)
            }
            def cleanDisplayName = ""
            if(json.fullDisplayName) {
                cleanDisplayName = UtilitiesService.cleanFullBuildName(json.fullDisplayName)
            }
            def jenkinsData = JenkinsData.findByBuildId(json.id)
            if(jenkinsData) {
                jenkinsData.timestamp = cleanTimestamp
                jenkinsData.jenkinsUrl = json.url
                jenkinsData.buildName = cleanDisplayName
                jenkinsData.result = json.result
                jenkinsData.duration = json.duration
                jenkinsData.causedBy = causedBy
                jenkinsData.remoteUrl = remoteUrl
                jenkinsData.lastBuiltRevision = lastBuiltRevision
                jenkinsData.dataType = "CI"
            } else {
                jenkinsData = new JenkinsData(buildId: json.id, timestamp: cleanTimestamp, url: json.url,
                        buildName: cleanDisplayName, result: json.result, buildNumber: json.number,
                        duration: json.duration, causedBy: causedBy, remoteUrl: remoteUrl,
                        lastBuiltRevision: lastBuiltRevision, dataType: "CI")
            }
            def couchReturn = couchConnectorService.saveToCouch(jenkinsData)
            logger.info("RETURNED FROM COUCH: $couchReturn")
            jenkinsData.couchId = couchReturn

            jenkinsData.save(flush: true, failOnError: true)
        } else {
            logger.error("ERROR: NOTHING RETURNED FOR BUILD")
        }
    }
}
