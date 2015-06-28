package com.nike.mm.entity

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType

/**
 * Created by rparr2 on 6/24/15.
 */
@Document(indexName = "measurementor", type = "jirahistory")
class JiraHistory {

    @Id
    String id
    @Field(type = FieldType.Date)
    Date timestamp
    String changedBy
    String changeField
    String newValue
    String key
    String sourceId
    String dataType
    String issueType
}
