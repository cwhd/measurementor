package com.nike.mm.entity.internal

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldIndex
import org.springframework.data.elasticsearch.annotations.FieldType

@Document(indexName = "measurementor", type = "jobhistory")
class JobHistory {

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
	String jobid

	@Field(type = FieldType.Date,
			index = FieldIndex.analyzed,
			searchAnalyzer = "standard",
			indexAnalyzer = "standard",
			store = true)
	Date startDate

	@Field(type = FieldType.Date,
			index = FieldIndex.analyzed,
			searchAnalyzer = "standard",
			indexAnalyzer = "standard",
			store = true)
	Date endDate

	@Field(type = FieldType.String,
			index = FieldIndex.analyzed,
			searchAnalyzer = "standard",
			indexAnalyzer = "standard",
			store = true)
	Status status

	@Field(type = FieldType.String,
			index = FieldIndex.analyzed,
			searchAnalyzer = "standard",
			indexAnalyzer = "standard",
			store = true)
	String comments

	enum Status {
		success,
		error
	}
}
