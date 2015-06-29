package com.nike.mm.business.internal

import com.nike.mm.business.internal.impl.MeasureMentorJobsConfigBusiness
import com.nike.mm.dto.MeasureMentorJobsConfigDto
import com.nike.mm.entity.MeasureMentorJobsConfig
import com.nike.mm.repository.es.internal.IMeasureMentorJobsConfigRepository
import org.jasypt.util.text.BasicTextEncryptor
import org.jasypt.util.text.TextEncryptor
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import spock.lang.Specification

/**
 * Created by rparr2 on 6/11/15.
 */
class MeasureMentorJobsConfigBusinessUnitSpec extends Specification {

    IMeasureMentorJobsConfigBusiness measureMentorJobsConfigBusiness

    IMeasureMentorJobsConfigRepository measureMentorConfigRepository

    TextEncryptor textEncryptor

    def setup() {
        this.measureMentorJobsConfigBusiness                                = new MeasureMentorJobsConfigBusiness()
        this.measureMentorConfigRepository                                  = Mock(IMeasureMentorJobsConfigRepository)
        this.textEncryptor                                            = new BasicTextEncryptor()
        this.textEncryptor.setPassword("password")
        this.measureMentorJobsConfigBusiness.measureMentorConfigRepository  = this.measureMentorConfigRepository
        this.measureMentorJobsConfigBusiness.textEncryptor            = this.textEncryptor
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

    def "find one and decrypt the configuration" () {

        setup:
        def dto = [id: "id", name: "name", jobOn: true, encryptedConfig: Base64.getEncoder().encodeToString(this.textEncryptor.encrypt('[{"id":"id"}]').getBytes()), cron:"cron"] as MeasureMentorJobsConfig

        when:
        def rdto = this.measureMentorJobsConfigBusiness.findById("id")

        then:
        1 * this.measureMentorConfigRepository.findOne(_) >> dto
        rdto                != null
        rdto.config.size()  == 1
        rdto.config[0].id   == "id"
    }

    def "save the config object without the id set" () {

        setup:
        def dto = [name:"name", jobOn: true, config: [[id:"id"]], cron: "* * * *"] as MeasureMentorJobsConfigDto

        when:
        def rdto = this.measureMentorJobsConfigBusiness.saveConfig(dto)

        then:
        1 * this.measureMentorConfigRepository.save(_)
    }

    def "save the config object with the id set" () {

        setup:
        def dto = [id: "id", name:"name", jobOn: true, config: [[id:"id"]], cron: "* * * *"] as MeasureMentorJobsConfigDto

        when:
        def rdto = this.measureMentorJobsConfigBusiness.saveConfig(dto)

        then:
        1 * this.measureMentorConfigRepository.save(_)
    }
}
