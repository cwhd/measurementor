package com.nike.mm.entity.plugins

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldIndex
import org.springframework.data.elasticsearch.annotations.FieldType


@Document(indexName = "measurementor-jira-history", type = "jirahistory")
class JiraHistory {

    @Id
    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String id

    @Field(type = FieldType.Date,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    Date   timestamp

    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String changedBy

    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String changeField

    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String newValue

    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String key

    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String sourceId

    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String dataType
}
