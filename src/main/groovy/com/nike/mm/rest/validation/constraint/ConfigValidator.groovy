package com.nike.mm.rest.validation.constraint

import com.google.common.collect.Lists
import com.nike.mm.facade.impl.MeasureMentorJobsConfigFacade
import com.nike.mm.facade.impl.MeasureMentorRunFacade
import com.nike.mm.rest.validation.annotation.ValidConfig
import org.springframework.beans.factory.annotation.Autowired

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class ConfigValidator implements ConstraintValidator<ValidConfig, Object> {

    public static final String CONFIGURATION_MANDATORY = "Configuration is mandatory"

    public static final String TYPE_FIELD_MANDATORY = "A field of name 'type' must be provided (GitHub, Jira, Jenkins, Stash)"

    @Autowired
    MeasureMentorRunFacade measureMentorRunFacade

    @Override
    void initialize(ValidConfig constraintAnnotation) {

    }

    @Override
    boolean isValid(Object value, ConstraintValidatorContext context) {
        List<String> errorMessages = Lists.newArrayList()
        if (!value) {
            addViolation(CONFIGURATION_MANDATORY, errorMessages)
        } else {
            boolean isCollection = isCollectionOrArray(value)
            if (isCollection) {
                value.each {config ->
                    validateSingleConfig(config, errorMessages)
                }
            } else {
                validateSingleConfig(value, errorMessages)
            }
        }

        boolean isValid = true
        if (errorMessages.size()) {
            isValid = false
            context.disableDefaultConstraintViolation()
            errorMessages.each { errorMessage ->
                context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation()
            }
        }
        return isValid
    }

    private static boolean isCollectionOrArray(Object value) {
        [Collection, Object[]].any { it.isAssignableFrom(value.getClass()) }
    }

    private void validateSingleConfig(Object config, List<String> errorMessages) {
        String errorMessage
        if (!config.type) {
            errorMessage = TYPE_FIELD_MANDATORY
        } else {
            errorMessage = this.measureMentorRunFacade.validateConfig(config)
        }
        if (errorMessage) {
            addViolation(errorMessage, errorMessages)
        }
    }

    private static void addViolation(String errorMessage, List<String> errorMessages) {
        errorMessages.add(errorMessage)
    }
}
