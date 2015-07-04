package com.nike.mm.facade;

/**
 * Main facade to run a configuration script for the system.
 *
 */
public interface IMeasureMentorRunFacade {

    /**
     * Validate the configuration of a given plugin.
     * @param config - configuration for a plugin
     * @return error message if validation failed, null/empty otherwise
     */
    String validateConfig(Object config);

    /**
     * Runs a job based on the job name.
     * @param jobid - The jobid in the es that contains the configuration information.
     */
    void runJobId(String jobid);
}
