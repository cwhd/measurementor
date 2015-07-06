package com.nike.mm.facade

import com.nike.mm.business.internal.IJobHistoryBusiness
import com.nike.mm.business.internal.IMeasureMentorJobsConfigBusiness
import com.nike.mm.business.internal.IMeasureMentorRunBusiness
import com.nike.mm.dto.MeasureMentorJobsConfigDto
import com.nike.mm.entity.JobHistory
import com.nike.mm.entity.MeasureMentorJobsConfig
import com.nike.mm.facade.impl.MeasureMentorJobsConfigFacade
import com.nike.mm.service.ICronService
import groovy.json.JsonSlurper
import org.jasypt.util.text.BasicTextEncryptor
import org.jasypt.util.text.TextEncryptor
import org.springframework.data.domain.PageImpl
import spock.lang.Specification

class MeasureMentorJobsConfigFacadeUnitSpec extends Specification {

    static String PASSWORD = "123"

    IMeasureMentorJobsConfigFacade measureMentorJobsConfigFacade

    IMeasureMentorJobsConfigBusiness measureMentorJobsConfigBusiness

    IJobHistoryBusiness jobHistoryBusiness

    IMeasureMentorRunBusiness measureMentorRunBusiness

    ICronService cronService

    TextEncryptor textEncryptor

    def setup() {
        this.measureMentorJobsConfigFacade                                  = new MeasureMentorJobsConfigFacade()
        this.measureMentorJobsConfigBusiness                                = Mock(IMeasureMentorJobsConfigBusiness)
        this.jobHistoryBusiness                                             = Mock(IJobHistoryBusiness)
        this.measureMentorRunBusiness                                       = Mock(IMeasureMentorRunBusiness)
        this.cronService                                                    = Mock(ICronService)
        this.textEncryptor                                                  = new BasicTextEncryptor()
        this.textEncryptor.setPassword(PASSWORD)
        this.measureMentorJobsConfigFacade.measureMentorJobsConfigBusiness  = this.measureMentorJobsConfigBusiness
        this.measureMentorJobsConfigFacade.jobHistoryBusiness               = this.jobHistoryBusiness
        this.measureMentorJobsConfigFacade.measureMentorRunBusiness         = this.measureMentorRunBusiness
        this.measureMentorJobsConfigFacade.cronService                      = this.cronService
        this.measureMentorJobsConfigFacade.textEncryptor                    = this.textEncryptor
    }

    def "find an individual record and config is decrypted"() {

        setup:
        def config = "{\"type\":\"jenkins\"}"
        def entity = [id: "notRealId", encryptedConfig: Base64.getEncoder().encodeToString(this.textEncryptor.encrypt(config).bytes)] as MeasureMentorJobsConfig

        when:
        def actual = this.measureMentorJobsConfigFacade.findById("notRealId")

        then:
        1 * this.measureMentorJobsConfigBusiness.findById(_) >> entity
        actual                                               instanceof MeasureMentorJobsConfigDto
        actual.id                                            == entity.id
        actual.config                                        == new JsonSlurper().parseText("{\"type\":\"jenkins\"}")
    }

    def "ensure that the save config calls the business"() {

        setup:
        def dto     = [id:"not a real id"] as MeasureMentorJobsConfigDto
        def entity  = [id:dto.id, name:"name", jobOn:true, cron:"* * * * *", encryptedConfig: Base64.getEncoder().encodeToString(this.textEncryptor.encrypt("{\"type\":\"jenkins\"}").bytes)] as MeasureMentorJobsConfig

        when:
        def rentity = this.measureMentorJobsConfigFacade.saveJobsConfig(dto);

        then:
        1 * this.measureMentorJobsConfigBusiness.saveConfig(_) >> entity
        1 * this.cronService.processJob("not a real id")
        rentity.id == dto.id
    }

    def "ensure that the list is converted to the appropriate dto with null job history"() {

        setup:
        def dtos = [[id: "testId", name:"name", jobOn:true, cron:"* * * * *", encryptedConfig: Base64.getEncoder().encodeToString(this.textEncryptor.encrypt("{\"type\":\"jenkins\"}").bytes)] as MeasureMentorJobsConfig]

        when:
        def rlist = this.measureMentorJobsConfigFacade.findListOfJobs(null)

        then:
        1 * this.measureMentorJobsConfigBusiness.findAll(_)         >> new PageImpl(dtos, null, 1)
        1 * this.jobHistoryBusiness.findJobsLastBuildStatus(_)      >> []
        rlist.size()                                                == 1
        rlist[0].id                                                 == "testId"
        rlist[0].name                                               == "name"
        rlist[0].jobOn                                              == true
        rlist[0].cron                                               == "* * * * *"
        rlist[0].config                                             == new JsonSlurper().parseText("{\"type\":\"jenkins\"}")
    }

    def "ensure that the list is converted and job history exists" () {

        setup:
        def dtos = [[id: "testId", name:"name", jobOn:true, cron:"* * * * *", encryptedConfig: Base64.getEncoder().encodeToString(this.textEncryptor.encrypt("{\"type\":\"jenkins\"}").bytes)] as MeasureMentorJobsConfig]
        def jh = [id: "id", jobid:"jid", startDate: new Date(), endDate: new Date(), status: JobHistory.Status.success, comments:"listing of comments"] as JobHistory

        when:
        def rlist = this.measureMentorJobsConfigFacade.findListOfJobs(null)

        then:
        1 * this.measureMentorJobsConfigBusiness.findAll(_)         >> new PageImpl(dtos, null, 1)
        1 * this.jobHistoryBusiness.findJobsLastBuildStatus(_)      >> jh
        rlist.size()                                                == 1
        rlist[0].id                                                 == "testId"
        rlist[0].name                                               == "name"
        rlist[0].jobOn                                              == true
        rlist[0].cron                                               == "* * * * *"
        rlist[0].lastbuildstatus                                    == JobHistory.Status.success.toString()
        rlist[0].lastBuildDate                                      != null
        rlist[0].config                                             == new JsonSlurper().parseText("{\"type\":\"jenkins\"}")
    }
}
