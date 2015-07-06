package com.nike.mm.facade.impl

import com.google.common.collect.Lists
import com.nike.mm.business.internal.IJobHistoryBusiness
import com.nike.mm.business.internal.IMeasureMentorJobsConfigBusiness
import com.nike.mm.business.internal.IMeasureMentorRunBusiness
import com.nike.mm.business.plugins.IMeasureMentorBusiness
import com.nike.mm.dto.JobRunRequestDto
import com.nike.mm.dto.JobRunResponseDto
import com.nike.mm.entity.JobHistory
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
    void runJobId(String jobid) {
        log.debug("Running job ID $jobid")

        final def startDate = this.dateService.currentDateTime

        try {
            this.measureMentorRunBusiness.startJob(jobid)
            final def entity = this.measureMentorConfigBusiness.findById(jobid)
            final def config = new JsonSlurper().parseText(this.textEncryptor.decrypt(new String(Base64.getDecoder().decode
                    (entity.encryptedConfig))))

            List<JobRunRequestDto> requests = createRequestsFromConfig(jobid, config)

            List<JobRunResponseDto> responses = Lists.newArrayListWithCapacity(requests.size())

            // execute all plugins in parallel
            GParsPool.withPool {
                requests.eachParallel { request ->
                    this.invokePlugin(request, responses)
                }
            }

            this.jobHistoryBusiness.saveJobRunResults(jobid, startDate, this.dateService.currentDateTime, responses)

        } finally {
            this.measureMentorRunBusiness.stopJob(jobid);
        }
    }

    @Override
    String validateConfig(Object config) {
        String errorMessage
        IMeasureMentorBusiness plugin = this.findByType(config.type)
        if (!plugin) {
            errorMessage = MessageFormat.format(NO_MATCHING_PLUGIN, config.type)
        } else {
            errorMessage = plugin.validateConfig(config)
        }

        return errorMessage
    }

    private static List<JobRunRequestDto> createRequestsFromConfig(String jobid, def configs) {

        List<JobRunRequestDto> list = Lists.newArrayList()
        if (isCollectionOrArray(configs)) {
            configs.each { config ->
                list.add(createRequestfromConfig(jobid, config))
            }
        } else {
            list.add(createRequestfromConfig(jobid, configs))
        }
        return list
    }

    private static JobRunRequestDto createRequestfromConfig(String jobid, def config) {
        JobRunRequestDto request = new JobRunRequestDto()
        request.jobid = jobid
        request.config = config
        request.pluginType = config.type
        return request
    }

    private void invokePlugin(JobRunRequestDto request, List<JobRunResponseDto> responses) {

        log.debug("Invoking plugin $request.pluginType")

        // retrieve the last time this job/plugin was run successfully
        Date lastRunDate = this.jobHistoryBusiness.findLastSuccessfulJobRanForJobidAndPlugin(request)

        IMeasureMentorBusiness plugin = this.findByType(request.pluginType)
        if (null == plugin) {
            String errorMessage = MessageFormat.format(NO_MATCHING_PLUGIN, request.pluginType)
            responses.add(createFailureResponse(request.pluginType, errorMessage))
        } else {
            String errorMessage = plugin.validateConfig(request.config)
            if (errorMessage) {
                responses.add(createFailureResponse(request.pluginType, errorMessage))
            } else {
                responses.add(plugin.updateDataWithResponse(lastRunDate, request.config))
            }
        }
    }

    private static JobRunResponseDto createFailureResponse(String pluginType, String errorMessage) {
        [type: pluginType, recordsCount: 0, status: JobHistory.Status.error, errorMessage:
                errorMessage] as JobRunResponseDto
    }

    private IMeasureMentorBusiness findByType(final String type) {
        for (IMeasureMentorBusiness mmb : this.measureMentorBusinesses) {
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
