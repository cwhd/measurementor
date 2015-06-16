package org.cwhd.measure

class GithubData {
    static mapWith = "mongo"
    static searchable = true

    String key
    String sha
    Date created
    String author
    String repo
    String scmAction // "pull-request",
    int linesAdded
    int linesRemoved
    int commentCount

    static constraints = {
        key nullable: true
        sha nullable: true
        created nullable: true
        author nullable: true
        repo nullable: true
        scmAction nullable: true
        linesAdded nullable: true
        linesRemoved nullable: true
        commentCount nullable: true
    }
}
