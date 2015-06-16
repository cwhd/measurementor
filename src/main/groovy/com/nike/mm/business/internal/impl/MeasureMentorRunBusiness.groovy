package com.nike.mm.business.internal.impl

import org.springframework.stereotype.Service

import com.nike.mm.business.internal.IMeasureMentorRunBusiness

@Service
class MeasureMentorRunBusiness implements IMeasureMentorRunBusiness {
	
	List<String> runningJobs = [];

	boolean isJobRunning(String jobid) {
		return this.runningJobs.contains(jobid);
	}
	
	void startJob(String jobid) {
		if ( this.isJobRunning(jobid)) {
			//TODO Handle this better.
			throw new RuntimeException('Job already running: $jobid')
		}
		this.runningJobs.add(jobid);
	}
	
	void stopJob(String jobid) {
		this.runningJobs.remove(jobid);
	}
}
