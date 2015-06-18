package com.nike.mm.service

import com.nike.mm.facade.IMeasureMentorJobsConfigFacade
import com.nike.mm.service.impl.CronJobBusinessException
import com.nike.mm.service.impl.CronJobRuntimeException
import com.nike.mm.service.impl.CronService
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import spock.lang.Specification

class CronServiceSpec extends Specification {

    CronService cronService
    ThreadPoolTaskScheduler threadPoolTaskScheduler
    IMeasureMentorJobsConfigFacade measureMentorJobsConfigFacade

    def setup() {
        this.cronService                                  = new CronService()
        this.threadPoolTaskScheduler                      = Mock(ThreadPoolTaskScheduler.class)
        this.measureMentorJobsConfigFacade                = Mock(IMeasureMentorJobsConfigFacade.class)
        this.cronService.threadPoolTaskScheduler          = this.threadPoolTaskScheduler
        this.cronService.measureMentorJobsConfigFacade    = this.measureMentorJobsConfigFacade
    }

    def "adding a cron job with an invalid ID fails"() {

        setup:
        String jobId = "invalid"
        String cron = "0 * * * * MON-FRI"

        when:
        this.cronService.addJob(jobId)

        then:
        1 * this.measureMentorJobsConfigFacade.findById(jobId)   >> null
        0 * this.threadPoolTaskScheduler.schedule(_, _)
        thrown(CronJobRuntimeException)
    }

    def "adding a cron job with null cron value fails"() {

        setup:
        String jobId = "valid"
        String cron = "0 * * * * MON-FRI"

        when:
        this.cronService.addJob(jobId)

        then:
        1 * this.measureMentorJobsConfigFacade.findById(jobId)   >> [id: jobId, jobOn: true, cron: null]
        0 * this.threadPoolTaskScheduler.schedule(_, _)
        thrown(CronJobBusinessException)
    }

    def "adding a cron job with empty cron value fails"() {

        setup:
        String jobId = "valid"

        when:
        this.cronService.addJob(jobId)

        then:
        1 * this.measureMentorJobsConfigFacade.findById(jobId)   >> [id: jobId, jobOn: true, cron: ""]
        0 * this.threadPoolTaskScheduler.schedule(_, _)
        thrown(CronJobBusinessException)
    }

    def "adding a cron job with cron but disabled state fails"() {

        setup:
        String jobId = "valid"

        when:
        this.cronService.addJob(jobId)

        then:
        1 * this.measureMentorJobsConfigFacade.findById(jobId)   >> [id: jobId, jobOn: false, cron: "0 * * * * MON-FRI"]
        0 * this.threadPoolTaskScheduler.schedule(_, _)
        thrown(CronJobBusinessException)
    }

    def "adding a cron job works"() {

        setup:
        String jobId = "valid"
        String cron = "0 * * * * MON-FRI"

        when:
        this.cronService.addJob(jobId)

        then:
        1 * this.measureMentorJobsConfigFacade.findById(jobId)   >> [id: jobId, jobOn: true, cron: cron]
        1 * this.threadPoolTaskScheduler.schedule(_, _)
    }

}
