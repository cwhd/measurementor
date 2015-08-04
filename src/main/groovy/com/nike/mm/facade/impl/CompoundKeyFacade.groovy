package com.nike.mm.facade.impl

import com.nike.mm.entity.plugins.CompoundKey
import com.nike.mm.facade.ICompoundKeyFacade
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

/**
 * Created by cdav18 on 8/4/15.
 */
@Service
class CompoundKeyFacade implements ICompoundKeyFacade {

    List<CompoundKey> findAllByPeople(String person, Pageable pageable) {
        final List<CompoundKey> dtos = []
        for(int i=0;i<5;i++) {
            dtos.add(
                    new CompoundKey(people: "chris_davis", jiraProject: "ACOE", repo: "doohiggy", key: i)
            )
        }

        return dtos
    }
}
