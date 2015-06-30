package com.nike.mm.rest.validation.annotation

import com.nike.mm.rest.validation.constraint.ConfigValidator

import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.ReportAsSingleViolation
import java.lang.annotation.*

@Documented
@Constraint(validatedBy = ConfigValidator.class)
@Target([ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER])
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
@interface ValidConfig {

    Class<?>[] groups() default [];

    String message() default "{job.config.invalid}";

    Class<? extends Payload>[] payload() default [];

}