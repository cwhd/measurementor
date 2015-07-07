package com.nike.mm.repository.es.plugins

import com.nike.mm.entity.plugins.Jenkins
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface IJenkinsEsRepository extends ElasticsearchRepository<Jenkins, String>{

}