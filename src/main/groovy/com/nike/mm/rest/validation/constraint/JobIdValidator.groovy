package com.nike.mm.rest.validation.constraint

import com.nike.mm.facade.IMeasureMentorJobsConfigFacade
import com.nike.mm.rest.validation.annotation.ValidJobId
import org.springframework.beans.factory.annotation.Autowired

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class JobIdValidator implements ConstraintValidator<ValidJobId, String> {

    @Autowired
    IMeasureMentorJobsConfigFacade measureMentorJobsConfigFacade

    @Override
    void initialize(ValidJobId constraintAnnotation) {
        // do nothing
    }

    @Override
    boolean isValid(String id, ConstraintValidatorContext context) {
        boolean isValid = true

        if (id) {
            def dto = measureMentorJobsConfigFacade.findById(id)
            if (!dto) {
                isValid = false
            }
        }

        return isValid
    }
}
