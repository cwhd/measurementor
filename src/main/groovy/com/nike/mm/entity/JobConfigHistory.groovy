package com.nike.mm.entity

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType


@Document(indexName = "measurementor", type="jobconfighistory")
class JobConfigHistory {

    @Id
    String id
    String jobid
    String jobHistoryid
    String type
    @Field(type = FieldType.Date)
    Date startDate
    @Field(type = FieldType.Date)
    Date endDate
    Integer recordsCount
    JobHistory.Status status
    String comments
}
