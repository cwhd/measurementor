package com.nike.mm.business.internal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nike.mm.entity.JobHistory;

public interface IJobHistoryBusiness {

    JobHistory save(JobHistory jobHistory);

    JobHistory findJobsLastBuildStatus(String jobid);

    JobHistory findLastSuccessfulJobRanForJobid(String jobid);

    Page<JobHistory> findByJobIdAndPage(String jobid, Pageable pageable);
}
