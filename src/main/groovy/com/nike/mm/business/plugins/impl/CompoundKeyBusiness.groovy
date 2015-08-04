package com.nike.mm.business.plugins.impl

import com.nike.mm.business.plugins.IMeasureMentorBusiness
import com.nike.mm.dto.JobRunResponseDto
import com.nike.mm.entity.plugins.CompoundKey
import com.nike.mm.repository.es.plugins.ICompositeKeyEsRepository
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by cdav18 on 8/4/15.
 */
class CompoundKeyBusiness implements IMeasureMentorBusiness {

    @Autowired
    ICompositeKeyEsRepository compositeKeyEsRepository

    String type() {
        return "Compound Key"
    }

    String validateConfig(Object config) {
        return null
    }

    JobRunResponseDto updateDataWithResponse(Date defaultFromDate, Object configInfo) {
        //TODO
        //get all the names
        //for each name, get JIRA and Stash data
        //once i have that, build the key and save it back to elastic
        List<CompoundKey> key = compositeKeyEsRepository.findKeysByPeople(null)

        return null
    }
}
