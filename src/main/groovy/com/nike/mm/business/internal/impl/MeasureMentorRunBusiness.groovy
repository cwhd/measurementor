package com.nike.mm.business.internal.impl

import com.nike.mm.business.internal.IMeasureMentorRunBusiness
import com.nike.mm.core.exception.impl.JobAlreadyRunningException
import org.springframework.stereotype.Service

@Service
class MeasureMentorRunBusiness implements IMeasureMentorRunBusiness {
	
	List<String> runningJobs = [];

	boolean isJobRunning(final String jobid) {
		return this.runningJobs.contains(jobid);
	}
	
	void startJob(final String jobid) {
		if ( this.isJobRunning(jobid)) {
			//TODO Handle this better.
			throw new JobAlreadyRunningException("Job already running: $jobid")
		}
		this.runningJobs.add(jobid);
	}
	
	void stopJob(final String jobid) {
		this.runningJobs.remove(jobid);
	}
}
