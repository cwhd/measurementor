package com.nike.mm.rest

import com.nike.mm.facade.IMeasureMentorRunFacade
import com.nike.mm.rest.impl.MeasureMentorRunRest
import spock.lang.Specification

/**
 * Created by rparr2 on 6/13/15.
 */
class MeasureMentorRunRestUnitSpec extends Specification {

    IMeasureMentorRunRest measureMentorRunRest

    IMeasureMentorRunFacade measureMentorRunFacade

    def setup() {
        this.measureMentorRunRest                           = new MeasureMentorRunRest()
        this.measureMentorRunFacade                         = Mock(IMeasureMentorRunFacade)
        this.measureMentorRunRest.measureMentorRunFacade    = this.measureMentorRunFacade
    }

    def "simple rest call" () {

        when:
        this.measureMentorRunRest.runJobId("jobid")

        then:
        1 * this.measureMentorRunFacade.runJobId(_)
    }
}
