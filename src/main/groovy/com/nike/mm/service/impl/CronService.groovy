package com.nike.mm.service.impl

import com.google.common.collect.Maps
import com.nike.mm.entity.MeasureMentorJobsConfig
import com.nike.mm.repository.es.internal.IMeasureMentorJobsConfigRepository
import com.nike.mm.service.ICronService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.Trigger
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.support.CronTrigger
import org.springframework.stereotype.Service

import java.util.concurrent.ScheduledFuture

@Service
class CronService implements ICronService {

//    static final String LOG_JOB_NOT_FOUND = "Unable to find job with ID {0}"
//    static final String LOG_UNABLE_TO_CANCEL_JOB = "Found but could not cancel job with ID {0}"
//    static final String LOG_JOB_CANCELED = "Found and canceled job with ID {0}"
//    static final String LOG_PREPARING_TO_ADD_CRON = "Preparing to add new cron job ID {0}"
//    static final String LOG_ADDING_NEW_CRON = "Adding new job ID {0}"
//    static final String LOG_ACTIVE_CRON_COUNT = "Number of cron jobs is {0}"
    static private final Map<String, ScheduledFuture> SCHEDULED_TASKS = Maps.newHashMap();

    @Autowired
    IMeasureMentorJobsConfigRepository measureMentorJobsConfigRepository

    @Autowired
    ThreadPoolTaskExecutor serviceTaskExecutor

    @Autowired
    ThreadPoolTaskScheduler threadPoolTaskScheduler

    @Override
    boolean addJob(final String jobId) {
        boolean result = false
        MeasureMentorJobsConfig mmJConfig = this.getJobConfigFromId(jobId)
        if (null != mmJConfig) {
//            log.log(Level.FINE, MessageFormat.format(LOG_PREPARING_TO_ADD_CRON, jobId))
            final String cron = mmJConfig.getCron()
            if (cron) {
                final MMJob mmJob = MMJob.createInstance(jobId, cron)
                Trigger trigger = new CronTrigger(cron);
//                log.log(Level.FINE, MessageFormat.format(LOG_ADDING_NEW_CRON, jobId))
                SCHEDULED_TASKS.put(jobId, this.threadPoolTaskScheduler.schedule(mmJob, trigger))
//                log.log(Level.FINE, MessageFormat.format(LOG_ACTIVE_CRON_COUNT, SCHEDULED_TASKS.size()))
                result = true
            }
        }
        return result
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
        mmJConfig
    }

    @Override
    void resetJobs() {
        throw new RuntimeException("Not implemented yet")
    }

    @Override
    boolean removeJob(final String jobId) {
        boolean removalSuccessful = false
        if (!SCHEDULED_TASKS.containsKey(jobId)) {
//            log.log(Level.FINE, MessageFormat.format(LOG_JOB_NOT_FOUND, jobId))
        } else {
            final ScheduledFuture future = SCHEDULED_TASKS.get(jobId)
            removalSuccessful = future.cancel(false)
            if (removalSuccessful) {
                SCHEDULED_TASKS.remove(jobId)
//                log.log(Level.FINE, MessageFormat.format(LOG_JOB_CANCELED, jobId))
//            } else {
//                log.log(Level.FINE, MessageFormat.format(LOG_UNABLE_TO_CANCEL_JOB, jobId))
            }
        }
        return removalSuccessful
    }
}