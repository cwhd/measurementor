package com.nike.mm.service

public interface ICronService {

    /**
     * Adds/remove a scheduled task for the given job ID
     * @param jobId
     */
    void processJob(String jobId)
}