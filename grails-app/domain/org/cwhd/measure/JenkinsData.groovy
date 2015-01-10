package org.cwhd.measure

class JenkinsData {
    static mapWith = "mongo"
    static searchable = true

    String buildName

    static constraints = {
        buildName nullable: true
    }
}
