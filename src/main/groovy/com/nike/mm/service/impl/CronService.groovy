package com.nike.mm.service.impl

import com.google.common.collect.Maps
import com.nike.mm.dto.MeasureMentorJobsConfigDto
import com.nike.mm.entity.MeasureMentorJobsConfig
import com.nike.mm.facade.IMeasureMentorJobsConfigFacade
import com.nike.mm.facade.IMeasureMentorRunFacade
import com.nike.mm.repository.es.internal.IMeasureMentorJobsConfigRepository
import com.nike.mm.service.ICronService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.Trigger
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.support.CronTrigger
import org.springframework.stereotype.Service

import java.text.MessageFormat
import java.util.concurrent.ScheduledFuture

@Service
class CronService implements ICronService {

    static final String LOG_JOB_CONFIG_NOT_FOUND = "Unable to find configuration for job ID {0}"
    static final String LOG_INVALID_JOB_STATE = "Job ID {0} is not suitable for CRON processing"
//    static final String LOG_ADDING_NEW_JOB = "Added new job ID {0} in queue: {1} jobs active"
    static final String LOG_JOB_NOT_FOUND = "Unable to find job with ID {0}"
    static final String LOG_UNABLE_TO_CANCEL_JOB = "Found but could not cancel job with ID {0}"

    static private final Map<String, ScheduledFuture> SCHEDULED_TASKS = Maps.newHashMap();

    @Autowired
    IMeasureMentorJobsConfigRepository measureMentorJobsConfigRepository

    @Autowired
    ThreadPoolTaskScheduler threadPoolTaskScheduler

    @Autowired
    IMeasureMentorRunFacade measureMentorRunFacade

    @Autowired
    IMeasureMentorJobsConfigFacade measureMentorJobsConfigFacade

    @Override
    void addJob(final String jobId) {

        MeasureMentorJobsConfigDto mmJConfigDto = this.measureMentorJobsConfigFacade.findById(jobId)
        if (null == mmJConfigDto) {
            throw new CronJobRuntimeException(MessageFormat.format(LOG_JOB_CONFIG_NOT_FOUND, jobId))
        }

        if (!(mmJConfigDto.cron && mmJConfigDto.jobOn)) {
            throw new CronJobBusinessException(MessageFormat.format(LOG_INVALID_JOB_STATE, jobId))
        }

        Trigger trigger = new CronTrigger(mmJConfigDto.cron);
        SCHEDULED_TASKS.put(jobId, this.threadPoolTaskScheduler.schedule({
            this.measureMentorRunFacade.runJobId(jobId)
        }, trigger))
//        log.log(Log.Level.DEBUG, MessageFormat.format(LOG_ADDING_NEW_JOB, jobId, SCHEDULED_TASKS.size()))
    }

    @Override
    void removeJob(final String jobId) {

        if (!SCHEDULED_TASKS.containsKey(jobId)) {
            throw new CronJobRuntimeException(MessageFormat.format(LOG_JOB_NOT_FOUND, jobId))
        } else {
            final ScheduledFuture future = SCHEDULED_TASKS.get(jobId)
            if (future.cancel(false)) {
                SCHEDULED_TASKS.remove(jobId)
            } else {
                throw new CronJobRuntimeException(MessageFormat.format(LOG_UNABLE_TO_CANCEL_JOB, jobId))
            }
        }
    }

    /**
     * Get job information from database
     * TODO replace this with call to database. Will it throw exception if no match found ?
     * @param jobId
     * @return
     */
    private MeasureMentorJobsConfig getJobConfigFromId(String jobId) {
        final MeasureMentorJobsConfig mmJConfig = new MeasureMentorJobsConfig();
        mmJConfig.setId(jobId)
        mmJConfig.setCron("0 * * * * MON-FRI")
        mmJConfig.jobOn = true
    }

}