package com.nike.mm.service

import com.nike.mm.service.impl.CronJobBusinessException

public interface ICronService {

    /**
     * Add a cron job
     * Does not update the database
     * @param jobId
     * @throws CronJobBusinessException if job is not a state to run
     * @throws CronRuntimeException when in case of other errors (job not found, failure to cancel, ...)
     */
    void addJob(String jobId)

    /**
     * Remove a cron job
     * @param jobId - String
     * @return true if the job was removed successfully
     */
    void removeJob(String jobId)
}