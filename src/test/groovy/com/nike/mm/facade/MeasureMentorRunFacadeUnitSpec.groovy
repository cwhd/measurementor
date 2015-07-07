package com.nike.mm.facade

import com.nike.mm.business.internal.IJobHistoryBusiness
import com.nike.mm.business.internal.IMeasureMentorJobsConfigBusiness
import com.nike.mm.business.internal.IMeasureMentorRunBusiness
import com.nike.mm.business.plugins.IMeasureMentorBusiness
import com.nike.mm.entity.internal.JobHistory
import com.nike.mm.entity.internal.MeasureMentorJobsConfig
import com.nike.mm.facade.impl.MeasureMentorRunFacade
import com.nike.mm.service.IDateService
import org.jasypt.util.text.BasicTextEncryptor
import org.jasypt.util.text.TextEncryptor
import spock.lang.Specification

class MeasureMentorRunFacadeUnitSpec extends Specification {

    static String PASSWORD = "123"

    MeasureMentorRunFacade measureMentorRunFacade

    IMeasureMentorRunBusiness measureMentorRunBusiness

    IJobHistoryBusiness jobHistoryBusiness

    IMeasureMentorJobsConfigBusiness measureMentorConfigBusiness

    IDateService dateService

    TextEncryptor textEncryptor

    def setup() {
        this.measureMentorRunFacade = new MeasureMentorRunFacade()
        this.measureMentorRunBusiness = Mock(IMeasureMentorRunBusiness)
        this.jobHistoryBusiness = Mock(IJobHistoryBusiness)
        this.measureMentorConfigBusiness = Mock(IMeasureMentorJobsConfigBusiness)
        this.textEncryptor = new BasicTextEncryptor()
        this.textEncryptor.setPassword(PASSWORD)
        this.dateService = Mock(IDateService)
        this.measureMentorRunFacade.measureMentorRunBusiness = this.measureMentorRunBusiness
        this.measureMentorRunFacade.jobHistoryBusiness = this.jobHistoryBusiness
        this.measureMentorRunFacade.measureMentorConfigBusiness = this.measureMentorConfigBusiness
        this.measureMentorRunFacade.dateService = this.dateService
        this.measureMentorRunFacade.textEncryptor = this.textEncryptor
    }

    def "run the job no previous job history and has no config so it should go right through"() {

        setup:
        def entity = [id: 'testId', name: 'SomeBuild', encryptedConfig: Base64.getEncoder().encodeToString(this
                .textEncryptor.encrypt("{}").bytes)] as MeasureMentorJobsConfig
        this.measureMentorRunFacade.measureMentorBusinesses = [];
        def jobHistory = new JobHistory(id: "123")

        when:
        this.measureMentorRunFacade.runJobId("anyid")

        then:
        2 * this.dateService.getCurrentDateTime()
        1 * this.measureMentorRunBusiness.startJob(_)
        1 * this.measureMentorConfigBusiness.findById(_)                         >> entity
        1 * this.jobHistoryBusiness.createJobHistory(_,_)                        >> jobHistory
        1 * this.jobHistoryBusiness.findLastSuccessfulJobRanForJobidAndPlugin(_) >> null
        0 * this.jobHistoryBusiness.saveJobRunResults(_)
        1 * this.measureMentorRunBusiness.stopJob(_)
    }

    def "run the job which has previous job history and has no config so it should go right through"() {

        setup:
        def entity = [id: 'testId', name: 'SomeBuild', encryptedConfig: Base64.getEncoder().encodeToString(this
                .textEncryptor.encrypt("{\"type\":\"test\"}").bytes)] as MeasureMentorJobsConfig
        def lastRunDate = new Date()
        this.measureMentorRunFacade.measureMentorBusinesses = [];
        def jobHistory = new JobHistory(id: "123")

        when:
        this.measureMentorRunFacade.runJobId("anyid")

        then:
        2 * this.dateService.getCurrentDateTime()
        1 * this.measureMentorRunBusiness.startJob(_)
        1 * this.measureMentorConfigBusiness.findById(_)                         >> entity
        1 * this.jobHistoryBusiness.createJobHistory(_,_)                        >> jobHistory
        1 * this.jobHistoryBusiness.findLastSuccessfulJobRanForJobidAndPlugin(_) >> lastRunDate
        0 * this.jobHistoryBusiness.saveJobRunResults(_)
        1 * this.measureMentorRunBusiness.stopJob(_)
    }

    def "run the job without history and find a plugin"() {

        setup:
        IMeasureMentorBusiness measureMentorBusiness = Mock(IMeasureMentorBusiness)
        this.measureMentorRunFacade.measureMentorBusinesses = [measureMentorBusiness];
        def entity = [id: 'testId', name: 'SomeBuild', encryptedConfig: Base64.getEncoder().encodeToString(this
                .textEncryptor.encrypt("{\"type\":\"test\"}").bytes)] as MeasureMentorJobsConfig
        def jobHistory = new JobHistory(id: "123")

        when:
        this.measureMentorRunFacade.runJobId("anyid")

        then:
        2 * this.dateService.getCurrentDateTime()
        1 * this.measureMentorRunBusiness.startJob(_)
        1 * this.measureMentorConfigBusiness.findById(_)                         >> entity
        1 * this.jobHistoryBusiness.createJobHistory(_,_)                        >> jobHistory
        1 * this.jobHistoryBusiness.findLastSuccessfulJobRanForJobidAndPlugin(_) >> null
        1 * measureMentorBusiness.type() >> 'available'
        0 * measureMentorBusiness.validateConfig(_)                              >> false
        0 * measureMentorBusiness.updateDataWithResponse(_)
        0 * this.jobHistoryBusiness.saveJobRunResults(_)
        1 * this.measureMentorRunBusiness.stopJob(_)
    }
}
