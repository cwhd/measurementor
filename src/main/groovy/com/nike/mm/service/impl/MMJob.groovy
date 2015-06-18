package com.nike.mm.service.impl
import groovy.util.logging.Log

@Log
class MMJob implements Runnable {

    String jobId
    String cron

    static MMJob createInstance(String jobId, String cron) {
        final MMJob mmJob = new MMJob()
        mmJob.setJobId(jobId)
        mmJob.setCron(cron)
    }

    @Override
    void run() {
        logEvent(this.jobId)
    }

    private void logEvent(String message) {
        println "Executing job " + message
    }
}
