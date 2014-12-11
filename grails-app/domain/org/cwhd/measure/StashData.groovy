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
    }
}
