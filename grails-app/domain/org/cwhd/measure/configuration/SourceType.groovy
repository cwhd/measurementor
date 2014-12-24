package org.cwhd.measure.configuration

class SourceType {
    static mapWith = "mongo"

    String sourceName       //should be key
    SystemName systemName

    //TODO should break this out and add estimate stuff to JIRA
    enum SystemName {
        JIRA, STASH
    }

    static constraints = {
        systemName blank: false
    }
}
