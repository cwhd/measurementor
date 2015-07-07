package com.nike.mm.entity.plugins

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document


@Document(indexName = "measurementor", type = "jirahistory")
class JiraHistory {

    @Id
    String id
    Date timestamp
    String changedBy
    String changeField
    String newValue
    String key
    String sourceId
    String dataType
}
