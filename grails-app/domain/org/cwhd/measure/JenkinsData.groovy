package org.cwhd.measure

/**
 * as an example check this out
 * https://jenkins.tools.nikecloud.com/job/SQA/job/humulo-staticanalysis/lastBuild/api/json
 */
class JenkinsData {
    static mapWith = "mongo"
    static searchable = true

    //TODO what ties this back to a PTS or SCM project?
    String buildId
    String buildName
    String result
    Date timestamp
    int duration
    String lastBuiltRevision
    String jenkinsUrl
    String remoteUrl
    String causedBy
    int buildNumber //TODO not sure if i need to get this granular of if i should roll this up
    //changeset is an interesting piece of data...:
    //https://jenkins.tools.nikecloud.com/job/SQA/job/qmjobwriter-war/lastBuild/api/json
    //maybe i can tie it back with remoteUrls[] ?  i think that points to where the source repo is...
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
    }
}
