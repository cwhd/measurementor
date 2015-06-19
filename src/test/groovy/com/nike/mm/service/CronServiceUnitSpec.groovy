package com.nike.mm.service

import com.nike.mm.facade.IMeasureMentorJobsConfigFacade
import com.nike.mm.service.impl.CronJobRuntimeException
import com.nike.mm.service.impl.CronService
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import spock.lang.Specification

import java.util.concurrent.ScheduledFuture

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
        String jobId                                             = "invalidId"

        when:
        this.cronService.processJob(jobId)

        then:
        1 * this.measureMentorJobsConfigFacade.findById(jobId)   >> null
        0 * this.threadPoolTaskScheduler.schedule(_, _)
        thrown(CronJobRuntimeException)
    }

    def "adding a cron job with null cron value does nothing"() {

        setup:
        String jobId                                             = "nullCron"

        when:
        this.cronService.processJob(jobId)

        then:
        1 * this.measureMentorJobsConfigFacade.findById(jobId)   >> [id: jobId, jobOn: true, cron: null]
        0 * this.threadPoolTaskScheduler.schedule(_, _)
    }

    def "adding a cron job with empty cron value does nothing"() {

        setup:
        String jobId                                             = "emptyCron"

        when:
        this.cronService.processJob(jobId)

        then:
        1 * this.measureMentorJobsConfigFacade.findById(jobId)   >> [id: jobId, jobOn: true, cron: ""]
        0 * this.threadPoolTaskScheduler.schedule(_, _)
    }

    def "adding a cron job with cron but jobOn false fails"() {

        setup:
        String jobId                                             = "cronButJobOff"

        when:
        this.cronService.processJob(jobId)

        then:
        1 * this.measureMentorJobsConfigFacade.findById(jobId)   >> [id: jobId, jobOn: false, cron: "0 * * * * MON-FRI"]
        0 * this.threadPoolTaskScheduler.schedule(_, _)
    }

    def "adding a cron job works"() {

        setup:
        String jobId                                             = "valid"
        String cron                                              = "0 * * * * MON-FRI"
        ScheduledFuture future                                   = Mock(ScheduledFuture.class)

        when:
        this.cronService.processJob(jobId)

        then:
        1 * this.measureMentorJobsConfigFacade.findById(jobId)   >> [id: jobId, jobOn: true, cron: cron]
        1 * this.threadPoolTaskScheduler.schedule(_, _)          >> future
        0 * future.cancel(_)                                     >> true
    }

    def "adding a valid cron job and then disabling it works"() {

        setup:
        String jobId                                             = "jobOnAndJobOff"
        String cron                                              = "0 * * * * MON-FRI"
        ScheduledFuture future                                   = Mock(ScheduledFuture.class)

        when:
        this.cronService.processJob(jobId)
        this.cronService.processJob(jobId)

        then:
        2 * this.measureMentorJobsConfigFacade.findById(jobId)   >>> [[id: jobId, jobOn: true, cron: cron],[id: jobId, jobOn: false, cron: cron]]
        1 * this.threadPoolTaskScheduler.schedule(_, _)          >> future
        1 * future.cancel(_)                                     >> true
    }

    def "adding a disabled cron job and then enabling it works"() {

        setup:
        String jobId                                             = "JobOffAndJobOn"
        String cron                                              = "0 * * * * MON-FRI"
        ScheduledFuture future                                   = Mock(ScheduledFuture.class)

        when:
        this.cronService.processJob(jobId)
        this.cronService.processJob(jobId)

        then:
        2 * this.measureMentorJobsConfigFacade.findById(jobId)   >>> [[id: jobId, jobOn: false, cron: cron],[id: jobId, jobOn: true, cron: cron]]
        1 * this.threadPoolTaskScheduler.schedule(_, _)          >> future
        0 * future.cancel(_)                                     >> true
    }

}
