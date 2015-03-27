class BootStrap {
    def grailsApplication
    def updateDataFromSourceService
    def asgardDataService
    def jenkinsDataService
    def stashDataService

    def init = { servletContext ->
        println("GO TIME!")

        if(grailsApplication.config.runOnStartup == "true") {
            updateDataFromSourceService.getAllData()
        }

    }
    def destroy = {
    }
}