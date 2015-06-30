package com.nike.mm.business.plugin

import com.nike.mm.business.plugin.data.IJenkinsDataForTests
import com.nike.mm.business.plugins.IJenkinsBusiness
import com.nike.mm.business.plugins.impl.JenkinsBusiness
import com.nike.mm.repository.es.plugins.IJenkinsEsRepository
import com.nike.mm.repository.ws.IJenkinsWsRepository
import com.nike.mm.service.impl.UtilitiesService
import org.apache.commons.lang3.StringUtils
import spock.lang.Specification

class JenkinsBusinessUnitSpec extends Specification {

    IJenkinsBusiness jenkinsBusiness

    IJenkinsWsRepository jenkinsWsRepository

    IJenkinsEsRepository jenkinsEsRepository

    def setup() {
        this.jenkinsBusiness                        = new JenkinsBusiness()
        this.jenkinsWsRepository                    = Mock(IJenkinsWsRepository)
        this.jenkinsEsRepository                    = Mock(IJenkinsEsRepository)
        this.jenkinsBusiness.jenkinsWsRepository    = this.jenkinsWsRepository
        this.jenkinsBusiness.utilitiesService       = new UtilitiesService()
        this.jenkinsBusiness.jenkinsEsRepository    = this.jenkinsEsRepository

    }

    def "make sure you have the jenkins type set"() {

        when:
        def type = this.jenkinsBusiness.type()

        then:
        type == "Jenkins"
    }

    def "confirm valid config"() {

        when:
        String errorMessage = this.jenkinsBusiness.validateConfig([url:"http://google.com"])

        then:
        StringUtils.isEmpty(errorMessage)
    }

    def "invalid config"() {

        when:
        String errorMessage = this.jenkinsBusiness.validateConfig([])

        then:
        !StringUtils.isEmpty(errorMessage)
    }

    def "update data is disabled"() {

        setup:
        def fromDate = Date.parse( 'dd-MM-yyyy', "01-01-2001" )

        when:
        this.jenkinsBusiness.updateDataWithResponse(fromDate, null);

        then:
        thrown(RuntimeException)
    }

    def "validate the jenkins create data method" () {

        setup:
        def config = [url:"http://made.up", credentials:"credentials"]
        def fromDate = Date.parse( 'dd-MM-yyyy', "01-01-2001" )

        when:
        this.jenkinsBusiness.updateDataWithResponse(fromDate,config)

        then:
        1 * this.jenkinsWsRepository.findListOfJobs(_)              >> IJenkinsDataForTests.API_JSON
        1 * this.jenkinsWsRepository.findListOfJobsJobs(_)          >> IJenkinsDataForTests.JOBS_JOBS_API
        1 * this.jenkinsWsRepository.findListOfBuilds(_)            >> IJenkinsDataForTests.JOBS_JOBS_JOBS_API
        1 * this.jenkinsWsRepository.findBuildInformation(_)        >> IJenkinsDataForTests.BUILDS_API
        1 * this.jenkinsWsRepository.findFinalBuildInformation(_)   >> IJenkinsDataForTests.BUILD_API
        1 * this.jenkinsEsRepository.findOne(_)                     >> null
        1 * this.jenkinsEsRepository.save(_)
    }

    def "validate the jenkins update data method" () {

        setup:
        def config = [url:"http://made.up", credentials:"credentials"]
        def fromDate = Date.parse( 'dd-MM-yyyy', "01-01-2001" )

        when:
        this.jenkinsBusiness.updateDataWithResponse(fromDate, config)

        then:
        1 * this.jenkinsWsRepository.findListOfJobs(_)              >> IJenkinsDataForTests.API_JSON
        1 * this.jenkinsWsRepository.findListOfJobsJobs(_)          >> IJenkinsDataForTests.JOBS_JOBS_API
        1 * this.jenkinsWsRepository.findListOfBuilds(_)            >> IJenkinsDataForTests.JOBS_JOBS_JOBS_API
        1 * this.jenkinsWsRepository.findBuildInformation(_)        >> IJenkinsDataForTests.BUILDS_API
        1 * this.jenkinsWsRepository.findFinalBuildInformation(_)   >> IJenkinsDataForTests.BUILD_API
        1 * this.jenkinsEsRepository.findOne(_)                     >> []
        1 * this.jenkinsEsRepository.save(_)
    }
}
