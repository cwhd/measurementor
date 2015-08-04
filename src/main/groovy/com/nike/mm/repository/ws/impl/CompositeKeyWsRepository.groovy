package com.nike.mm.repository.ws.impl

import com.nike.mm.repository.ws.ICompositeKeyWsRepository
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Repository

/**
 * Created by cdav18 on 8/4/15.
 */
@Slf4j
@Repository
class CompositeKeyWsRepository implements ICompositeKeyWsRepository {

    Object getAllPeople() {
        log.debug("getting all people...")
        //TODO
        //go to elasticsearch
        //get all the people using the query i figured out
        //return the list...
    }

    Object getAllProjectsForPeople() {
        log.debug("getting all projects for people...")
        return null
    }

    Object getAllReposForPeople() {
        log.debug("getting all repos for people...")
        return null
    }
}
