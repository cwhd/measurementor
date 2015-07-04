package com.nike.mm.repository.es.internal;

import com.nike.mm.entity.MeasureMentorJobsConfig;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

//import org.springframework.stereotype.Repository;

public interface IMeasureMentorJobsConfigRepository extends ElasticsearchRepository<MeasureMentorJobsConfig, String> {

    MeasureMentorJobsConfig findByName(String name);
}
