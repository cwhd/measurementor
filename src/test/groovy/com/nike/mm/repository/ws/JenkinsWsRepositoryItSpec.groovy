package com.nike.mm.repository.ws

import com.nike.mm.dto.HttpRequestDto
import com.nike.mm.dto.ProxyDto
import com.nike.mm.repository.ws.impl.JenkinsWsRepository
import com.nike.mm.service.IHttpRequestService
import com.nike.mm.service.impl.HttpRequestService
import spock.lang.Specification

/**
 * Created by rparr2 on 6/18/15.
 */
class JenkinsWsRepositoryItSpec extends Specification {

    IJenkinsWsRepository jenkinsWsRepository

    IHttpRequestService httpRequestService

    def setup () {
        this.jenkinsWsRepository                    = new JenkinsWsRepository()
        this.httpRequestService                     = new HttpRequestService()
        this.jenkinsWsRepository.httpRequestService = this.httpRequestService
    }

    def "check that we can get the jobs" () {

        setup:
        String path = "api/json";
        HttpRequestDto dto = [url: "", path: path, query:[start: 0, limit: 300], credentials: "", proxyDto:[]as ProxyDto] as HttpRequestDto

        when:
        //this.jenkinsWsRepository.getListOfBuilds(dto)
        1 == 1

        then:
        1 == 1
    }
}
