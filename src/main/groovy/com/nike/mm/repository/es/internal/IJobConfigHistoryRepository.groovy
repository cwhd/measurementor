package com.nike.mm.repository.es.internal

import com.nike.mm.entity.internal.JobConfigHistory
import com.nike.mm.entity.internal.JobHistory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository


interface IJobConfigHistoryRepository extends ElasticsearchRepository<JobConfigHistory, Long> {

    Page<JobConfigHistory> findByJobidAndStatusAndType(String jobid, JobHistory.Status status, String type, Pageable
            pageable)
}
