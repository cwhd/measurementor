package com.nike.mm.rest.constraint

import com.nike.mm.rest.validation.constraint.CronValidator
import spock.lang.Specification


class CronValidatorUnitSpec extends Specification {

    CronValidator cronValidator

    def setup() {
        cronValidator = new CronValidator()
    }

    def "Invalid CRON expression fails" () {

        when:
        def result = cronValidator.isValid("invalid", null)

        then:
        !result
    }

    def "Null CRON expression passes (@NotNull validator enforces value)" () {

        when:
        def result = cronValidator.isValid(null, null)

        then:
        result
    }

    def "Empty string CRON expression passes" () {

        when:
        def result = cronValidator.isValid("", null)

        then:
        result
    }

    def "Valid CRON expression passes" () {

        when:
        def result = cronValidator.isValid("0 * * * * MON-FRI", null)

        then:
        result
    }

}
