package com.nike.mm.facade

import com.nike.mm.business.internal.IJobHistoryBusiness
import com.nike.mm.business.internal.IMeasureMentorJobsConfigBusiness
import com.nike.mm.business.internal.IMeasureMentorRunBusiness
import com.nike.mm.dto.MeasureMentorJobsConfigDto
import com.nike.mm.entity.JobHistory
import com.nike.mm.entity.MeasureMentorJobsConfig
import com.nike.mm.facade.impl.MeasureMentorJobsConfigFacade
import com.nike.mm.service.ICronService
import org.jasypt.util.text.StrongTextEncryptor
import org.springframework.data.domain.PageImpl
import spock.lang.Specification

/**
 * Created by rparr2 on 6/11/15.
 */
class MeasureMentorJobsConfigFacadeUnitSpec extends Specification {

    IMeasureMentorJobsConfigFacade measureMentorJobsConfigFacade

    IMeasureMentorJobsConfigBusiness measureMentorJobsConfigBusiness

    IJobHistoryBusiness jobHistoryBusiness

    IMeasureMentorRunBusiness measureMentorRunBusiness

    ICronService cronService

    StrongTextEncryptor strongTextEncryptor

    def setup() {
        this.measureMentorJobsConfigFacade                                  = new MeasureMentorJobsConfigFacade()
        this.measureMentorJobsConfigBusiness                                = Mock(IMeasureMentorJobsConfigBusiness)
        this.jobHistoryBusiness                                             = Mock(IJobHistoryBusiness)
        this.measureMentorRunBusiness                                       = Mock(IMeasureMentorRunBusiness)
        this.cronService                                                    = Mock(ICronService)
        this.strongTextEncryptor                                            = new StrongTextEncryptor()
        this.measureMentorJobsConfigFacade.measureMentorJobsConfigBusiness  = this.measureMentorJobsConfigBusiness
        this.measureMentorJobsConfigFacade.jobHistoryBusiness               = this.jobHistoryBusiness
        this.measureMentorJobsConfigFacade.measureMentorRunBusiness         = this.measureMentorRunBusiness
        this.measureMentorJobsConfigFacade.cronService                      = this.cronService
        this.measureMentorJobsConfigFacade.strongTextEncryptor              = this.strongTextEncryptor
    }

    def "find an individual record"() {

        setup:
        def dto = [id: "notRealId"] as MeasureMentorJobsConfigDto

        when:
        def rdto = this.measureMentorJobsConfigFacade.findById("notRealId")

        then:
        1 * this.measureMentorJobsConfigBusiness.findById(_) >> dto
        rdto.id == dto.id
    }

    def "ensure that the save config calls the business"() {

        setup:
        def dto     = [id:"not a real id"] as MeasureMentorJobsConfigDto
        def entity  = [id:dto.id] as MeasureMentorJobsConfig

        when:
        def rentity = this.measureMentorJobsConfigFacade.saveJobsConfig(dto);

        then:
        1 * this.measureMentorJobsConfigBusiness.saveConfig(_) >> entity
        1 * this.cronService.processJob("not a real id")
        rentity.id == dto.id
    }

    def "ensure that the list is converted to the appropriate dto with null job history"() {

        setup:
        def dtos = [[id: "testId", name:"name", jobOn:true, cron:"* * * * *", encryptedConfig: null] as MeasureMentorJobsConfig]

        when:
        def rlist = this.measureMentorJobsConfigFacade.findListOfJobs(null)

        then:
        1 * this.measureMentorJobsConfigBusiness.findAll(_) >> new PageImpl(dtos, null, 1)
        1 * this.jobHistoryBusiness.findJobsLastBuildStatus(_) >> []
        rlist.size()        == 1
        rlist[0].id         == "testId"
        rlist[0].name       == "name"
        rlist[0].jobOn      == true
        rlist[0].cron       == "* * * * *"
    }

    def "ensure that the list is converted and job history exists" () {

        setup:
        def dtos = [[id: "testId", name:"name", jobOn:true, cron:"* * * * *", encryptedConfig: null] as MeasureMentorJobsConfig]
        def jh = [id: "id", jobid:"jid", startDate: new Date(), endDate: new Date(), status: JobHistory.Status.success, comments:"listing of comments"] as JobHistory

        when:
        def rlist = this.measureMentorJobsConfigFacade.findListOfJobs(null)

        then:
        1 * this.measureMentorJobsConfigBusiness.findAll(_)         >> new PageImpl(dtos, null, 1)
        1 * this.jobHistoryBusiness.findJobsLastBuildStatus(_)      >> jh
        rlist.size()                == 1
        rlist[0].id                 == "testId"
        rlist[0].name               == "name"
        rlist[0].jobOn              == true
        rlist[0].cron               == "* * * * *"
        rlist[0].lastbuildstatus    == true
        rlist[0].lastBuildDate      != null
    }
}
