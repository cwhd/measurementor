package com.nike.mm.repository.ws.impl

import com.nike.mm.dto.HttpRequestDto
import com.nike.mm.entity.plugins.CompoundKey
import com.nike.mm.repository.ws.ICompositeKeyWsRepository
import com.nike.mm.service.IHttpRequestService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * Created by cdav18 on 8/4/15.
 */
@Slf4j
@Repository
class CompositeKeyWsRepository implements ICompositeKeyWsRepository {

    @Autowired IHttpRequestService httpRequestService

    List<CompoundKey> getAllPeople() {
        log.debug("getting all people...")
        //TODO
        //go to elasticsearch
        //get all the people using the query i figured out
        //return the list...

        def json = this.httpRequestService.postSomething("URL HERE", ["size": 0, "aggs": ["peeps": ["terms": ["field":"people","size": 100]]]])
        println json
        def peeps = []
        for (def peep : json.aggregations.peeps.buckets) {
            peeps.add(peep.key)
        }
        println "------------------------------"
        println "ALL PEOPLE"
        println "------------------------------"
        println peeps
        return peeps
    }

    List<CompoundKey> getAllProjectsForPeople() {
        log.debug("getting all projects for people...")
        return null
    }

    List<CompoundKey> getAllReposForPeople() {
        log.debug("getting all repos for people...")
        return null
    }
}
