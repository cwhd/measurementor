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

    def getJobs(url, path, fromDate) {
        logger.info("----")
        logger.info("CALLING JENKINS")
        logger.info("----")
        path += "api/json"      //add this to the end of everything to get a JSON response

        def json = makeJenkinsRequest(null, path, null)
        def jobs = []
        if(json) {
            def newPath = ""
            for (def i : json.jobs) {
                logger.debug("----")
                logger.debug("$i.name")
                logger.debug("$i.url")
                logger.debug("----")
                newPath = UtilitiesService.getPathFromUrl(i.url)
                getJobs(i.url, newPath, fromDate)
                jobs.add(i.name)
            }
            //TODO maybe process the builds async, since that's the long pole here...
            for (def b in json.builds) {
                logger.info("----")
                logger.info("GETTING BUILDS FOR $b.url")
                logger.debug("----")
                newPath = UtilitiesService.getPathFromUrl(b.url)
                def buildDate = getBuild(b.url, newPath)
                if(buildDate >= fromDate) {
                    //we only need to do stuff if this was modified on or after the fromDate
                    logger.info("AFTER THE WAY BACK JENKINS DATE: $buildDate is >= then $fromDate")
                    break
                }
            }
        }
        return jobs
    }

    def getBuild(url, path) {
        def json = makeJenkinsRequest(url, path + "/api/json", null)
        if(json) {
            def cleanTimestamp = UtilitiesService.convertTimestampFromString(json.timestamp)
            def causedBy = ""
            def remoteUrl = ""
            def lastBuiltRevision = ""
            logger.debug("----")
            logger.debug("buildName: $json.fullDisplayName")
            logger.debug("buildId: $json.id")
            logger.debug("result: $json.result")
            logger.debug("buildNumber: $json.number")
            logger.debug("timstamp: $cleanTimestamp")
            logger.debug("URL: $json.url")
            //logger.info(json.changeset)
            for (def a in json.actions) {
                if (a.causes) {
                    for (def c in a.causes) {
                        logger.debug("cause User Id: $c.userId")
                        causedBy = c.userId
                    }
                }
                if (a.remoteUrls) {
                    for (def r in a.remoteUrls) { //TODO i should make sure to comma separate these...
                        logger.info("remoteUrls: $r")
                        remoteUrl += r
                    }
                }
                if (a.lastBuildRevision) {    //TODO not sure why this doesn't work yet...
                    logger.info("lastBuiltRevision: $a.lastBuiltRevision.SHA1")
                    lastBuiltRevision = a.lastBuiltRevision.SHA1
                }
            }
            def cleanDisplayName = ""
            if (json.fullDisplayName) {
                cleanDisplayName = UtilitiesService.cleanFullBuildName(json.fullDisplayName)
            }
            def jenkinsData = JenkinsData.findByBuildId(json.id)
            if (jenkinsData) {
                jenkinsData.timestamp = cleanTimestamp
                jenkinsData.jenkinsUrl = json.url
                jenkinsData.buildName = cleanDisplayName
                jenkinsData.result = json.result
                jenkinsData.duration = json.duration
                jenkinsData.causedBy = causedBy
                jenkinsData.remoteUrl = remoteUrl
                jenkinsData.lastBuiltRevision = lastBuiltRevision
                jenkinsData.dataType = "CI"
                jenkinsData.buildNumber = json.number
            } else {
                jenkinsData = new JenkinsData(buildId: json.id, timestamp: cleanTimestamp, jenkinsUrl: json.url,
                        buildName: cleanDisplayName, result: json.result, buildNumber: json.number,
                        duration: json.duration, causedBy: causedBy, remoteUrl: remoteUrl,
                        lastBuiltRevision: lastBuiltRevision, dataType: "CI")
            }

            if(grailsApplication.config.sendDataToCouch == "true") {
                def couchReturn = couchConnectorService.saveToCouch(jenkinsData)
                logger.debug("RETURNED FROM COUCH: $couchReturn")
                jenkinsData.couchId = couchReturn
            }

            jenkinsData.save(flush: true, failOnError: true)
            return cleanTimestamp

        } else {
            logger.error("ERROR: NOTHING RETURNED FOR BUILD")
        }
    }

    def makeJenkinsRequest(url, path, query) {
        def credentials = grailsApplication.config.jenkins.credentials
        if(!url) {
            url = grailsApplication.config.jenkins.url //this is defined in application.properties
        }
        try {
            return httpRequestService.callRestfulUrl(url, path, query, false, null, credentials)
            logger.debug("GOT DATA BACK FROM JENKINS")
        } catch (Exception ex) {
            //TODO handle this!!!
            logger.error("JENKINS FAIL: $ex.message")
        }
    }
}
