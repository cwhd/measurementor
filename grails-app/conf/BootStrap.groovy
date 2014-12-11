import org.cwhd.measure.system.JobHistory

import java.util.concurrent.TimeUnit

class BootStrap {
    def jiraDataService
    def stashDataService

    def init = { servletContext ->

        println "GO!"
        //TODO in

        def result = "unknown"
        try {
            def startDateTime = new Date()
            //TODO i should get all the projects from somewhere, probably a configuration DB
            //TODO i should only get data that's changed since the last time the job ran...i should be able to figure
            //that out from the jobHistory table
            stashDataService.getAll()
            //jiraDataService.getData(0, 100, "ACOE")
            //jiraDataService.reIndexEC()
            def doneDateTime = new Date()
            def difference = doneDateTime.getTime() - startDateTime.getTime()
            def minutesDiff = TimeUnit.MILLISECONDS.toMinutes(difference)
            result = "success in $difference ms"
            println "---------------------------------------------------"
            println "ALL DONE IN ~$minutesDiff minutes"
            println "---------------------------------------------------"
        } catch (Exception ex) {
            result = "FAIL: $ex.message"
            println "FAIL: $ex"
        }

        JobHistory history = new JobHistory(jobDate: new Date(), jobResult: result)
        history.save(failOnError: true)

    }
    def destroy = {
    }
}
