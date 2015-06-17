package com.nike.mm.business.internal.impl

import com.nike.mm.business.internal.IDateBusiness
import org.joda.time.DateTime
import org.springframework.stereotype.Service


@Service
class DateBusiness implements IDateBusiness {

    DateTime getCurrentDateTime() {
        return new DateTime()

    }
}
