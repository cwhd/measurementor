package com.nike.mm.rest.impl

import com.nike.mm.facade.IMeasureMentorRunFacade
import com.nike.mm.rest.IMeasureMentorRunRest
import com.nike.mm.service.ICronService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/run-job")
class MeasureMentorRunRest implements IMeasureMentorRunRest {

    @Autowired
    IMeasureMentorRunFacade measureMentorRunFacade

    @Autowired
    ICronService cronService

    @Override
    @RequestMapping(value = "/start/{jobid}", method = RequestMethod.GET)
    public void runJobId(@PathVariable final String jobid) {
        this.measureMentorRunFacade.runJobId(jobid);
    }
}
