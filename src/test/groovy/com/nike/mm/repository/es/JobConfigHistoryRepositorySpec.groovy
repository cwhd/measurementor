package com.nike.mm.repository.es

import com.nike.mm.MeasurementorApplication
import com.nike.mm.entity.internal.JobConfigHistory
import com.nike.mm.entity.internal.JobHistory
import com.nike.mm.repository.es.internal.IJobConfigHistoryRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MeasurementorApplication.class)
@WebAppConfiguration
class JobConfigHistoryRepositorySpec extends Specification  {

    @Autowired IJobConfigHistoryRepository jobConfigHistoryRepository

    @Test
    def "findByJobidAndStatusAndType" () {

        setup:
        this.jobConfigHistoryRepository.deleteAll()
        JobConfigHistory successJh = [jobid: "123", type: "type", status: JobHistory.Status.success, endDate: new Date()] as JobConfigHistory
        JobConfigHistory failedJh = [jobid: "123", type: "type", status: JobHistory.Status.error, endDate: new Date()] as JobConfigHistory
        this.jobConfigHistoryRepository.save(successJh)
        this.jobConfigHistoryRepository.save(failedJh)

        when:
        Page<JobConfigHistory> actual = this.jobConfigHistoryRepository.findByJobidAndStatusAndType("123", JobHistory.Status.success, "type", new PageRequest(0, 1, new Sort(Sort.Direction.ASC, "endDate")))

        then:
        actual.content.size()            == 1
        actual.content[0].id             != null
        actual.content[0].status         == JobHistory.Status.success

        cleanup:
        this.jobConfigHistoryRepository.deleteAll()
    }
}
