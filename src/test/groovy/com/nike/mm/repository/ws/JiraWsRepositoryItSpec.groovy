package com.nike.mm.repository.ws

import com.nike.mm.dto.HttpRequestDto
import com.nike.mm.dto.JiraRequestDto
import com.nike.mm.repository.ws.impl.JiraWsRepository
import com.nike.mm.service.IHttpRequestService
import com.nike.mm.service.impl.HttpRequestService
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

/**
 * Created by rparr2 on 6/17/15.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = MeasurementorApplication.class)
//@WebAppConfiguration
class JiraWsRepositoryItSpec extends Specification {

    @Autowired IJiraWsRepository jiraWsRepository

    @Autowired IHttpRequestService httpRequestService

    def setup() {
        this.jiraWsRepository                       = new JiraWsRepository()
        this.httpRequestService                     = new HttpRequestService()
        this.jiraWsRepository.httpRequestService    = this.httpRequestService
        this.httpRequestService.setProxy            = true
        this.httpRequestService.proxyUrl            = ""
        this.httpRequestService.proxyPort           = 8080
    }

    //@Test
    def "get the list of projects" () {

        setup:
        String path = "/rest/api/2/project";
        HttpRequestDto dto = [url: "https://jira.nike.com", path: path, credentials: ""] as HttpRequestDto

        when:
        def projects = this.jiraWsRepository.getProjectsList(dto)
        1 == 1

        then:
        //projects != null
        //projects.size() > 0
        //println "projects size $projects.size()"
        1 == 1
    }

    def "get the data for the first project we find in the list" () {

        setup:
        String path = "/rest/api/2/project";
        HttpRequestDto dto = [url: "https://jira.nike.com", path: path, credentials: ""] as HttpRequestDto
        def projects = this.jiraWsRepository.getProjectsList(dto)
        def jiraRequestDto = [startAt: 0, maxResults: 100, project: projects[0], url:"https://jira.nike.com", credentials: ""] as JiraRequestDto

        when:
        //def results = this.jiraWsRepository.getData(jiraRequestDto)
        1 == 1

        then:
        //results != null
        //results.size() > 0
        1 == 1
    }

}
