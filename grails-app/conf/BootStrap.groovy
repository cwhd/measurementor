class BootStrap {
    def grailsApplication
    def updateDataFromSourceService
    def githubDataService

    def init = { servletContext ->
        println("GO TIME!")

        if(grailsApplication.config.runOnStartup == "true") {
            updateDataFromSourceService.getAllData()
        }

    }
    def destroy = {
    }
}