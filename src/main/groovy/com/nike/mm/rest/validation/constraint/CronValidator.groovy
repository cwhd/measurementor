package com.nike.mm.rest.validation.constraint

import com.nike.mm.rest.validation.annotation.ValidCron
import org.springframework.scheduling.support.CronSequenceGenerator

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class CronValidator implements ConstraintValidator<ValidCron, String> {

    @Override
    void initialize(ValidCron constraintAnnotation) {

    }

    @Override
    boolean isValid(String value, ConstraintValidatorContext context) {
        if (value) {
            try {
                new CronSequenceGenerator(value)
            } catch (IllegalArgumentException iae) {
                return false
            }
        }
        return true
    }
}
