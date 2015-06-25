package com.nike.mm.repository.es.plugins

import com.nike.mm.entity.JiraHistory
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
 * Created by rparr2 on 6/24/15.
 */
interface IJiraHistoryEsRepository extends ElasticsearchRepository<JiraHistory, String> {

}