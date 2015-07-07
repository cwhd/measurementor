package com.nike.mm.business.internal.impl

import com.nike.mm.business.internal.IJobHistoryBusiness
import com.nike.mm.dto.JobRunRequestDto
import com.nike.mm.dto.JobRunResponseDto
import com.nike.mm.entity.internal.JobConfigHistory
import com.nike.mm.entity.internal.JobHistory
import com.nike.mm.repository.es.internal.IJobConfigHistoryRepository
import com.nike.mm.repository.es.internal.IJobHistoryRepository
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
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
     * Error message when a plugin failed for a given reason
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

    @Value('${mm.plugin.defaultStartDate}')
    String defaultStartDate

    @Override
    JobHistory save(JobHistory jobHistory) {
        this.jobHistoryRepository.save(jobHistory);
    }

    @Override
    JobHistory findLastSuccessfulJobRanForJobid(String jobid) {
        Page<JobHistory> rpage = this.jobHistoryRepository.findByJobidAndStatus(jobid, JobHistory.Status.success,
                getDefaultDescEndDatePageRequest())
        JobHistory rjh = null;
        if (rpage.content.size() > 0) {
            rjh = rpage.content[0];
        }
        return rjh;
    }

    @Override
    JobHistory findJobsLastBuildStatus(String jobid) {
        Page<JobHistory> rpage = this.jobHistoryRepository.findByJobid(jobid, getDefaultDescEndDatePageRequest())
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
        log.debug("Retrieving job history for $request.jobid plugin $request.pluginType")

        Date lastRunDate = new SimpleDateFormat("dd/MM/yyyy").parse(this.defaultStartDate);

        Page<JobConfigHistory> results = this.jobConfigHistoryRepository.findByJobidAndStatusAndType(request.jobid,
                JobHistory.Status.success, request.pluginType, getDefaultDescEndDatePageRequest())
        if (0 != results.content.size()) {
            lastRunDate = results.content[0].endDate
        }
        return lastRunDate
    }

    @Override
    JobHistory createJobHistory(final String jobid, final Date startDate) {

        final JobHistory jh = new JobHistory(
                jobid: jobid,
                startDate: startDate)

        return this.jobHistoryRepository.save(jh)
    }

    @Override
    JobConfigHistory createJobPluginHistory(
            final String jobid, final String jobHistoryId, final String pluginType) {

        final JobHistory jh = this.jobHistoryRepository.findOne(jobHistoryId)

        JobConfigHistory jch = new JobConfigHistory(
                jobid: jobid,
                jobHistoryid: jh.id,
                type: pluginType,
                startDate: jh.startDate)
        return this.jobConfigHistoryRepository.save(jch)
    }

    @Override
    void saveJobRunResults(final String jobid, final Date endDate, final List<JobRunResponseDto> responses) {

        final StringBuilder sb = new StringBuilder(StringUtils.EMPTY)
        responses.each { response ->
            if (response.errorMessage) {
                sb.append(MessageFormat.format(PLUGIN_FAILED, response.type))
                sb.append(response.errorMessage)
                sb.append(StringUtils.CR)
            }
        }
        final String messages = sb.toString()

        //todo come up with custom method
        final JobHistory jh = this.jobHistoryRepository.findOne(jobid)

        jh.endDate = endDate
        if (messages) {
            jh.status = JobHistory.Status.error
            jh.comments = messages
        } else {
            jh.status = JobHistory.Status.success
        }
        this.jobHistoryRepository.save(jh)

        responses.each { final response ->
            final JobConfigHistory jch = new JobConfigHistory(
                    jobid: jobid,
                    jobHistoryid: jh.id,
                    type: response.type,
                    startDate: jh.startDate,
                    endDate: endDate,
                    recordsCount: response.recordsCount,
                    status: response.status,
                    comments: response.errorMessage)
            this.jobConfigHistoryRepository.save(jch)
        }
    }

    static PageRequest getDefaultDescEndDatePageRequest() {
        new PageRequest(0, 1, new Sort(Sort.Direction.DESC, END_DATE))
    }
}
