package org.cwhd.measure

class JenkinsData {
    static mapWith = "mongo"
    static searchable = true

    String dataType
    String buildId
    String couchId
    String buildName
    String result
    Date timestamp
    int duration
    String lastBuiltRevision
    String jenkinsUrl
    String remoteUrl    //TODO this looks like a stash repo, i should be able to tie this back to SCM with a better name...
    String causedBy
    int buildNumber
    //changeset is an interesting piece of data...:
    //maybe i can tie it back with remoteUrls[] ?
    //changeset.items[].author - though this might not tie back to a user somewhere else...

    static constraints = {
        buildName nullable: true
        buildId nullable: true
        result nullable: true
        timestamp nullable: true
        duration nullable: true
        lastBuiltRevision nullable: true
        jenkinsUrl nullable: true
        buildNumber nullable: true
        remoteUrl nullable: true
        causedBy nullable: true
        couchId nullable: true
        dataType nullable: true
    }
}
