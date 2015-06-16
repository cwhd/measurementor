package com.nike.mm.facade.impl

import org.dozer.Mapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

import com.nike.mm.business.internal.IJobHistoryBusiness
import com.nike.mm.dto.JobHistoryDto
import com.nike.mm.entity.JobHistory
import com.nike.mm.facade.IJobHistoryFacade

@Service
class JobHistoryFacade implements IJobHistoryFacade {
	
	@Autowired IJobHistoryBusiness jobHistoryBusiness
	
	@Override
	Page<JobHistoryDto> findAllByJobid(String jobid, Pageable pageable) {
		Page rpage = this.jobHistoryBusiness.findByJobIdAndPage(jobid, pageable)
		List<JobHistoryDto> dtos = [];
		for (JobHistory jh: rpage) {
			dtos.add([id:jh.id, jobid:jh.jobid, startDate: jh.startDate, endDate: jh.endDate, success:jh.success, status:jh.status, comments: jh.comments] as  JobHistoryDto)
		}
		return new PageImpl(dtos, pageable, rpage.getTotalElements())
	}
}