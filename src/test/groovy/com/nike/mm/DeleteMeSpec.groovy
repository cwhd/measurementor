package com.nike.mm

import com.nike.mm.dto.HttpRequestDto
import com.nike.mm.dto.ProxyDto
import com.nike.mm.repository.ws.IJenkinsWsRepository
import com.nike.mm.repository.ws.IStashWsRepository
import com.nike.mm.repository.ws.impl.JenkinsWsRepository
import com.nike.mm.repository.ws.impl.StashWsRepository
import com.nike.mm.service.IHttpRequestService
import com.nike.mm.service.impl.HttpRequestService
import spock.lang.Specification

/**
 * Created by rparr2 on 6/30/15.
 */
class DeleteMeSpec extends Specification {

    IStashWsRepository stashWsRepository

    IJenkinsWsRepository jenkinsWsRepository

    IHttpRequestService httpRequestService

    def setup() {
//        this.stashWsRepository = new StashWsRepository()
//        this.stashWsRepository.httpRequestService = new HttpRequestService()

        this.jenkinsWsRepository = new JenkinsWsRepository()
        this.jenkinsWsRepository.httpRequestService = new HttpRequestService()
    }

//    def "lets get this to break for us"() {
//
//        //https://jenkins.tools.nikecloud.com/api/json
//
//        setup:
//        //def proxtdto = [url:"connsvr.nike.com", port:8080, setProxy: true] as ProxyDto
//        def proxtdto = [] as ProxyDto
//
//        HttpRequestDto dto = [url: "http://stash.nike.com", credentials: "Uy5RTUF1dG9tYXRpb246UEhFZHJlM2E=", path: "/rest/api/1.0/projects", query: [start: 0, limit: 300], proxyDto: proxtdto] as HttpRequestDto
//
//        when: "http://stash.nike.com/rest/api/1.0/projects"
//        this.stashWsRepository.findAllProjects(dto)
//
//        then:
//        1 == 1
//    }

    def "lets get this to break for us"() {

        //https://jenkins.tools.nikecloud.com/api/json

        setup:
        //def proxtdto = [url:"connsvr.nike.com", port:8080, setProxy: true] as ProxyDto
        def proxtdto = [] as ProxyDto
        def url = "https://jenkins.tools.nikecloud.com"
        def path = "/api/json"
        def credentials = "Uy5RTUF1dG9tYXRpb246UEhFZHJlM2E="

        HttpRequestDto dto = [url: url, credentials: credentials, path: path, query: [start: 0, limit: 300], proxyDto: proxtdto] as HttpRequestDto

        when: "https://jenkins.tools.nikecloud.com/api/json"
        this.jenkinsWsRepository.findListOfJobs(dto)

        then:
        1 == 1
    }
}
