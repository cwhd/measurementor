package com.nike.mm.facade

import com.nike.mm.entity.plugins.CompoundKey
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * Created by cdav18 on 8/4/15.
 */
public interface ICompoundKeyFacade {

    List<CompoundKey> findAllByPeople(String person, Pageable pageable);

}