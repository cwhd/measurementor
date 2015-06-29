package com.nike.mm.rest.validation.annotation

import com.nike.mm.rest.validation.constraint.CronValidator

import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.ReportAsSingleViolation
import java.lang.annotation.*

@Documented
@Constraint(validatedBy = CronValidator.class)
@Target([ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER])
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
@interface ValidCron {

    Class<?>[] groups() default [];

    String message() default "{job.cron.format.invalid}";

    Class<? extends Payload>[] payload() default [];

}