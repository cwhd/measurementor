package com.nike.mm.entity.plugins

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldIndex
import org.springframework.data.elasticsearch.annotations.FieldType

@Document(indexName = "measurementor-jira-aggregated", type = "jira")
class Jira {

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
	String couchId

	@Field(type = FieldType.String,
			index = FieldIndex.analyzed,
			searchAnalyzer = "standard",
			indexAnalyzer = "standard",
			store = true)
	String key

	@Field(type = FieldType.Date,
			index = FieldIndex.analyzed,
			searchAnalyzer = "standard",
			indexAnalyzer = "standard",
			store = true)
	Date created

	@Field(type = FieldType.String,
			index = FieldIndex.analyzed,
			searchAnalyzer = "standard",
			indexAnalyzer = "standard",
			store = true)
	String createdBy

	@Field(type = FieldType.String,
			index = FieldIndex.analyzed,
			searchAnalyzer = "standard",
			indexAnalyzer = "standard",
			store = true)
	String issuetype

	@Field(type = FieldType.Integer,
			index = FieldIndex.analyzed,
			searchAnalyzer = "standard",
			indexAnalyzer = "standard",
			store = true)
	int movedForward

	@Field(type = FieldType.Integer,
			index = FieldIndex.analyzed,
			searchAnalyzer = "standard",
			indexAnalyzer = "standard",
			store = true)
	int movedBackward

	@Field(type = FieldType.Integer,
			index = FieldIndex.analyzed,
			searchAnalyzer = "standard",
			indexAnalyzer = "standard",
			store = true)
	int storyPoints

	@Field(type = FieldType.String,
			index = FieldIndex.analyzed,
			searchAnalyzer = "standard",
			indexAnalyzer = "standard",
			store = true)
	String[] assignees //this is actually a map of stuff

	@Field(type = FieldType.String,
			index = FieldIndex.analyzed,
			searchAnalyzer = "standard",
			indexAnalyzer = "standard",
			store = true)
	String[] tags

	@Field(type = FieldType.String,
			index = FieldIndex.analyzed,
			searchAnalyzer = "standard",
			indexAnalyzer = "standard",
			store = true)
	String dataType

	@Field(type = FieldType.Date,
			index = FieldIndex.analyzed,
			searchAnalyzer = "standard",
			indexAnalyzer = "standard",
			store = true)
	Date finished

	@Field(type = FieldType.Long,
			index = FieldIndex.analyzed,
			searchAnalyzer = "standard",
			indexAnalyzer = "standard",
			store = true)
	long leadTime

	@Field(type = FieldType.Long,
			index = FieldIndex.analyzed,
			searchAnalyzer = "standard",
			indexAnalyzer = "standard",
			store = true)
	long devTime

	@Field(type = FieldType.Integer,
			index = FieldIndex.analyzed,
			searchAnalyzer = "standard",
			indexAnalyzer = "standard",
			store = true)
	int commentCount

	@Field(type = FieldType.String,
			index = FieldIndex.analyzed,
			searchAnalyzer = "standard",
			indexAnalyzer = "standard",
			store = true)
	String jiraProject

	@Field(type = FieldType.Integer,
			index = FieldIndex.analyzed,
			searchAnalyzer = "standard",
			indexAnalyzer = "standard",
			store = true)
	int estimateHealth

	@Field(type = FieldType.Long,
			index = FieldIndex.analyzed,
			searchAnalyzer = "standard",
			indexAnalyzer = "standard",
			store = true)
	long rawEstimateHealth

	@Field(type = FieldType.String,
			index = FieldIndex.analyzed,
			searchAnalyzer = "standard",
			indexAnalyzer = "standard",
			store = true)
	String[] components

	@Field(type = FieldType.String,
			index = FieldIndex.analyzed,
			searchAnalyzer = "standard",
			indexAnalyzer = "standard",
			store = true)
	String product
}
