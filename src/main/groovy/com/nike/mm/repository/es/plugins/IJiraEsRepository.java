package com.nike.mm.repository.es.plugins;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.nike.mm.entity.Jira;

public interface IJiraEsRepository extends ElasticsearchRepository<Jira, Long> {

}
