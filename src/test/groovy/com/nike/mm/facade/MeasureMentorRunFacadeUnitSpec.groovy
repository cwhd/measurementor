package com.nike.mm.facade

import com.nike.mm.business.internal.IJobHistoryBusiness
import com.nike.mm.business.internal.IMeasureMentorJobsConfigBusiness
import com.nike.mm.business.internal.IMeasureMentorRunBusiness
import com.nike.mm.business.plugins.IMeasureMentorBusiness
import com.nike.mm.dto.MeasureMentorJobsConfigDto
import com.nike.mm.entity.JobHistory
import com.nike.mm.facade.impl.MeasureMentorRunFacade
import spock.lang.Specification

/**
 * Created by rparr2 on 6/13/15.
 */
class MeasureMentorRunFacadeUnitSpec  extends Specification {

    MeasureMentorRunFacade measureMentorRunFacade

    IMeasureMentorRunBusiness measureMentorRunBusiness

    IJobHistoryBusiness jobHistoryBusiness

    IMeasureMentorJobsConfigBusiness measureMentorConfigBusiness

    def setup() {
        this.measureMentorRunFacade                             = new MeasureMentorRunFacade()
        this.measureMentorRunBusiness                           = Mock(IMeasureMentorRunBusiness)
        this.jobHistoryBusiness                                 = Mock(IJobHistoryBusiness)
        this.measureMentorConfigBusiness                        = Mock(IMeasureMentorJobsConfigBusiness)
        this.measureMentorRunFacade.measureMentorRunBusiness    = this.measureMentorRunBusiness
        this.measureMentorRunFacade.jobHistoryBusiness          = this.jobHistoryBusiness
        this.measureMentorRunFacade.measureMentorConfigBusiness = this.measureMentorConfigBusiness
    }

    def "run job id throw exception because it is already running" () {

        when:
        this.measureMentorRunFacade.runJobId("anyid")

        then:
        1 * this.measureMentorRunBusiness.isJobRunning(_)               >> true
        1 * this.jobHistoryBusiness.save(_)
        thrown(RuntimeException)
    }

    def "run the job no previous job history and has no config so it should go right through" ()  {

        setup:
        def configDto                                                   = [id: 'testId', name: 'SomeBuild', config: []] as MeasureMentorJobsConfigDto
        this.measureMentorRunFacade.measureMentorBusinesses             = [];

        when:
        this.measureMentorRunFacade.runJobId("anyid")

        then:
        1 * this.measureMentorRunBusiness.isJobRunning(_)               >> false
        1 * this.measureMentorRunBusiness.startJob(_)
        1 * this.measureMentorConfigBusiness.findById(_)                >> configDto
        1 * this.jobHistoryBusiness.findLastSuccessfulJobRanForJobid(_) >> null
        1 * this.jobHistoryBusiness.save(_)
        1 * this.measureMentorRunBusiness.stopJob(_)
    }

    def "run the job has a previous job history and has no config so it should go right through" ()  {

        setup:
        def configDto                                                   = [id: 'testId', name: 'SomeBuild', config: []] as MeasureMentorJobsConfigDto
        def previousJobHistory                                          = [id: "previousJobHistoryId", endDate: new Date()] as JobHistory
        this.measureMentorRunFacade.measureMentorBusinesses             = [];

        when:
        this.measureMentorRunFacade.runJobId("anyid")

        then:
        1 * this.measureMentorRunBusiness.isJobRunning(_)               >> false
        1 * this.measureMentorRunBusiness.startJob(_)
        1 * this.measureMentorConfigBusiness.findById(_)                >> configDto
        1 * this.jobHistoryBusiness.findLastSuccessfulJobRanForJobid(_) >> previousJobHistory
        1 * this.jobHistoryBusiness.save(_)
        1 * this.measureMentorRunBusiness.stopJob(_)
    }

    def "run the job no previous job history and cannot find a confugued business function." ()  {

        setup:
        def configDto                                                   = [id: 'testId', name: 'SomeBuild', config: [[type:'invalid']]] as MeasureMentorJobsConfigDto
        this.measureMentorRunFacade.measureMentorBusinesses             = [];

        when:
        this.measureMentorRunFacade.runJobId("anyid")

        then:
        1 * this.measureMentorRunBusiness.isJobRunning(_)               >> false
        1 * this.measureMentorRunBusiness.startJob(_)
        1 * this.measureMentorConfigBusiness.findById(_)                >> configDto
        1 * this.jobHistoryBusiness.findLastSuccessfulJobRanForJobid(_) >> null
        1 * this.jobHistoryBusiness.save(_)
        1 * this.measureMentorRunBusiness.stopJob(_)
    }

    def "run the job and find a plugin but fail the validation" () {

        setup:
        IMeasureMentorBusiness measureMentorBusiness                    = Mock(IMeasureMentorBusiness)
        this.measureMentorRunFacade.measureMentorBusinesses             = [measureMentorBusiness];
        def configDto                                                   = [id: 'testId', name: 'SomeBuild', config: [[type:'availableButFailConfig']]] as MeasureMentorJobsConfigDto

        when:
        this.measureMentorRunFacade.runJobId("anyid")

        then:
        1 * this.measureMentorRunBusiness.isJobRunning(_)               >> false
        1 * this.measureMentorRunBusiness.startJob(_)
        1 * this.measureMentorConfigBusiness.findById(_)                >> configDto
        1 * this.jobHistoryBusiness.findLastSuccessfulJobRanForJobid(_) >> null
        1 * measureMentorBusiness.type() >> 'availableButFailConfig'
        1 * measureMentorBusiness.validateConfig(_)                     >> false
        0 * measureMentorBusiness.updateData(_, _)
        1 * this.jobHistoryBusiness.save(_)
        1 * this.measureMentorRunBusiness.stopJob(_)
    }

    def "run the job and find a plugin pass validation and update data" () {

        setup:
        IMeasureMentorBusiness measureMentorBusiness                    = Mock(IMeasureMentorBusiness)
        IMeasureMentorBusiness measureMentorBusinessSkip                = Mock(IMeasureMentorBusiness)
        this.measureMentorRunFacade.measureMentorBusinesses             = [measureMentorBusinessSkip, measureMentorBusiness];
        def configDto                                                   = [id: 'testId', name: 'SomeBuild', config: [[type:'availableButFailConfig']]] as MeasureMentorJobsConfigDto

        when:
        this.measureMentorRunFacade.runJobId("anyid")

        then:
        1 * this.measureMentorRunBusiness.isJobRunning(_)               >> false
        1 * this.measureMentorRunBusiness.startJob(_)
        1 * this.measureMentorConfigBusiness.findById(_)                >> configDto
        1 * this.jobHistoryBusiness.findLastSuccessfulJobRanForJobid(_) >> null
        1 * measureMentorBusinessSkip.type()                            >> 'skip'
        0 * measureMentorBusinessSkip.validateConfig()
        0 * measureMentorBusinessSkip.updateData(_, _)
        1 * measureMentorBusiness.type()                                >> 'availableButFailConfig'
        1 * measureMentorBusiness.validateConfig(_)                     >> true
        1 * measureMentorBusiness.updateData(_, _)
        1 * this.jobHistoryBusiness.save(_)
        1 * this.measureMentorRunBusiness.stopJob(_)
    }
}
