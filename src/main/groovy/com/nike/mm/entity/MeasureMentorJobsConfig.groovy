package com.nike.mm.entity

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "measurementor", type = "config")
class MeasureMentorJobsConfig {
	
	@Id
	String id;
	String name;
	boolean jobOn;
	String encryptedConfig;
	String cron;
}
