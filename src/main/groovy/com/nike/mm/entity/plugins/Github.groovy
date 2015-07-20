package com.nike.mm.entity.plugins

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldIndex
import org.springframework.data.elasticsearch.annotations.FieldType

@Document(indexName = "measurementor-github", type = "github")
class Github {
	
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
	String key

	@Field(type = FieldType.String,
			index = FieldIndex.analyzed,
			searchAnalyzer = "standard",
			indexAnalyzer = "standard",
			store = true)
	String sha

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
	String author

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

}
