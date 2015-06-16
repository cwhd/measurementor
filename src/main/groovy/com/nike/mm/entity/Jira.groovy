package com.nike.mm.entity

import java.util.Date;

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "measurementor", type = "github")
class Jira {

	@Id
	String id;
	
	String couchId
	String key
	Date created
	String createdBy
	String issuetype
	int movedForward
	int movedBackward
	int storyPoints
	String[] assignees //this is actually a map of stuff
	String[] tags
	String dataType
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
