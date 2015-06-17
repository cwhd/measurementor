package com.nike.mm.business.internal

import com.nike.mm.business.internal.impl.DateBusiness
import spock.lang.Specification

class DateBusinessUnitSpec extends Specification {

    IDateBusiness dateBusiness

    def setup() {
        this.dateBusiness = new DateBusiness()
    }

    def "Get the current date"() {

        when:
        def currentDate = this.dateBusiness.getCurrentDateTime()

        then:
        currentDate != null
    }


}
