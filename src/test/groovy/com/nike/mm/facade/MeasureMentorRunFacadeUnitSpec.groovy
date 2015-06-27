package com.nike.mm.facade

import com.nike.mm.service.IDateService
import com.nike.mm.business.internal.IJobHistoryBusiness
import com.nike.mm.business.internal.IMeasureMentorJobsConfigBusiness
import com.nike.mm.business.internal.IMeasureMentorRunBusiness
import com.nike.mm.business.plugins.IMeasureMentorBusiness
import com.nike.mm.dto.MeasureMentorJobsConfigDto
import com.nike.mm.entity.JobHistory
import com.nike.mm.facade.impl.MeasureMentorRunFacade
import spock.lang.Specification

class MeasureMentorRunFacadeUnitSpec  extends Specification {

    MeasureMentorRunFacade measureMentorRunFacade

    IMeasureMentorRunBusiness measureMentorRunBusiness

    IJobHistoryBusiness jobHistoryBusiness

    IMeasureMentorJobsConfigBusiness measureMentorConfigBusiness

    IDateService dateService

    def setup() {
        this.measureMentorRunFacade                             = new MeasureMentorRunFacade()
        this.measureMentorRunBusiness                           = Mock(IMeasureMentorRunBusiness)
        this.jobHistoryBusiness                                 = Mock(IJobHistoryBusiness)
        this.measureMentorConfigBusiness                        = Mock(IMeasureMentorJobsConfigBusiness)
        this.dateService = Mock(IDateService)
        this.measureMentorRunFacade.measureMentorRunBusiness    = this.measureMentorRunBusiness
        this.measureMentorRunFacade.jobHistoryBusiness          = this.jobHistoryBusiness
        this.measureMentorRunFacade.measureMentorConfigBusiness = this.measureMentorConfigBusiness
        this.measureMentorRunFacade.dateService = this.dateService
    }

    def "run the job no previous job history and has no config so it should go right through" ()  {

        setup:
        def configDto                                                   = [id: 'testId', name: 'SomeBuild', config: []] as MeasureMentorJobsConfigDto
        this.measureMentorRunFacade.measureMentorBusinesses             = [];

        when:
        this.measureMentorRunFacade.runJobId("anyid")

        then:
        2 * this.dateService.getCurrentDateTime()
        1 * this.measureMentorRunBusiness.startJob(_)
        1 * this.measureMentorConfigBusiness.findById(_)                         >> configDto
        0 * this.jobHistoryBusiness.findLastSuccessfulJobRanForJobidAndPlugin(_) >> null
        0 * this.jobHistoryBusiness.saveJobRunResults(_)
        1 * this.measureMentorRunBusiness.stopJob(_)
    }

    def "run the job which has previous job history and has no config so it should go right through" ()  {

        setup:
        def configDto                                                   = [id: 'testId', name: 'SomeBuild', config: []] as MeasureMentorJobsConfigDto
        def previousJobHistory                                          = [id: "previousJobHistoryId", endDate: new Date()] as JobHistory
        this.measureMentorRunFacade.measureMentorBusinesses             = [];

        when:
        this.measureMentorRunFacade.runJobId("anyid")

        then:
        2 * this.dateService.getCurrentDateTime()
        1 * this.measureMentorRunBusiness.startJob(_)
        1 * this.measureMentorConfigBusiness.findById(_)                         >> configDto
        0 * this.jobHistoryBusiness.findLastSuccessfulJobRanForJobidAndPlugin(_) >> previousJobHistory
        0 * this.jobHistoryBusiness.saveJobRunResults(_)
        1 * this.measureMentorRunBusiness.stopJob(_)
    }

    def "run the job without history and find a plugin" () {

        setup:
        IMeasureMentorBusiness measureMentorBusiness                    = Mock(IMeasureMentorBusiness)
        this.measureMentorRunFacade.measureMentorBusinesses             = [measureMentorBusiness];
        def configDto                                                   = [id: 'testId', name: 'SomeBuild', config: [[type:'available']]] as MeasureMentorJobsConfigDto

        when:
        this.measureMentorRunFacade.runJobId("anyid")

        then:
        2 * this.dateService.getCurrentDateTime()
        1 * this.measureMentorRunBusiness.startJob(_)
        1 * this.measureMentorConfigBusiness.findById(_)                         >> configDto
        1 * this.jobHistoryBusiness.findLastSuccessfulJobRanForJobidAndPlugin(_) >> null
        1 * measureMentorBusiness.type()                                         >> 'available'
        1 * measureMentorBusiness.validateConfig(_)                              >> false
        0 * measureMentorBusiness.updateData(_)
        0 * this.jobHistoryBusiness.saveJobRunResults(_)
        1 * this.measureMentorRunBusiness.stopJob(_)
    }
}
