package com.nike.mm.repository.es.internal;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//import org.springframework.stereotype.Repository;

import com.nike.mm.entity.MeasureMentorJobsConfig;

public interface IMeasureMentorJobsConfigRepository extends ElasticsearchRepository<MeasureMentorJobsConfig, String> {

    MeasureMentorJobsConfig findByName(String name);
}
