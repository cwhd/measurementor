package com.nike.mm.dto

import com.nike.mm.rest.validation.annotation.ValidCron
import com.nike.mm.rest.validation.annotation.ValidJobId
import org.hibernate.validator.constraints.NotEmpty

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class MeasureMentorJobsConfigDto {

    @ValidJobId
    String id

    @NotEmpty(message = "{job.name.mandatory}")
    @Size(min = 5, max = 100, message = "{job.name.length.invalid}")
    String name

    boolean jobOn

    @NotNull(message = "{job.config.mandatory}")
    Object config

    @NotEmpty(message = "{job.cron.mandatory}")
    @ValidCron
    String cron

    Date lastBuildDate

    String lastbuildstatus
}
