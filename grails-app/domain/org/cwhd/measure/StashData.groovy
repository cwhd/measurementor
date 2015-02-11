package org.cwhd.measure

class StashData {
    static mapWith = "mongo"
    static searchable = true

    String key
    Date created
    Date updated
    String author
    String[] reviewers
    String stashProject
    String repo
    String scmAction // "pull-request",
    String dataType
    String[] parents
    int linesAdded
    int linesRemoved
    int commentCount
    String couchId
    //TODO need to get these up and running
    double timeOpen //how long something is open.  if this is closed, it's the difference between the createdDate and updatedDate.  If
    //it's not closed it's the length of time between createdDate and today
    String state //this would be MERGED for example
    int commitCount

    static constraints = {
        created nullable: true
        updated nullable: true
        author nullable: true
        reviewers nullable: true
        stashProject nullable: true
        repo nullable: true
        scmAction nullable: true
        dataType nullable: true
        parents nullable: true
        linesAdded nullable: true
        linesRemoved nullable: true
        commentCount nullable: true
        couchId nullable: true
        timeOpen nullable: true
        state nullable: true
        commitCount nullable: true
    }
}
