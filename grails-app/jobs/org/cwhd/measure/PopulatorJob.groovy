package org.cwhd.measure

import org.cwhd.measure.system.JobHistory

import java.util.concurrent.TimeUnit

/**
 * the main job that calls all the services to get data.  note that history
 * is stored in mongo along with everything else
 */
class PopulatorJob {
    def jiraDataService
    def stashDataService

    static triggers = {
        simple startDelay: 5000l, repeatCount:0
    }

    //TODO this would be best if i can pass in the last date this ran...
    //if i have that date, then query for all changes since last run
    //if i don't have it get everything!
    def execute() {

        /*

        def result = "unknown"
        try {
            def startDateTime = new Date()
            jiraDataService.getData(0, 100, "ACOE")
            stashDataService.getAll()
            def doneDateTime = new Date()
            def difference = doneDateTime.getTime() - startDateTime.getTime()
            def minutesDiff = TimeUnit.MILLISECONDS.toMinutes(difference)
            result = "success in $difference ms"
            println "---------------------------------------------------"
            println "ALL DONE IN ~$minutesDiff minutes"
            println "---------------------------------------------------"
        } catch (Exception ex) {
            result = "FAIL: $ex.message"
        }

        JobHistory history = new JobHistory(jobDate: new Date(), jobResult: result)
        history.save(failOnError: true)
        */
    }
}
