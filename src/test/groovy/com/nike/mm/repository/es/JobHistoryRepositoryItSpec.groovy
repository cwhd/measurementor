package com.nike.mm.repository.es

import com.nike.mm.MeasurementorApplication
import com.nike.mm.entity.internal.JobHistory
import com.nike.mm.repository.es.internal.IJobHistoryRepository
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
class JobHistoryRepositoryItSpec extends Specification {

	@Autowired IJobHistoryRepository jobHistoryRepository
	
	@Test
	def "save and retrieve" () {
		
		setup:
		this.jobHistoryRepository.deleteAll()
		JobHistory jh = [jobname:"testjob", startDate: new Date(), endDate: new Date(), status:"Done"] as JobHistory 
		
		when:
		JobHistory rjh = this.jobHistoryRepository.save(jh);
		
		then:
		rjh.id != null
		
		cleanup:
		this.jobHistoryRepository.deleteAll()
	}
	
	@Test
	def "get the most recent" () {
		
		setup:
		this.jobHistoryRepository.deleteAll()
		JobHistory mostRecent = this.jobHistoryRepository.save([jobid:"test-job-id", startDate: new Date(), endDate: new Date(), status: JobHistory.Status.success] as JobHistory)
		JobHistory leastRecent  = this.jobHistoryRepository.save([jobid:"test-job-id", startDate: new Date(), endDate: new Date((new Date().getTime() - 10000)), status: JobHistory.Status.error] as JobHistory)
		
		when:
		Page<JobHistory> rpage = this.jobHistoryRepository.findByJobidAndStatus("test-job-id", JobHistory.Status.success, new PageRequest(0, 1, new Sort(Sort.Direction.ASC, "endDate")))
		
		then:
		rpage.content.size()	== 1
		rpage.getTotalPages() 	== 1
		rpage.content[0].id 	== mostRecent.id
		
		cleanup:
		this.jobHistoryRepository.deleteAll()
	}

	@Test
	def "get history" () {

		setup:
		this.jobHistoryRepository.deleteAll()
		JobHistory mostRecent = this.jobHistoryRepository.save([jobid:"test-job-id", startDate: new Date(), endDate: new Date(), status: JobHistory.Status.success] as JobHistory)
		JobHistory leastRecent  = this.jobHistoryRepository.save([jobid:"test-job-id", startDate: new Date(), endDate: new Date((new Date().getTime() - 10000)), status: JobHistory.Status.success] as JobHistory)

		when:
		Page<JobHistory> rpage = this.jobHistoryRepository.findByJobid("test-job-id", new PageRequest(0, 20, new Sort(Sort.Direction.DESC, "endDate")))

		then:
		rpage.content.size()	== 2
		rpage.getTotalPages() 	== 1
		rpage.content[0].id 	== mostRecent.id
		rpage.content[1].id 	== leastRecent.id

		cleanup:
		this.jobHistoryRepository.deleteAll()
	}
}
