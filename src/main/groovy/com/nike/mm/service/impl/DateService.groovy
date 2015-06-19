package com.nike.mm.service.impl

import com.nike.mm.service.IDateService
import org.joda.time.DateTime
import org.springframework.stereotype.Service


@Service
class DateService implements IDateService {

    Date getCurrentDateTime() {
        return new DateTime().toDate()

    }
}
