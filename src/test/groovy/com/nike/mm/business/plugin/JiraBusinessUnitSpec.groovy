package com.nike.mm.business.plugin

import com.nike.mm.business.plugin.data.IJiraDataForTests
import com.nike.mm.business.plugins.IJiraBusiness
import com.nike.mm.business.plugins.impl.JiraBusiness
import com.nike.mm.repository.es.plugins.IJiraEsRepository
import com.nike.mm.repository.es.plugins.IJiraHistoryEsRepository
import com.nike.mm.repository.ws.IJiraWsRepository
import com.nike.mm.service.IUtilitiesService
import com.nike.mm.service.impl.UtilitiesService
import spock.lang.Specification

/**
 * Created by rparr2 on 6/12/15.
 */
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

        when:
        boolean validConfig = this.jiraBusiness.validateConfig([url:"http://google.com"])

        then:
        validConfig
    }

    def "invalid config"() {

        when:
        boolean validConfig = this.jiraBusiness.validateConfig([])

        then:
        !validConfig
    }

    def "update data and general workflow"() {

        setup:
        def config = [url:"http://made.up", credentials:"credentials"]

        when:
        this.jiraBusiness.updateData(config);

        then:
        1 * this.jiraWsRepository.getProjectsList(_)        >> ['PROJECTA']
        2 * this.jiraWsRepository.getDataForProject(_)      >>> [IJiraDataForTests.JIRA_DATA, IJiraDataForTests.JIRA_DATA_NO_ISSUES]
        1 * this.jiraHistoryEsRepository.findOne(_)         >> null
        1 * this.jiraEsRepository.findOne(_)                >> null
    }

    def "update data and general workflow with jira record that exists"() {

        setup:
        def config = [url:"http://made.up", credentials:"credentials"]

        when:
        this.jiraBusiness.updateData(config);

        then:
        1 * this.jiraWsRepository.getProjectsList(_)        >> ['PROJECTA']
        2 * this.jiraWsRepository.getDataForProject(_)      >>> [IJiraDataForTests.JIRA_DATA, IJiraDataForTests.JIRA_DATA_NO_ISSUES]
        1 * this.jiraHistoryEsRepository.findOne(_)         >> null
        1 * this.jiraEsRepository.findOne(_)                >> [:]
    }
}
