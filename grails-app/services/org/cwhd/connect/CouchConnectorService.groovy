package org.cwhd.connect

import grails.transaction.Transactional
import org.apache.commons.logging.LogFactory

@Transactional
class CouchConnectorService {
    private static final logger = LogFactory.getLog(this)
    def httpRequestService
    def grailsApplication

    /***
     * pass in an object and i'll save it to couch for you
     * this class is a cludge to connect to a couch DB that is external to the larger system.
     * This class won't be called if you set the sendDataToCouch to false in the application.properties file.
     * @param obj
     */
    def saveToCouch(obj) {
        if(grailsApplication.config.sendDataToCouch == "true") {
            logger.debug("TIME TO CHILL ON THE COUCH")
            def couchId = httpRequestService.postToCouch(obj)
            logger.debug("CHILL COMPLETE: $couchId")
            return couchId
        } else {
            return null
        }
    }
}
