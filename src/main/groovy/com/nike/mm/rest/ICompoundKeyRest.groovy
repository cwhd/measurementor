package com.nike.mm.rest

import com.nike.mm.entity.plugins.CompoundKey
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.PagedResources

/**
 * Created by cdav18 on 8/4/15.
 */
public interface ICompoundKeyRest {

    List<CompoundKey> pageThrough();

}