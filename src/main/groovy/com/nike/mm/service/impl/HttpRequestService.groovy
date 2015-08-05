package com.nike.mm.service.impl

import com.nike.mm.dto.HttpRequestDto
import com.nike.mm.service.IHttpRequestService
import groovy.util.logging.Slf4j
import groovyx.net.http.HTTPBuilder
import org.springframework.stereotype.Service

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.URLENC

@Slf4j
@Service
class HttpRequestService implements IHttpRequestService {

    Object callRestfulUrl(HttpRequestDto httpRequestDto) {

        def http = new HTTPBuilder(httpRequestDto.url)

        http.ignoreSSLIssues()

        if (httpRequestDto.proxyDto && httpRequestDto.proxyDto.url) {
            http.setProxy(httpRequestDto.proxyDto.url, httpRequestDto.proxyDto.port, null)
        }

        http.request(GET, JSON) { req ->
            log.debug("Request path: " + http.getUri().getPath() + httpRequestDto.path)
            uri.path = http.getUri().getPath() + httpRequestDto.path
            if (httpRequestDto.query) {
                uri.query = httpRequestDto.query
            }
            headers.'User-Agent' = 'Mozilla/5.0 Ubuntu/8.10 Firefox/3.0.4'
            if (httpRequestDto.credentials) {
                headers.'Authorization' = "Basic " + httpRequestDto.credentials
            }
            headers.'Accept' = 'application/json'
            headers.'Content-Type' = 'application/json'
            if (httpRequestDto.authCookies) {
                headers.'Cookie' = httpRequestDto.authCookies.join(';')
            }

            response.success = { resp, json ->
                return json
            }

            // handler for any failure status code:
            //TODO i should handle this failure better
            response.failure = { resp ->
//				log.error("HTTP Fail! $resp")
                log.debug("Erk $resp.status")
//				log.debug(resp.getData())
//				log.debug(resp.getHeaders())
                return null
            }
        }

        //TODO i need a post in here for elastic :^(
    }

    Object postSomething(def url, def postBody) {
        println "in the post..."
        println url
        postBody = ["size": 0, "aggs": ["peeps": ["terms": ["field":"people","size": 100]]]]
        println postBody

        def http = new HTTPBuilder(url)
        http.request( POST, JSON ) { req ->
            body =  postBody

            response.success = { resp, json ->
                println "RESPONSE"
                resp.properties.each { prop, val ->
                    println "$prop : $val"
                }
                println "uri: ${http.uri}"
                println "uri.path: ${http.getUri().getPath()}"
                println "POST Success: ${resp.statusLine}"
                println json

                return json

            }
        }

//        http.post( path: path, body: postBody, requestContentType: URLENC ) { resp, json ->
//
//            println "uri: ${http.uri}"
//            println "uri.path: ${http.getUri().getPath()}"
//            println "POST Success: ${resp.statusLine}"
//            return json
//        }

//        http.request(POST) {
//            uri.path = path
//            send URLENC, postBody
//
//            response.success = { resp, json ->
//                println "RESPONSE"
//                resp.properties.each { prop, val ->
//                    println "$prop : $val"
//                }
//                println "POST response status: ${resp.statusLine}"
//                println "uri: ${http.uri}"
//                println "uri.path: ${http.getUri().getPath()}"
//                println "POST Success: ${resp.statusLine}"
//                println json
//                return json
//            }
//
//        }
    }

}