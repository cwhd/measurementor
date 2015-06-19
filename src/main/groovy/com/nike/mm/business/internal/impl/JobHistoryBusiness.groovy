package com.nike.mm.business.internal.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

import com.nike.mm.business.internal.IJobHistoryBusiness
import com.nike.mm.entity.JobHistory
import com.nike.mm.repository.es.internal.IJobHistoryRepository

@Service
class JobHistoryBusiness implements IJobHistoryBusiness {

    @Autowired
    IJobHistoryRepository jobHistoryRepository;

    @Override
    JobHistory save(JobHistory jobHistory) {
        this.jobHistoryRepository.save(jobHistory);
    }

    @Override
    JobHistory findLastSuccessfulJobRanForJobid(String jobid) {
        Page<JobHistory> rpage = this.jobHistoryRepository.findByJobidAndStatus(jobid, JobHistory.Status.success,
                getDefaultDescEndDatePagerequest())
        JobHistory rjh = null;
        if (rpage.content.size() > 0) {
            rjh = rpage.content[0];
        }
        return rjh;
    }

    @Override
    JobHistory findJobsLastBuildStatus(String jobid) {
        Page<JobHistory> rpage = this.jobHistoryRepository.findByJobid(jobid, getDefaultDescEndDatePagerequest())
        JobHistory rjh = null;
        if (rpage.content.size() > 0) {
            rjh = rpage.content[0];
        }
        return rjh;
    }

    @Override
    Page<JobHistory> findByJobIdAndPage(String jobid, Pageable pageable) {
        return this.jobHistoryRepository.findByJobid(jobid, pageable)
    }

    static PageRequest getDefaultDescEndDatePagerequest() {
        new PageRequest(0, 1, new Sort(Sort.Direction.DESC, "endDate"))
    }
}
