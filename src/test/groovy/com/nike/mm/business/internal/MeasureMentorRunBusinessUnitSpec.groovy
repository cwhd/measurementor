package com.nike.mm.business.internal

import com.nike.mm.business.internal.impl.MeasureMentorRunBusiness
import spock.lang.Specification

/**
 * Created by rparr2 on 6/12/15.
 */
class MeasureMentorRunBusinessUnitSpec extends Specification {

    IMeasureMentorRunBusiness measureMentorRunBusiness

    def setup() {
        this.measureMentorRunBusiness = new MeasureMentorRunBusiness()
    }

    def "running the same job twice throws an exception" () {

        setup:
        this.measureMentorRunBusiness.startJob("id")

        when:
        this.measureMentorRunBusiness.startJob("id")

        then:
        thrown(RuntimeException)
    }
}
