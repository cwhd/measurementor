package com.nike.mm.rest

import com.nike.mm.dto.MeasureMentorJobsConfigDto
import com.nike.mm.facade.IMeasureMentorJobsConfigFacade
import com.nike.mm.rest.impl.MeasureMentorJobsConfigRest
import org.springframework.data.domain.PageImpl
import org.springframework.data.web.PagedResourcesAssembler
import spock.lang.Specification

class MeasureMentorJobsConfigRestUnitSpec extends Specification {

    IMeasureMentorJobsConfigRest measureMentorJobsConfigRest

    IMeasureMentorJobsConfigFacade measureMentorJobsConfigFacade

    def setup() {
        this.measureMentorJobsConfigRest                                = new MeasureMentorJobsConfigRest()
        this.measureMentorJobsConfigFacade                              = Mock(IMeasureMentorJobsConfigFacade)
        this.measureMentorJobsConfigRest.measureMentorJobsConfigFacade  = this.measureMentorJobsConfigFacade
    }

    def "find by id"() {

        when:
        this.measureMentorJobsConfigRest.findById("id")

        then:
        1 * this.measureMentorJobsConfigFacade.findById(_)
    }

    def "save the job config" () {

        when:
        this.measureMentorJobsConfigRest.save([] as MeasureMentorJobsConfigDto)

        then:
        1 * this.measureMentorJobsConfigFacade.saveJobsConfig(_)
    }

    def "page through the jobs configuration" () {

        setup:
        def pageAssembler = Mock(PagedResourcesAssembler)

        when:
        this.measureMentorJobsConfigRest.pageThroughJobConfigs(null, pageAssembler)

        then:
        1 * this.measureMentorJobsConfigFacade.findListOfJobs(_)        >> new PageImpl<MeasureMentorJobsConfigDto>([], null, 1)
        1 * pageAssembler.toResource(_, _)                              >> null
        this.measureMentorJobsConfigRest.measureMentorJobsConfigFacade  != null
    }
}
