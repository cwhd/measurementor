package com.nike.mm.repository.es.internal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.nike.mm.entity.JobHistory;

public interface IJobHistoryRepository extends ElasticsearchRepository<JobHistory, Long> {

    Page<JobHistory> findByJobidAndStatus(String jobid, String status, Pageable pageable);

    Page<JobHistory> findByJobidAndSuccess(String jobid, boolean success, Pageable pageable);

    Page<JobHistory> findByJobid(String jobid, Pageable pageable);
}
