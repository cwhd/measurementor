package com.nike.mm.business.internal

import com.google.common.collect.Lists
import com.nike.mm.business.internal.impl.JobHistoryBusiness
import com.nike.mm.dto.JobRunResponseDto
import com.nike.mm.entity.internal.JobHistory
import com.nike.mm.repository.es.internal.IJobConfigHistoryRepository
import com.nike.mm.repository.es.internal.IJobHistoryRepository
import org.springframework.data.domain.PageImpl
import spock.lang.Specification

class JobHistoryBusinessUnitSpec extends Specification {

    IJobHistoryBusiness jobHistoryBusiness

    IJobHistoryRepository jobHistoryRepository

    IJobConfigHistoryRepository jobConfigHistoryRepository

    def setup() {
        this.jobHistoryBusiness                             = new JobHistoryBusiness()
        this.jobHistoryRepository                           = Mock(IJobHistoryRepository)
        this.jobConfigHistoryRepository                     = Mock(IJobConfigHistoryRepository)
        this.jobHistoryBusiness.jobHistoryRepository        = this.jobHistoryRepository
        this.jobHistoryBusiness.jobConfigHistoryRepository  = this.jobConfigHistoryRepository
    }

    def "save the job history record" () {

        setup:
        def jh = [id: "id"] as JobHistory

        when:
        def rjh = this.jobHistoryBusiness.save(jh)

        then:
        1 * this.jobHistoryRepository.save(_) >> jh
        rjh != null
    }

    def "find the last successful job for jobid" () {

        setup:
        def dtos = [[id: "id"] as JobHistory]

        when:
        def rjh = this.jobHistoryBusiness.findLastSuccessfulJobRanForJobid("id");

        then:
        1 * this.jobHistoryRepository.findByJobidAndStatus(_, _ , _) >> new PageImpl(dtos, null, 1)
        rjh != null
    }

    def "cant find the last successful job for jobid return null" () {

        when:
        def rjh = this.jobHistoryBusiness.findLastSuccessfulJobRanForJobid("id");

        then:
        1 * this.jobHistoryRepository.findByJobidAndStatus(_, _ , _) >> new PageImpl([], null, 1)
        rjh == null
    }

    def "find the last build status for a jobid" () {

        setup:
        def dtos = [[id: "id"] as JobHistory]

        when:
        def rjh = this.jobHistoryBusiness.findJobsLastBuildStatus("id");

        then:
        1 * this.jobHistoryRepository.findByJobid(_,  _) >> new PageImpl(dtos, null, 1)
        rjh != null
    }

    def "cant find the last build status for a jobid returns null" () {

        when:
        def rjh = this.jobHistoryBusiness.findJobsLastBuildStatus("id");

        then:
        1 * this.jobHistoryRepository.findByJobid(_,  _) >> new PageImpl([], null, 1)
        rjh == null
    }

    def "find by job id and page the results" () {

        setup:
        def dtos = [[id: "id"] as JobHistory]

        when:
        def page = this.jobHistoryBusiness.findByJobIdAndPage(null, null)

        then:
        1 * this.jobHistoryRepository.findByJobid(_, _) >> new PageImpl(dtos, null, 1)
        page.content.size() == 1
    }

    def "cant find by job id and page the results returns empty page" () {

        when:
        def page = this.jobHistoryBusiness.findByJobIdAndPage(null, null)

        then:
        1 * this.jobHistoryRepository.findByJobid(_, _) >> new PageImpl([], null, 1)
        page.content.isEmpty()
    }

    def "Save job run results without plugin results" () {

        when:
        this.jobHistoryBusiness.saveJobRunResults("jobid", new Date(), new Date(), Collections.emptyList())

        then:
        0 * this.jobConfigHistoryRepository.save(_)
        1 * this.jobHistoryRepository.save(_)
    }

    def "Save job run results with plugin results" () {

        setup:
        def fakeJobRunResult = new JobRunResponseDto()
        def pluginResults = Lists.newArrayList(fakeJobRunResult, fakeJobRunResult)
        def jh = [id: "jobHistoryId"] as JobHistory

        when:
        this.jobHistoryBusiness.saveJobRunResults("jobid", new Date(), new Date(), pluginResults)

        then:
        1 * this.jobHistoryRepository.save(_)                  >> jh
        2 * this.jobConfigHistoryRepository.save(_)
    }

}
