package com.nike.mm.business.internal;

public interface IMeasureMentorRunBusiness {

    boolean isJobRunning(String jobid);

    void startJob(String jobid);

    void stopJob(String jobid);
}
