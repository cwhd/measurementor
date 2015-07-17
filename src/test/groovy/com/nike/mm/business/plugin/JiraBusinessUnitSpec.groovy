package com.nike.mm.business.plugin

import com.nike.mm.business.plugin.data.IJiraDataForTests
import com.nike.mm.business.plugins.IJiraBusiness
import com.nike.mm.business.plugins.impl.JiraBusiness
import com.nike.mm.repository.es.plugins.IJiraEsRepository
import com.nike.mm.repository.es.plugins.IJiraHistoryEsRepository
import com.nike.mm.repository.ws.IJiraWsRepository
import com.nike.mm.service.IUtilitiesService
import com.nike.mm.service.impl.UtilitiesService
import org.apache.commons.lang3.StringUtils
import org.springframework.data.domain.PageImpl
import spock.lang.Specification

class JiraBusinessUnitSpec extends Specification {

    IJiraBusiness jiraBusiness

    IJiraWsRepository jiraWsRepository

    IUtilitiesService utilitiesService

    IJiraHistoryEsRepository jiraHistoryEsRepository

    IJiraEsRepository jiraEsRepository

    def setup() {
        this.jiraBusiness                           = new JiraBusiness()
        this.jiraWsRepository                       = Mock(IJiraWsRepository)
        this.jiraHistoryEsRepository                = Mock(IJiraHistoryEsRepository)
        this.jiraEsRepository                       = Mock(IJiraEsRepository)
        this.utilitiesService                       = new UtilitiesService()
        this.jiraBusiness.jiraWsRepository          = this.jiraWsRepository
        this.jiraBusiness.jiraHistoryEsRepository   = this.jiraHistoryEsRepository
        this.jiraBusiness.utilitiesService          = this.utilitiesService
        this.jiraBusiness.jiraEsRepository          = this.jiraEsRepository
    }

    def "make sure you have the type set"() {

        when:
        def type = this.jiraBusiness.type()

        then:
        type == "Jira"
    }

    def "confirm valid config"() {

        setup:
        def config = [url:"http://google.com", credentials: "credentials", proxyUrl: "proxyUrl", proxyPort: 8080]

        when:
        String errorMessage = this.jiraBusiness.validateConfig(config)

        then:
        StringUtils.isEmpty(errorMessage)
    }

    def "invalid config - emtpy"() {

        setup:
        def config = []

        when:
        String errorMessage = this.jiraBusiness.validateConfig([])

        then:
        errorMessage  == "Jira: Missing url, Jira: Missing credentials"
    }

    def "invalid config - no credentials"() {

        setup:
        def config = [url:"http://google.com"]

        when:
        String errorMessage = this.jiraBusiness.validateConfig(config)

        then:
        errorMessage  == "Jira: Missing credentials"
    }

    def "valid config - no proxy url or proxy port"() {

        setup:
        def config = [url:"http://google.com", credentials: "credentials"]

        when:
        String errorMessage = this.jiraBusiness.validateConfig(config)

        then:
        StringUtils.isEmpty(errorMessage)
    }

    def "invalid config - no proxy port"() {

        setup:
        def config = [url:"http://google.com", credentials: "credentials", proxyUrl: "proxyUrl"]

        when:
        String errorMessage = this.jiraBusiness.validateConfig(config)

        then:
        errorMessage  == "Jira: Missing proxy port"
    }

    def "invalid config - non integer proxy port"() {

        setup:
        def config = [url:"http://google.com", credentials: "credentials", proxyUrl: "proxyUrl", proxyPort: "string"]

        when:
        String errorMessage = this.jiraBusiness.validateConfig(config)

        then:
        errorMessage  == "Jira: Proxy must be a positive integer"
    }

    def "invalid config - negative integer proxy port"() {

        setup:
        def config = [url:"http://google.com", credentials: "credentials", proxyUrl: "proxyUrl", proxyPort: -5]

        when:
        String errorMessage = this.jiraBusiness.validateConfig(config)

        then:
        errorMessage  == "Jira: Proxy must be a positive integer"
    }

    def "invalid config - zero integer proxy port"() {

        setup:
        def config = [url:"http://google.com", credentials: "credentials", proxyUrl: "proxyUrl", proxyPort: 0]

        when:
        String errorMessage = this.jiraBusiness.validateConfig(config)

        then:
        errorMessage  == "Jira: Missing proxy port"
    }

    def "update data and general workflow"() {

        setup:
        def config = [url:"http://made.up", credentials:"credentials"]
        def fromDate = Date.parse( 'dd-MM-yyyy', "01-01-2001" )
        def jiraPage = new PageImpl<String>([])

        when:
        this.jiraBusiness.updateDataWithResponse(fromDate, config);

        then:
        1 * this.jiraWsRepository.getProjectsList(_)        >> ['PROJECTA']
        1 * this.jiraEsRepository.findByJiraProject(_,_)    >> jiraPage
        2 * this.jiraWsRepository.getDataForProject(_)      >>> [IJiraDataForTests.JIRA_DATA, IJiraDataForTests.JIRA_DATA_NO_ISSUES]
        1 * this.jiraHistoryEsRepository.findOne(_)         >> null
        1 * this.jiraEsRepository.findOne(_)                >> null
    }

    def "update data and general workflow with jira record that exists"() {

        setup:
        def config = [url:"http://made.up", credentials:"credentials"]
        def fromDate = Date.parse( 'dd-MM-yyyy', "01-01-2001" )
        def jiraPage = new PageImpl<String>([])

        when:
        this.jiraBusiness.updateDataWithResponse(fromDate, config);

        then:
        1 * this.jiraWsRepository.getProjectsList(_)        >> ['PROJECTA']
        1 * this.jiraEsRepository.findByJiraProject(_,_)    >> jiraPage
        2 * this.jiraWsRepository.getDataForProject(_)      >>> [IJiraDataForTests.JIRA_DATA, IJiraDataForTests.JIRA_DATA_NO_ISSUES]
        1 * this.jiraHistoryEsRepository.findOne(_)         >> null
        1 * this.jiraEsRepository.findOne(_)                >> [:]
    }

    def "bug fix when author address is null"() {

        setup:
        def config = [url:"http://made.up", credentials:"credentials"]
        def fromDate = Date.parse( 'dd-MM-yyyy', "01-01-2001" )
        def jiraPage = new PageImpl<String>([])

        when:
        this.jiraBusiness.updateDataWithResponse(fromDate, config);

        then:
        1 * this.jiraWsRepository.getProjectsList(_)        >> ['PROJECTA']
        1 * this.jiraEsRepository.findByJiraProject(_,_)    >> jiraPage
        2 * this.jiraWsRepository.getDataForProject(_)      >>> [IJiraDataForTests.JIRA_DATA_AUTHOR_NULL, IJiraDataForTests.JIRA_DATA_NO_ISSUES]
        1 * this.jiraHistoryEsRepository.findOne(_)         >> null
        1 * this.jiraEsRepository.findOne(_)                >> null
    }

    def "bug fix when email address is null"() {

        setup:
        def config = [url:"http://made.up", credentials:"credentials"]
        def fromDate = Date.parse( 'dd-MM-yyyy', "01-01-2001" )
        def jiraPage = new PageImpl<String>([])

        when:
        this.jiraBusiness.updateDataWithResponse(fromDate, config);

        then:
        1 * this.jiraWsRepository.getProjectsList(_)        >> ['PROJECTA']
        1 * this.jiraEsRepository.findByJiraProject(_,_)    >> jiraPage
        2 * this.jiraWsRepository.getDataForProject(_)      >>> [IJiraDataForTests.JIRA_DATA_EMAIL_NULL, IJiraDataForTests.JIRA_DATA_NO_ISSUES]
        1 * this.jiraHistoryEsRepository.findOne(_)         >> null
        1 * this.jiraEsRepository.findOne(_)                >> null
    }

}
