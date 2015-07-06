package com.nike.mm.core.exception

abstract class AbstractMmException extends RuntimeException {

    AbstractMmException(final String message) {
        super(message)
    }
}
