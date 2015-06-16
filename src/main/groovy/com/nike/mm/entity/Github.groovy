package com.nike.mm.entity

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "measurementor", type = "github")
class Github {
	
	@Id
	private String id;

	String key
	String sha
	Date created
	String author
	String repo
	String scmAction // "pull-request",
	int linesAdded
	int linesRemoved
	int commentCount

}
