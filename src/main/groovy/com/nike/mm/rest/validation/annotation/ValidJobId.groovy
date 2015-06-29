package com.nike.mm.rest.validation.annotation

import com.nike.mm.rest.validation.constraint.JobIdValidator

import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.ReportAsSingleViolation
import java.lang.annotation.*

@Documented
@Constraint(validatedBy = JobIdValidator.class)
@Target([ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER])
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
@interface ValidJobId {

    Class<?>[] groups() default [];

    String message() default "{job.id.invalid}";

    Class<? extends Payload>[] payload() default [];
}