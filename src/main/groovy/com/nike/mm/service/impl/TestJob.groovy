package com.nike.mm.service.impl

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

/**
 * TestJob.
 */
@Service
class TestJob {

    @Scheduled(cron = "0 * * * * MON-FRI")
    void demoServiceMethod() {
        println "test at " + new Date()
    }
}
