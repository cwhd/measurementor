package com.nike.mm.rest.constraint

import com.nike.mm.facade.IMeasureMentorJobsConfigFacade
import com.nike.mm.rest.validation.constraint.JobIdValidator
import spock.lang.Specification


class JobIdValidatorUnitSpec extends Specification {

    JobIdValidator jobIdValidator

    IMeasureMentorJobsConfigFacade measureMentorJobsConfigFacade

    def setup() {
        measureMentorJobsConfigFacade                   = Mock(IMeasureMentorJobsConfigFacade)
        jobIdValidator                                  = new JobIdValidator()
        jobIdValidator.measureMentorJobsConfigFacade    = measureMentorJobsConfigFacade
    }

    def "Null Job Id is valid" () {

        when:
        def result = jobIdValidator.isValid(null, null)

        then:
        0 * measureMentorJobsConfigFacade.findById(_)
        result
    }

    def "Empty String Job Id is valid" () {

        when:
        def result = jobIdValidator.isValid("", null)

        then:
        0 * measureMentorJobsConfigFacade.findById("")  >> []
        result
    }

    def "Job Id that does not exist in the system fails" () {

        setup:
        def jobId = "invalid"

        when:
        def result = jobIdValidator.isValid(jobId, null)

        then:
        1 * measureMentorJobsConfigFacade.findById(jobId)  >> null
        !result
    }

    def "Job Id that does exist in the system passes" () {

        setup:
        def jobId = "valid"

        when:
        def result = jobIdValidator.isValid(jobId, null)

        then:
        1 * measureMentorJobsConfigFacade.findById(jobId)  >> []
        result
    }
}
