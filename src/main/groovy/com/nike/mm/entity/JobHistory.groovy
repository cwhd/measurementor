package com.nike.mm.entity

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType

@Document(indexName = "measurementor", type = "jobhistory")
class JobHistory {

	@Id
	String id
	String jobid
	@Field(type = FieldType.Date)
	Date startDate
	@Field(type = FieldType.Date)
	Date endDate
	Status status
	String comments

	enum Status {
		success,
		error
	}
}
