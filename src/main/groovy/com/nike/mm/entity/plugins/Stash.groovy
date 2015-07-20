package com.nike.mm.entity.plugins

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldIndex
import org.springframework.data.elasticsearch.annotations.FieldType


@Document(indexName = "measurementor-stash", type = "stash")
class Stash {

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
    Date created

    @Field(type = FieldType.Date,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    Date updated

    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String author

    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String[] reviewers

    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String stashProject

    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String repo

    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String scmAction // "pull-request",

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
    String[] parents

    @Field(type = FieldType.Integer,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    int linesAdded

    @Field(type = FieldType.Integer,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    int linesRemoved

    @Field(type = FieldType.Integer,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    int commentCount


    @Field(type = FieldType.Double,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    //TODO need to get these up and running
    double timeOpen //how long something is open.  if this is closed, it's the difference between the createdDate and updatedDate.  If
    //it's not closed it's the length of time between createdDate and today

    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String state //this would be MERGED for example

    @Field(type = FieldType.Integer,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    int commitCount

    @Field(type = FieldType.Date,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    Date referenceDate
}
