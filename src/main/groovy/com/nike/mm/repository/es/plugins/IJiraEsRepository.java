package com.nike.mm.repository.es.plugins;

import com.nike.mm.entity.Jira;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface IJiraEsRepository extends ElasticsearchRepository<Jira, Long> {

}
