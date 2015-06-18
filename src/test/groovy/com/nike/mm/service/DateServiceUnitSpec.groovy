package com.nike.mm.service

import com.nike.mm.service.impl.DateService
import spock.lang.Specification

class DateServiceUnitSpec extends Specification {

    IDateService dateService

    def setup() {
        this.dateService = new DateService()
    }

    def "Get the current date"() {

        when:
        def currentDate = this.dateService.getCurrentDateTime()

        then:
        currentDate != null
    }


}
