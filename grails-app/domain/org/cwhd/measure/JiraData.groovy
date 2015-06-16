package org.cwhd.measure

class JiraData {
    static mapWith = "mongo"
    static searchable = true

    String couchId
    String key
    Date created
    String createdBy
    String issuetype
    int movedForward
    int movedBackward
    int storyPoints
    String[] assignees //this is actually a map of stuff
    String[] tags
    String dataType
    Date finished
    long leadTime
    long devTime
    int commentCount
    String jiraProject
    int estimateHealth
    long rawEstimateHealth
    String[] components
    String product

    static constraints = {
        couchId nullable: true
        finished nullable: true
        created nullable: true
        createdBy nullable: true
        issuetype nullable: true
        storyPoints nullable: true
        assignees nullable: true
        tags nullable: true
        leadTime nullable: true
        devTime nullable: true
        commentCount nullable: true
        jiraProject nullable: true
        estimateHealth nullable: true
        rawEstimateHealth nullable: true
        components nullable: true
        product nullable: true
    }
}
