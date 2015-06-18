package com.nike.mm.service.impl

import com.nike.mm.core.exception.AbstractMmException


class CronJobBusinessException extends AbstractMmException {

    CronJobBusinessException(String message) {
        super(message)
    }
}
