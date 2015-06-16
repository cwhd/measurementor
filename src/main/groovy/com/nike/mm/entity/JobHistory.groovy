package com.nike.mm.entity

import java.util.Date;

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "measurementor", type = "jobhistory")
class JobHistory {

	@Id
	String id
	String jobid
	Date startDate
	Date endDate
	boolean success
	String status
	String comments
}
