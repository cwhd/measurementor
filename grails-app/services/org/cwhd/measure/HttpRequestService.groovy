package org.cwhd.measure

import grails.transaction.Transactional
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET
import org.apache.commons.logging.LogFactory

/***
 * utility service to make HTTP requests
 */
@Transactional
class HttpRequestService {
    private static final logger = LogFactory.getLog(this)
    def grailsApplication

    def callRestfulUrl(url, path, query, setProxy) {

        def http = new HTTPBuilder( url )
        if(setProxy) {
            http.setProxy('connsvr.nike.com',8080,null)
        }

        http.request( GET, JSON ) {
            uri.path = path
            if(query) {
                uri.query = query
            }

            logger.info("getting data from $uri")

            headers.'User-Agent' = 'Mozilla/5.0 Ubuntu/8.10 Firefox/3.0.4'
            headers.'Authorization' = 'Basic ' + grailsApplication.config.stash.credentials
            headers.'Accept' = 'application/json'
            headers.'Content-Type' = 'application/json'

            response.success = { resp, json ->
               // logger.debug(json)
                return json
            }

            // handler for any failure status code:
            //TODO i should handle this failure better
            response.failure = { resp ->
                logger.error("HTTP Fail! $resp")
                logger.info(resp.getStatus())
                logger.info(resp.getData())
                logger.info(resp.getHeaders())
                return null
            }
        }
    }
}
