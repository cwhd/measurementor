class BootStrap {
    def grailsApplication
    def updateDataFromSourceService

    def init = { servletContext ->
        if(grailsApplication.config.runOnStartup) {
            updateDataFromSourceService.getAllData()
        }
    }
    def destroy = {
    }
}
