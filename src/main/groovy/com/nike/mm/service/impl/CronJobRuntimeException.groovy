package com.nike.mm.service.impl

import com.nike.mm.core.exception.AbstractMmException


class CronJobRuntimeException extends AbstractMmException {

    CronJobRuntimeException(String message) {
        super(message)
    }
}
