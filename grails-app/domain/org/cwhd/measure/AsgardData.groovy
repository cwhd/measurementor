package org.cwhd.measure

class AsgardData {
    static mapWith = "mongo"
    static searchable = true

    String applicationName

    static constraints = {
        applicationName nullable: true
    }
}
