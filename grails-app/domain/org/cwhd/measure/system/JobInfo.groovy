package org.cwhd.measure.system

class JobInfo {
    static mapWith = "mongo"

    String dataSource   //this would be like JIRA, Stash, GitHub, etc
    String dataRoot     //the root of the data, i.e. ACOE (JIRA project) or BP (Stash project name)

    static constraints = {
    }
}
