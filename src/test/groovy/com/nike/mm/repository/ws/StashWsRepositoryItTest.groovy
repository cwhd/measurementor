package com.nike.mm.repository.ws

import com.nike.mm.dto.HttpRequestDto
import com.nike.mm.repository.ws.impl.StashWsRepository
import com.nike.mm.service.IHttpRequestService
import com.nike.mm.service.impl.HttpRequestService
import groovyx.net.http.HTTPBuilder
import spock.lang.Specification

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET

/**
 * Created by rparr2 on 6/16/15.
 */
class StashWsRepositoryItTest extends Specification {

    IStashWsRepository stashWsRepository

    IHttpRequestService httpRequestService

    def setup() {
        this.stashWsRepository                      = new StashWsRepository()
        this.httpRequestService                     = new HttpRequestService()
        this.stashWsRepository.httpRequestService   = this.httpRequestService
    }


    def "test to help me figure out this stuff to make it easier" () {

        setup:
        String path = "/rest/api/1.0/projects";
        HttpRequestDto dto = [url: "http://stash.nike.com", path: path, query:[start: 0, limit: 300]] as HttpRequestDto

        when:
        def projects = this.stashWsRepository.findAllProjects(dto)

        then:
        projects != null
        !projects.isEmpty()
    }
}
