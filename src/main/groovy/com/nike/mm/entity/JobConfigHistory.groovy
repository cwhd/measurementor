package com.nike.mm.entity

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document


@Document(indexName = "measurementor", type="jobconfighistory")
class JobConfigHistory {

    @Id
    String id
    String jobid
    String jobHistoryid
    String type
    Date startDate
    Date endDate
    Integer recordsCount
    JobHistory.Status status
    String comments
}
