class BootStrap {
    def grailsApplication
    def updateDataFromSourceService
    def asgardDataService
    def jenkinsDataService
    def stashDataService

    def init = { servletContext ->
        println("GO TIME!")

//        asgardDataService.getApplications()
        //jenkinsDataService.getJobs(null, "", new Date()-1)
        //stashDataService.getAll(new Date()-300)

        if(grailsApplication.config.runOnStartup == "true") {
            updateDataFromSourceService.getAllData()
        }

    }
    def destroy = {
    }
}