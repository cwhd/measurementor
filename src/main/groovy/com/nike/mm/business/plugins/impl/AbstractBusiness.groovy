package com.nike.mm.business.plugins.impl

import com.nike.mm.business.plugins.IMeasureMentorBusiness

import java.text.MessageFormat


abstract class AbstractBusiness implements IMeasureMentorBusiness {

    String prefixWithType(String message) {
        return MessageFormat.format("{0}: {1}", type(), message)
    }
}
