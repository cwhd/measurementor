class BootStrap {
    def grailsApplication
    def updateDataFromSourceService
    def asgardDataService
    def jenkinsDataService
    def couchConnectorService

    def init = { servletContext ->
        println("GO TIME!")
        //asgardDataService.getApplications()
        //jenkinsDataService.getJobs(null, "")
        //couchConnectorService.saveToCouch(new org.cwhd.measure.JiraData(key: "ACOE-0"))

        if(grailsApplication.config.runOnStartup == "true") {
            updateDataFromSourceService.getAllData()
        }
    }
    def destroy = {
    }
}