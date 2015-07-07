package com.nike.mm.business.internal;

import com.nike.mm.dto.JobRunRequestDto;
import com.nike.mm.dto.JobRunResponseDto;
import com.nike.mm.entity.internal.JobConfigHistory;
import com.nike.mm.entity.internal.JobHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface IJobHistoryBusiness {

    JobHistory save(JobHistory jobHistory);

    JobHistory findJobsLastBuildStatus(String jobid);

    JobHistory findLastSuccessfulJobRanForJobid(String jobid);

    Page<JobHistory> findByJobIdAndPage(String jobid, Pageable pageable);

    Date findLastSuccessfulJobRanForJobidAndPlugin(JobRunRequestDto request);

    JobHistory createJobHistory(String jobid, Date startDate);

    JobConfigHistory createJobPluginHistory(String jobid, String jobHistoryId, String pluginType);

    void saveJobRunResults(String jobid, Date endDate, List<JobRunResponseDto> responses);
}
