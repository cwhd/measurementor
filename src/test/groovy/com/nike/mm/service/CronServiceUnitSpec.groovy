package com.nike.mm.service
import com.nike.mm.service.impl.CronService
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import spock.lang.Specification

class CronServiceSpec extends Specification {

    CronService cronService
    ThreadPoolTaskScheduler threadPoolTaskScheduler

    def setup() {
        this.cronService                            = new CronService()
        this.threadPoolTaskScheduler                = Mock(ThreadPoolTaskScheduler.class)
        this.cronService.threadPoolTaskScheduler    = this.threadPoolTaskScheduler
    }

    def "adding a cron job works"() {

        setup:
        String jobId = "valid"

        when:
        boolean result = this.cronService.addJob(jobId)

        then:
        result                                      == true
        1 * this.threadPoolTaskScheduler.schedule(_, _)
    }

}
