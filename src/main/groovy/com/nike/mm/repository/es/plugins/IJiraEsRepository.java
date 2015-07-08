package com.nike.mm.repository.es.plugins;

import com.nike.mm.entity.plugins.Jira;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface IJiraEsRepository extends ElasticsearchRepository<Jira, Long> {

    Page<Jira> findByJiraProject(String jiraProject, Pageable pageable);
}
