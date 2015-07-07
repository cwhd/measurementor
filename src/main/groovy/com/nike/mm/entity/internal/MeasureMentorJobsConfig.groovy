package com.nike.mm.entity.internal

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldIndex
import org.springframework.data.elasticsearch.annotations.FieldType

@Document(indexName = "measurementor", type = "jobconfig")
class MeasureMentorJobsConfig {

    @Id
    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String id;

    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String name;

    @Field(type = FieldType.Boolean,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    boolean jobOn;

    @Field(type = FieldType.String,
            index = FieldIndex.not_analyzed,
            store = true)
    String encryptedConfig;

    @Field(type = FieldType.String,
            index = FieldIndex.not_analyzed,
            store = true)
    String cron;
}
