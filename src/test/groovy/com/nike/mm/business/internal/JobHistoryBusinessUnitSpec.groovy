package com.nike.mm.business.internal

import com.nike.mm.business.internal.impl.JobHistoryBusiness
import com.nike.mm.entity.JobHistory
import com.nike.mm.repository.es.internal.IJobHistoryRepository
import org.springframework.data.domain.PageImpl
import spock.lang.Specification

class JobHistoryBusinessUnitSpec extends Specification {

    IJobHistoryBusiness jobHistoryBusiness

    IJobHistoryRepository jobHistoryRepository

    def setup() {
        this.jobHistoryBusiness                         = new JobHistoryBusiness()
        this.jobHistoryRepository                       = Mock(IJobHistoryRepository)
        this.jobHistoryBusiness.jobHistoryRepository    = this.jobHistoryRepository
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

}
