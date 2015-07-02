package com.nike.mm.entity

import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType

import java.util.Date;

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "measurementor", type = "jira")
class Jira {

	@Id
	String id
	
	String couchId
	String key
	@Field(type = FieldType.Date)
	Date created
	String createdBy
	String issuetype
	int movedForward
	int movedBackward
	Integer recidivism
	String[] fixedVersions
	int storyPoints
	String[] assignees //this is actually a map of stuff
	String[] tags
	String dataType
	@Field(type = FieldType.Date)
	Date finished
	long leadTime
	long devTime
	int commentCount
	String jiraProject
	int estimateHealth
	long rawEstimateHealth
	String[] components
	String product
}
