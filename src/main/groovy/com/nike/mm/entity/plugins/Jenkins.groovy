package com.nike.mm.entity.plugins

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

/**
 * Created by rparr2 on 6/22/15.
 */
@Document(indexName = "measurementor", type = "jenkins")
class Jenkins {

    @Id
    String id
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
}
