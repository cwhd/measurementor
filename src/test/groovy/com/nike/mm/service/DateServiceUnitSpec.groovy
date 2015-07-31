package com.nike.mm.service

import com.nike.mm.service.impl.DateService
import com.nike.mm.service.impl.UtilitiesService
import spock.lang.Specification

class DateServiceUnitSpec extends Specification {

    IDateService dateService
    IUtilitiesService utilitiesService

    def setup() {
        this.dateService = new DateService()
        this.utilitiesService = new UtilitiesService()
    }

    def "Get the current date"() {

        when:
        def currentDate = this.dateService.getCurrentDateTime()

        then:
        currentDate != null
    }

    def "Make sure names get scrubbed"() {
        expect: finalName == utilitiesService.cleanPersonName(name)
        where:
            name | finalName
            "chris_davis" | "chris_davis"
            "chris.davis@whatever.com" | "chris_davis"
            "davis, chris" | "chris_davis"
            "davis, chris (ETW)" | "chris_davis"
            "davis, chris (etw)" | "chris_davis"
            "chris davis (ETW)" | "chris_davis"
            null | "system_ghost"
            "" | "system_ghost"
    }


}
