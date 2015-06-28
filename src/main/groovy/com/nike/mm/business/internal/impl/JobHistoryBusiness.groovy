package com.nike.mm.business.internal.impl

import com.nike.mm.business.internal.IJobHistoryBusiness
import com.nike.mm.dto.JobRunRequestDto
import com.nike.mm.dto.JobRunResponseDto
import com.nike.mm.entity.JobConfigHistory
import com.nike.mm.entity.JobHistory
import com.nike.mm.repository.es.internal.IJobConfigHistoryRepository
import com.nike.mm.repository.es.internal.IJobHistoryRepository
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

import java.text.MessageFormat
import java.text.SimpleDateFormat

@Slf4j
@Service
class JobHistoryBusiness implements IJobHistoryBusiness {

    /**
     * Error message when a plugin failed for a givne reason
     */
    public static final String PLUGIN_FAILED = "Plugin {0} failed: "

    /**
     * Message in case of success
     */
    public static final String SUCCESS = "Success"

    /**
     * endDate field
     */
    public static final String END_DATE = "endDate"

    @Autowired
    IJobHistoryRepository jobHistoryRepository;

    @Autowired
    IJobConfigHistoryRepository jobConfigHistoryRepository

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

    @Override
    Date findLastSuccessfulJobRanForJobidAndPlugin(JobRunRequestDto request) {
        log.debug("Retrieving job history for {}/{}", request.jobid, request.pluginType)

        Date lastRunDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1901");

        Page<JobConfigHistory> results = this.jobConfigHistoryRepository.findByJobidAndStatusAndType(request.jobid,
                JobHistory.Status.success, request.pluginType, getDefaultDescEndDatePagerequest())
        if (0 != results.content.size()) {
            lastRunDate = results.content[0].endDate
        }
        return lastRunDate
    }

    @Override
    void saveJobRunResults(String jobid, Date startDate, Date endDate, List<JobRunResponseDto> responses) {

        StringBuilder sb = new StringBuilder(StringUtils.EMPTY)
        responses.each { response ->
            if (response.errorMessage) {
                sb.append(MessageFormat.format(PLUGIN_FAILED, response.type))
                sb.append(response.errorMessage)
                sb.append(StringUtils.CR)
            }
        }
        String messages = sb.toString()

        JobHistory jh = [
                jobid    : jobid,
                startDate: startDate,
                endDate  : endDate,
                status   : JobHistory.Status.success,
                comments : SUCCESS

        ] as JobHistory
        if (messages) {
            jh.status = JobHistory.Status.error
            jh.comments = messages
        }

        jh = this.jobHistoryRepository.save(jh)

        responses.each { response ->
            JobConfigHistory jch = [
                    jobid       : jobid,
                    jobHistoryid: jh.id,
                    type        : response.type,
                    startDate   : startDate,
                    endDate     : endDate,
                    recordsCount: response.recordsCount,
                    status      : response.status,
                    comments    : response.errorMessage
            ] as JobConfigHistory
            this.jobConfigHistoryRepository.save(jch)
        }
    }

    static PageRequest getDefaultDescEndDatePagerequest() {
        new PageRequest(0, 1, new Sort(Sort.Direction.DESC, END_DATE))
    }
}
