package com.nike.mm.service.impl

import com.nike.mm.dto.HttpRequestDto
import com.nike.mm.service.IHttpRequestService
import groovyx.net.http.HTTPBuilder
import org.springframework.stereotype.Service

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET

@Service
class HttpRequestService implements IHttpRequestService {
	
	Object callRestfulUrl(HttpRequestDto httpRequestDto) {
		
		def http = new HTTPBuilder( httpRequestDto.url )

		http.ignoreSSLIssues()

        if(httpRequestDto.proxyDto) {
            http.setProxy(httpRequestDto.proxyDto.url, httpRequestDto.proxyDto.port, null)
        }

		http.request( GET, JSON ) { req ->
			uri.path = http.getUri().getPath() + httpRequestDto.path
			if(httpRequestDto.query) {
				uri.query = httpRequestDto.query
			}
			headers.'User-Agent' = 'Mozilla/5.0 Ubuntu/8.10 Firefox/3.0.4'
			if (httpRequestDto.credentials) {
				headers.'Authorization' =  "Basic " + httpRequestDto.credentials
			}
			headers.'Accept' = 'application/json'
			headers.'Content-Type' = 'application/json'
			if(httpRequestDto.authCookies) {
				headers.'Cookie' = httpRequestDto.authCookies.join(';')
			}

			response.success = { resp, json ->
				return json
			}

			// handler for any failure status code:
			//TODO i should handle this failure better
			response.failure = { resp ->
//				log.error("HTTP Fail! $resp")
				println "Erk"
//				log.debug(resp.getData())
//				log.debug(resp.getHeaders())
				return null
			}
		}
	}
}
