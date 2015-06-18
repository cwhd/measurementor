package com.nike.mm.service

public interface ICronService {

    /**
     * Add a cron job
     * @param jobId
     */
    boolean addJob(String jobId)

    void resetJobs();

    /**
     * Remove a cron job
     * @param jobId - String
     * @return true if the job was removed successfully
     */
    boolean removeJob(String jobId)
}