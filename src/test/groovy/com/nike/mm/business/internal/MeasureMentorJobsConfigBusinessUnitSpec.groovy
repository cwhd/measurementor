package com.nike.mm.business.internal

import com.nike.mm.business.internal.impl.MeasureMentorJobsConfigBusiness
import com.nike.mm.entity.internal.MeasureMentorJobsConfig
import com.nike.mm.repository.es.internal.IMeasureMentorJobsConfigRepository
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import spock.lang.Specification

class MeasureMentorJobsConfigBusinessUnitSpec extends Specification {

    IMeasureMentorJobsConfigBusiness measureMentorJobsConfigBusiness

    IMeasureMentorJobsConfigRepository measureMentorConfigRepository


    def setup() {
        this.measureMentorJobsConfigBusiness                                = new MeasureMentorJobsConfigBusiness()
        this.measureMentorConfigRepository                                  = Mock(IMeasureMentorJobsConfigRepository)
        this.measureMentorJobsConfigBusiness.measureMentorConfigRepository  = this.measureMentorConfigRepository
    }

    def "find all and page" () {

        setup:
        def dtos = [[id: "id", name: "name", jobOn: true, encryptedConfig: "encrypted", cron: "cron"] as MeasureMentorJobsConfig]

        when:
        def page = this.measureMentorJobsConfigBusiness.findAll(new PageRequest(0, 1, new Sort(Sort.Direction.DESC, "endDate")))

        then:
        1 * this.measureMentorConfigRepository.findAll(_) >> new PageImpl<MeasureMentorJobsConfig>(dtos, null, 1)
        page.content.size() == 1
    }

    def "find one" () {

        setup:
        def expected = [id: "id"] as MeasureMentorJobsConfig

        when:
        def actual = this.measureMentorJobsConfigBusiness.findById("id")

        then:
        1 * this.measureMentorConfigRepository.findOne(_) >> expected
        expected                                          == actual
        expected.id                                       == actual.id
    }

    def "save the config object" () {

        setup:
        def entity = [id: "id", name:"name", jobOn: true, config: [[id:"id"]], cron: "* * * *"] as MeasureMentorJobsConfig
        def expected = [] as MeasureMentorJobsConfig

        when:
        def actual = this.measureMentorJobsConfigBusiness.saveConfig(entity)

        then:
        1 * this.measureMentorConfigRepository.save(entity) >> expected
        actual                                              == expected
    }
}
