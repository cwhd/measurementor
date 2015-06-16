package com.nike.mm.facade;

/**
 * Main facade to run a configuration script for the system.
 *
 */
public interface IMeasureMentorRunFacade {

    /**
     * Runs a job based on the job name.
     * @param jobid - The jobid in the es that contains the configuration information.
     */
    void runJobId(String jobid);
}
