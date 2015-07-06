package com.nike.mm.facade.impl

import com.nike.mm.business.internal.IJobHistoryBusiness
import com.nike.mm.dto.JobHistoryDto
import com.nike.mm.entity.JobHistory
import com.nike.mm.facade.IJobHistoryFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class JobHistoryFacade implements IJobHistoryFacade {

    @Autowired
    IJobHistoryBusiness jobHistoryBusiness

    @Override
    Page<JobHistoryDto> findAllByJobid(final String jobid, final Pageable pageable) {
        final Page rpage = this.jobHistoryBusiness.findByJobIdAndPage(jobid, pageable)
        final List<JobHistoryDto> dtos = [];
        for (final JobHistory jh : rpage) {
            dtos.add(toDto(jh))
        }
        return new PageImpl(dtos, pageable, rpage.getTotalElements())
    }

    /**
     * Create JobHistoryDto from JobHistory instance.
     * @param jh - JobHistory instance
     * @return JobHistoryDto instance
     */
    private static JobHistoryDto toDto(final JobHistory jh) {
        return new JobHistoryDto(id: jh.id,
                jobid: jh.jobid,
                startDate: jh.startDate,
                endDate: jh.endDate,
                status: jh.status,
                comments: jh.comments)
    }
}