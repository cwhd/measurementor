package com.nike.mm.repository.es.plugins

import com.nike.mm.entity.Jenkins
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface IJenkinsEsRepository extends ElasticsearchRepository<Jenkins, String>{

}