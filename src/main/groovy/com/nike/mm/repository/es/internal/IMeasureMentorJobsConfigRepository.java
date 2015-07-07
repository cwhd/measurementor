package com.nike.mm.repository.es.internal;

import com.nike.mm.entity.internal.MeasureMentorJobsConfig;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface IMeasureMentorJobsConfigRepository extends ElasticsearchRepository<MeasureMentorJobsConfig, String> {

    MeasureMentorJobsConfig findByName(String name);
}
