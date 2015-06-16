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

    def "job is not running" () {

        when:
        boolean jobRunning = this.measureMentorRunBusiness.isJobRunning("id")

        then:
        !jobRunning
    }

    def "job is running"() {

        setup:
        this.measureMentorRunBusiness.startJob("id")

        when:
        boolean jobRunning = this.measureMentorRunBusiness.isJobRunning("id")

        then:
        jobRunning
    }

    def "turn the job off"() {

        setup:
        this.measureMentorRunBusiness.startJob("id")

        when:
        boolean jobRunning = this.measureMentorRunBusiness.isJobRunning("id")

        then:
        jobRunning

        when:
        this.measureMentorRunBusiness.stopJob("id")
        jobRunning = this.measureMentorRunBusiness.isJobRunning("id")

        then:
        !jobRunning
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
