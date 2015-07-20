package com.nike.mm.entity.plugins

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldIndex
import org.springframework.data.elasticsearch.annotations.FieldType


@Document(indexName = "measurementor-jenkins", type = "jenkins")
class Jenkins {

    @Id
    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String id

    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String dataType

    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String buildId

    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String couchId

    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String buildName

    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String result

    @Field(type = FieldType.Date,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    Date timestamp

    @Field(type = FieldType.Integer,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    int duration

    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String lastBuiltRevision

    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String jenkinsUrl

    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String remoteUrl    //TODO this looks like a stash repo, i should be able to tie this back to SCM with a better name...

    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String causedBy

    @Field(type = FieldType.Integer,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    int buildNumber
}
