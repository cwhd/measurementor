package com.nike.mm.facade.impl

import com.google.common.collect.Lists
import com.nike.mm.business.internal.IJobHistoryBusiness
import com.nike.mm.business.internal.IMeasureMentorJobsConfigBusiness
import com.nike.mm.business.internal.IMeasureMentorRunBusiness
import com.nike.mm.business.plugins.IMeasureMentorBusiness
import com.nike.mm.dto.JobRunRequestDto
import com.nike.mm.dto.JobRunResponseDto
import com.nike.mm.entity.internal.JobConfigHistory
import com.nike.mm.entity.internal.JobHistory
import com.nike.mm.facade.IMeasureMentorRunFacade
import com.nike.mm.service.IDateService
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import groovyx.gpars.GParsPool
import org.jasypt.util.text.TextEncryptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.text.MessageFormat

@Service
@Slf4j
class MeasureMentorRunFacade implements IMeasureMentorRunFacade {

    /**
     * Error message when no plugin found in a given configuration
     */
    public static final String NO_MATCHING_PLUGIN = "No measure mentor configured for: {0}"

    @Autowired
    Set<IMeasureMentorBusiness> measureMentorBusinesses

    @Autowired
    IMeasureMentorJobsConfigBusiness measureMentorConfigBusiness

    @Autowired
    IMeasureMentorRunBusiness measureMentorRunBusiness

    @Autowired
    IJobHistoryBusiness jobHistoryBusiness

    @Autowired
    IDateService dateService

    @Autowired
    TextEncryptor textEncryptor

    @Override
    void runJobId(final String jobid) {
        log.debug("Running job ID $jobid")

        final def startDate = this.dateService.currentDateTime

        try {
            this.measureMentorRunBusiness.startJob(jobid)
            final def entity = this.measureMentorConfigBusiness.findById(jobid)
            final def config = new JsonSlurper().parseText(this.textEncryptor.decrypt(new String(Base64.getDecoder().decode
                    (entity.encryptedConfig))))

            // create job history entry
            final JobHistory jh = this.jobHistoryBusiness.createJobHistory(jobid, startDate)

            final List<JobRunRequestDto> requests = createRequestsFromConfig(jobid, jh.id, config)

            final List<JobRunResponseDto> responses = Lists.newArrayListWithCapacity(requests.size())

            // execute all plugins in parallel
            GParsPool.withPool {
                requests.eachParallel { final request ->
                    this.invokePlugin(request, responses)
                }
            }

            this.jobHistoryBusiness.saveJobRunResults(jh.id, this.dateService.currentDateTime, responses)

        } finally {
            this.measureMentorRunBusiness.stopJob(jobid);
        }
    }

    @Override
    String validateConfig(final Object config) {
        final String errorMessage
        final IMeasureMentorBusiness plugin = this.findByType(config.type)
        if (!plugin) {
            errorMessage = MessageFormat.format(NO_MATCHING_PLUGIN, config.type)
        } else {
            errorMessage = plugin.validateConfig(config)
        }

        return errorMessage
    }

    private static List<JobRunRequestDto> createRequestsFromConfig(
            final String jobid, final String jobHistoryId, final def configs) {

        final List<JobRunRequestDto> list = Lists.newArrayList()
        if (isCollectionOrArray(configs)) {
            configs.each { final config ->
                list.add(createRequestfromConfig(jobid, jobHistoryId, config))
            }
        } else {
            list.add(createRequestfromConfig(jobid, jobHistoryId, configs))
        }
        return list
    }

    /**
     * Create a request object for a given plugin
     * @param jobid - ID of the configuration that will be run
     * @param jobHistoryId - Job run ID
     * @param config - Configuration that will be executed
     * @return Request object
     */
    private static JobRunRequestDto createRequestfromConfig(
            final String jobid, final String jobHistoryId, final def config) {
        return new JobRunRequestDto(
                jobid: jobid,
                jobHistoryId: jobHistoryId,
                config: config,
                pluginType: config.type)
    }

    private void invokePlugin(final JobRunRequestDto request, final List<JobRunResponseDto> responses) {

        log.debug("Invoking plugin $request.pluginType")

        // retrieve the last time this job/plugin was run successfully
        final Date lastRunDate = this.jobHistoryBusiness.findLastSuccessfulJobRanForJobidAndPlugin(request)

        final IMeasureMentorBusiness plugin = this.findByType(request.pluginType)
        if (null == plugin) {
            final String errorMessage = MessageFormat.format(NO_MATCHING_PLUGIN, request.pluginType)
            responses.add(createFailureResponse(request.pluginType, errorMessage))
        } else {
            final String errorMessage = plugin.validateConfig(request.config)
            if (errorMessage) {
                responses.add(createFailureResponse(request.pluginType, errorMessage))
            } else {
                // create new entry in job history for plugin
                final JobConfigHistory jch = this.jobHistoryBusiness.createJobPluginHistory(request.jobid,
                        request.jobHistoryId, request.pluginType)
                // run plugin
                responses.add(plugin.updateDataWithResponse(jch.id, lastRunDate, request.config))
                // save results in plugin history

            }
        }
    }

    private static JobRunResponseDto createFailureResponse(final String pluginType, final String errorMessage) {
        [type: pluginType, recordsCount: 0, status: JobHistory.Status.error, errorMessage:
                errorMessage] as JobRunResponseDto
    }

    private IMeasureMentorBusiness findByType(final String type) {
        for (final IMeasureMentorBusiness mmb : this.measureMentorBusinesses) {
            if (mmb.type() == type) {
                return mmb;
            }
        }
        return null;
    }

    private static boolean isCollectionOrArray(Object value) {
        [Collection, Object[]].any { it.isAssignableFrom(value.getClass()) }
    }
}
