package org.cwhd.measure.system

class JobHistory {
    static mapWith = "mongo"

    Date startDate
    Long completionTime //this is in minutes
    String jobResult
    String jobNotes
    String projectCount

    static constraints = {
        completionTime nullable: true
        jobResult nullable: true
        jobNotes nullable: true
        projectCount nullable: true
    }
}
