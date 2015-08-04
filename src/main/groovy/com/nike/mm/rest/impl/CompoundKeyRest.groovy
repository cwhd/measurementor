package com.nike.mm.rest.impl

import com.nike.mm.entity.plugins.CompoundKey
import com.nike.mm.facade.ICompoundKeyFacade
import com.nike.mm.rest.ICompoundKeyRest
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.PagedResources
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by cdav18 on 8/4/15.
 */
@Slf4j
@RestController
@RequestMapping("/api/compound-key")
class CompoundKeyRest implements ICompoundKeyRest {

    @Autowired
    ICompoundKeyFacade compoundKeyFacade

    @Override
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public List<CompoundKey> pageThrough() {
        println "in the controller again..."
        List<CompoundKey> dtos = this.compoundKeyFacade.findAllByPeople(null, null)
        println dtos
        return dtos

    }
}
