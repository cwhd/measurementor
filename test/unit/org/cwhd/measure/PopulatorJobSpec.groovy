package org.cwhd.measure

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(PopulatorJob)
class PopulatorJobSpec extends Specification {
    def populatorJob

    def setup() {
        populatorJob = new PopulatorJob()
    }

    def cleanup() {
    }

//    void "subtrack dates"() {
//        def duration = populatorJob.checkTimeDifference(new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss").parse("1976-08-07:7:30:15"), new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss").parse("1976-08-07:7:15:15"))
//        expect: duration.minutes == 15
//    }

}
