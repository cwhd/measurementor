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

    def getAllData() {

        def result = "unknown"
        def startDateTime = new Date()
        JobHistory history = new JobHistory(startDate: startDateTime, jobResult: result, projectCount: 0)
        def jobNotes = ""

        try {
            def lastJob = JobHistory.list([max: 1, sort: "startDate", order: "desc"])
            logger.info("---------------------------------------------------")
            logger.info("last job started: $lastJob.startDate : and took: $lastJob.completionTime : and was: $lastJob.jobResult")
            logger.info("---------------------------------------------------")
            def wayBackDiff = 3
            //TODO fix this...
            if(lastJob.jobResult == "done") {
                wayBackDiff = TimeUnit.MILLISECONDS.toDays(startDateTime.getTime() - lastJob.startDate.getTime()) + 1
            }
            logger.info("wayBackDiff: $wayBackDiff")
            def wayBackDate = startDateTime - wayBackDiff

            //TODO i should only get data that's changed since the last time the job ran...i should be able to figure
            //that out from the jobHistory table
            //stashDataService.getAll()
            /*
        for(source in SourceType.findAll()) {
            if(source.systemName == SourceType.SystemName.JIRA) {
                jiraDataService.getData(0, 100, source.sourceName, null)
            } else if(source.SystemName == SourceType.SystemName.STASH) {
                //stashDataService.getAll()
            }
        }
        */
            def getDataFrom = wayBackDate.format("YYYY-MM-dd")
            logger.info("getDataFrom: $getDataFrom")
            def jiraProjects = jiraDataService.getProjects()
            history.projectCount = jiraProjects.size()
            for (def p : jiraProjects) {
                logger.info("************************************")
                logger.info("getting data for $p")
                logger.info("************************************")
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
