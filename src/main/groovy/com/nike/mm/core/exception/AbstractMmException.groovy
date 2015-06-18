package com.nike.mm.core.exception

abstract class AbstractMmException extends RuntimeException {

    AbstractMmException(String message) {
        super(message)
    }
}
