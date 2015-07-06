package com.nike.mm.core.exception.impl

import com.nike.mm.core.exception.AbstractMmException


class JobAlreadyRunningException extends AbstractMmException {

    /**
     * Job is already running exception
     * @param message
     */
    JobAlreadyRunningException(final String message) {
        super(message)
    }
}
