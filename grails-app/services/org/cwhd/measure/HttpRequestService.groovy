package org.cwhd.measure

import grails.transaction.Transactional
import groovyx.net.http.HTTPBuilder

import static groovyx.net.http.ContentType.HTML
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET
import org.apache.commons.logging.LogFactory
import groovy.json.*
import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.Method.POST

/***
 * utility service to make HTTP requests
 */
@Transactional
class HttpRequestService {
    private static final logger = LogFactory.getLog(this)
    def grailsApplication

    def callRestfulUrl(url, path, query, setProxy, authCookies, credentials) {

        def http = new HTTPBuilder( url )
        if(setProxy) {
            http.setProxy('connsvr.nike.com',8080,null)
        }

        http.request( GET, JSON ) {
            uri.path = path
            if(query) {
                uri.query = query
            }

            logger.debug("getting data from $uri")

            headers.'User-Agent' = 'Mozilla/5.0 Ubuntu/8.10 Firefox/3.0.4'
            headers.'Authorization' = 'Basic ' + credentials
            headers.'Accept' = 'application/json'
            headers.'Content-Type' = 'application/json'
            if(authCookies) {
                logger.info("GOT COOKIES $authCookies")
                headers.'Cookie' = authCookies.join(';')
            }

            response.success = { resp, json ->
               // logger.debug(json)
                return json
            }

            // handler for any failure status code:
            //TODO i should handle this failure better
            response.failure = { resp ->
                logger.error("HTTP Fail! $resp")
                logger.info(resp.getStatus())
                logger.debug(resp.getData())
                logger.debug(resp.getHeaders())
                return null
            }
        }
    }

    /**
     * use this method if you want to transfer this data to a couchbase server.  It's kind of hacky
     * but it works.
     * @param obj
     * @return
     */
    def postToCouch(obj) {

        def couchId = ""
        def requestType = groovyx.net.http.Method.POST
        //if we already have an ID this is an update
        if(obj.couchId){
            logger.debug("UPDATING!")
            couchId = "/" + obj.couchId
            requestType = groovyx.net.http.Method.PUT
        }
        def http = new HTTPBuilder(grailsApplication.config.couchbase.url)

        //this is kind of a hack, but for some reason JSONBuilder has a circular reference?  maybe something with GORM?
        StringBuilder sb = new StringBuilder()
        def propertyCounter = 0
        sb.append("{")
        obj.properties.each { k,v ->
            if(propertyCounter > 0) {
                sb.append(",")
            }
            sb.append("\"$k\":")
            if(v instanceof Date) {
                logger.debug("FOUND A DATE: $k : $v")
                logger.debug(v.format("yyyy-MM-dd'T'HH:mm:ss'Z'"))
                sb.append("\"" + v.format("yyyy-MM-dd'T'HH:mm:ss'Z'") + "\"")
            } else if(v instanceof Number) {
                sb.append("$v")
            } else {
                sb.append("\"$v\"")
            }
            propertyCounter++
        }
        sb.append("}")

        http.request( requestType, JSON ) {
            uri.path = "/measurementor$couchId"
            uri.port = 8092

            logger.debug("$requestType TO $uri")

            headers.'Authorization' = 'Basic ' + grailsApplication.config.couchbase.credentials
            headers.'Content-Type' = 'application/json'
            def bodString = sb.toString()
            body =  bodString
            logger.debug(bodString)

            response.success = { resp, json ->
                logger.debug("OK BY COUCH:")
                resp.properties.each { k, v ->
                    logger.debug("\"$k\":\"$v\"")
                }
                //since couch returns an ID in the header we need to find it that way
                for (def h in resp.getHeaders("Location")) {
                    def tokens = h.getValue().toString().split("/")
                    couchId = tokens[tokens.size()-1]
                }
            }

            // handler for any failure status code:
            //TODO i should handle this failure better
            response.failure = { resp ->
                logger.error("Couch HTTP Fail! $resp")
                logger.info(resp.getStatus())
                logger.debug(resp.getData())
                logger.debug(resp.getHeaders())
                couchId = null
            }
        }
        logger.debug("THE ID IS: $couchId")
        return couchId
    }

    /**
     * Some applications need auth cookies to log in.  This will get the cookies returned from an
     * authentication service and return them.
     * @param url
     * @param path
     * @param setProxy
     * @return
     */
    def getAuthCookies(url, path, setProxy) {
        def http = new HTTPBuilder( url )
        if(setProxy) {
            http.setProxy('connsvr.nike.com',8080,null)
        }
        def cookies = []

        http.request( GET, HTML ) {
            uri.path = path

            logger.info("getting data from $uri")

            headers.'User-Agent' = 'Mozilla/5.0 Ubuntu/8.10 Firefox/3.0.4'
            headers.'Authorization' = 'Basic ' + grailsApplication.config.stash.credentials
            headers.'Accept' = 'application/json'
            headers.'Content-Type' = 'application/json'

            response.success = { resp, json ->
                logger.info("YAY IT WORKED!")
                resp.getHeaders('Set-Cookie').each {
                    def cookie = it.value.split(';')[0]
                    logger.info("COOKIE: $cookie")
                    cookies.add(cookie)
                }
                //cookies.add("JSESSIONID=F43C0B6D1F87969299EA7949887F1923")
                resp.properties.each { k, v ->
                    logger.info("\"$k\":\"$v\"")
                }
                for (def h in resp.getAllHeaders()) {
                    logger.info("HEADER NAME:" + h.getName())
                    logger.info("HEADER VAL:" + h.getValue())
                }
                logger.info(json)

                return cookies
            }

            // handler for any failure status code:
            //TODO i should handle this failure better
            response.failure = { resp ->
                logger.info("FAIL")
                resp.properties.each { k, v ->
                    logger.info("\"$k\":\"$v\"")
                }

                logger.error("HTTP Fail! $resp")
                logger.info(resp.getStatus())
                logger.info(resp.getData())
                logger.info(resp.getHeaders())
                return null
            }
        }
    }
}
