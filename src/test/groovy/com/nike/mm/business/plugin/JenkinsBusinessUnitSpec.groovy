package com.nike.mm.business.plugin

import com.nike.mm.business.plugins.IJenkinsBusiness
import com.nike.mm.business.plugins.impl.JenkinsBusiness
import spock.lang.Specification

/**
 * Created by rparr2 on 6/12/15.
 */
class JenkinsBusinessUnitSpec extends Specification {

    IJenkinsBusiness jenkinsBusiness

    def setup() {
        this.jenkinsBusiness = new JenkinsBusiness()
    }

    def "make sure you have the jenkins type set"() {

        when:
        def type = this.jenkinsBusiness.type()

        then:
        type == "Jenkins"
    }

    def "confirm valid config"() {

        when:
        boolean validConfig = this.jenkinsBusiness.validateConfig([url:"http://google.com"])

        then:
        validConfig
    }

    def "invalid config"() {

        when:
        boolean validConfig = this.jenkinsBusiness.validateConfig([])

        then:
        !validConfig
    }

    def "update data is disabled"() {

        when:
        this.jenkinsBusiness.updateData(null, null);

        then:
        thrown(RuntimeException)
    }
}
