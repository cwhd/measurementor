package com.nike.mm.entity.plugins

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldIndex
import org.springframework.data.elasticsearch.annotations.FieldType


@Document(indexName = "measurementor", type = "jirahistory")
class JiraHistory {

    @Id
    @Field(type = FieldType.Date,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String id
    Date timestamp
    String changedBy
    String changeField
    String newValue
    String key
    String sourceId
    String dataType
}
