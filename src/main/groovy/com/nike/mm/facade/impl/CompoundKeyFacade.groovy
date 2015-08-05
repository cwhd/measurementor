package com.nike.mm.facade.impl

import com.nike.mm.entity.plugins.CompoundKey
import com.nike.mm.facade.ICompoundKeyFacade
import com.nike.mm.repository.ws.ICompositeKeyWsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

/**
 * Created by cdav18 on 8/4/15.
 */
@Service
class CompoundKeyFacade implements ICompoundKeyFacade {

    @Autowired ICompositeKeyWsRepository compositeKeyWsRepository

    List<CompoundKey> findAllByPeople(String person, Pageable pageable) {
        println "in the f..."

        List<CompoundKey> keys = compositeKeyWsRepository.getAllPeople()

        return keys
    }
}
