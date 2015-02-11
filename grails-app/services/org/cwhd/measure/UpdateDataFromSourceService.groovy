package org.cwhd.measure

import grails.transaction.Transactional
import org.cwhd.measure.system.JobHistory
import org.apache.commons.logging.LogFactory
import java.util.concurrent.TimeUnit

@Transactional
class UpdateDataFromSourceService {
    private static final logger = LogFactory.getLog(this)
    def jiraDataService
    def stashDataService
    def grailsApplication
    def jenkinsDataService

    //TODO there should be a seperate method to get data from everything and a single method that calls them in parallel

    def getAllData() {

        def result = "unknown"
        def startDateTime = new Date()
        JobHistory history = new JobHistory(startDate: startDateTime, jobResult: result, projectCount: 0)
        def jobNotes = ""

        try {
            //get the info from the last job that ran...
            def lastJob = JobHistory.list([max: 1, sort: "startDate", order: "desc"])
            logger.info("---------------------------------------------------")
            logger.info("last job started: $lastJob.startDate : and took: $lastJob.completionTime : and was: $lastJob.jobResult")
            logger.info("---------------------------------------------------")
            def wayBackDiff = 32 //if we don't know when the last job ran, get data from yesterday
            //TODO fix this...
            //TODO getting the waybackdate should be a function and i should have a unit test...
            if(lastJob.jobResult == "done") {
                logger.info("LAST JOB WAS DONE!")
                wayBackDiff = TimeUnit.MILLISECONDS.toDays(startDateTime.getTime() - lastJob.startDate.getTime()) + 1
                logger.info("AND WAY BACK DIFF IS $wayBackDiff")
            }

            logger.info("wayBackDiff: $wayBackDiff")
            def wayBackDate = startDateTime - wayBackDiff
            def getDataFrom = wayBackDate.format("YYYY-MM-dd")
            logger.info("getDataFrom: $getDataFrom")

            try {
                stashDataService.getAll(wayBackDate)
            } catch (Exception e) {
                logger.error("CRAP, STASH DATA SERVICE FAILED" + e.message)
            }
            //TODO stashDataService should also get data from the way back date
            //TODO need to finish this guy up...
            try {
                jenkinsDataService.getJobs(null, "")
            } catch (Exception e) {
                logger.error("CRAP, JENKINS DATA SERVICE FAILED" + e.message)
            }

            def jiraProjects = jiraDataService.getProjects()
            history.projectCount = jiraProjects.size()
            for (def p : jiraProjects) {
                logger.info("-----")
                logger.info("getting data for $p")
                try {
                    jiraDataService.getData(0, 250, p, getDataFrom)
                } catch (Exception e) {
                    logger.info("JIRA FAIL: $e.stackTrace")
                    jobNotes += "JIRA FAIL: $p : $e.message"
                }
            }

            def doneDateTime = new Date()
            def minutesDiff = TimeUnit.MILLISECONDS.toMinutes(doneDateTime.getTime() - startDateTime.getTime())
            result = "done"
            history.completionTime = minutesDiff

            logger.info("---------------------------------------------------")
            logger.info("ALL DONE IN ~$minutesDiff minutes")
            logger.info("---------------------------------------------------")
        } catch (Exception ex) {
            result = "FAIL: $ex.message"
            logger.info("FAIL: $ex.stackTrace")
            jobNotes += "GENERAL FAIL: $ex.message"
            jiraDataService.reIndexEC() //if something fails above sometimes the re-index doesn't happen
        }

        history.jobNotes = jobNotes
        history.jobResult = result
        history.save(failOnError: true)

    }
}
