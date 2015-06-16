package com.nike.mm.business.plugin

import com.nike.mm.business.plugins.IJiraBusiness
import com.nike.mm.business.plugins.impl.JiraBusiness
import spock.lang.Specification

/**
 * Created by rparr2 on 6/12/15.
 */
class JiraBusinessUnitSpec extends Specification {

    IJiraBusiness jiraBusiness

    def setup() {
        this.jiraBusiness = new JiraBusiness()
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

    def "update data is disabled"() {

        when:
        this.jiraBusiness.updateData(null, null);

        then:
        thrown(RuntimeException)
    }
}
