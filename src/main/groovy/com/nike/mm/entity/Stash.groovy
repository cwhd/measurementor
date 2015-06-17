package com.nike.mm.entity

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

/**
 * Entity for Stash data.
 * Created by rparr2 on 6/16/15.
 */
@Document(indexName = "measurementor", type = "stash")
class Stash {

    @Id
    String id
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
}
