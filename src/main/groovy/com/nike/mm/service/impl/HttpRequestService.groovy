package com.nike.mm.service.impl

import static groovyx.net.http.ContentType.HTML
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.Method.GET
import static groovyx.net.http.Method.POST
import lombok.extern.java.Log;
import groovy.json.*
import groovyx.net.http.HTTPBuilder

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import com.nike.mm.dto.HttpRequestDto
import com.nike.mm.service.IHttpRequestService

@Log
@Service
class HttpRequestService implements IHttpRequestService {
	
	@Value('${mm.proxy.set_proxy}')
	private boolean setProxy;
	
	@Value('${mm.proxy.url}')
	private String proxyUrl;
	
	@Value('${mm.proxy.port}')
	private String proxyPort;
	
	Object callRestfulUrl(HttpRequestDto httpRequestDto) {
		
		def http = new HTTPBuilder( httpRequestDto.url )
		if(this.setProxy) {
			http.setProxy(this.proxyUrl, this.proxyPort,null)
		}
		http.request( GET, JSON ) { req ->
			uri.path = httpRequestDto.path
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
