package com.nike.mm.business.plugins.impl

import com.nike.mm.business.plugins.IMeasureMentorBusiness
import org.apache.commons.lang3.StringUtils

import java.text.MessageFormat


abstract class AbstractBusiness implements IMeasureMentorBusiness {

    /**
     * Comma constant
     */
    static final String COMMA = ","

    /**
     * Error message when url is missing
     */
    static final String MISSING_URL = "Missing url"

    /**
     * Prefix a given String with the plugin type
     * @param message
     * @return
     */
    String prefixWithType(def message) {
        return MessageFormat.format("{0}: {1}", type(), message)
    }

    /**
     * Format a list of error messages to be sent back to the caller of the web service.
     * @param errorMessages
     * @return
     */
    def buildValidationErrorString(def errorMessages) {
        StringBuilder sb = new StringBuilder()
        errorMessages.each { def errorMessage ->
            if (sb.size()) {
                sb.append(COMMA)
                sb.append(StringUtils.SPACE)
            }
            sb.append(prefixWithType(errorMessage))
        }
        sb.toString()
    }
}
