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
     * @param obj
     */
    def saveToCouch(obj) {
        def doThis = grailsApplication.config.sendDataToCouch
        if(doThis) {
            logger.info("TIME TO CHILL ON THE COUCH")
            def couchId = httpRequestService.postToCouch(obj)
            logger.info("CHILL COMPLETE: $couchId")
            return couchId
        } else {
            return null
        }
    }
}
