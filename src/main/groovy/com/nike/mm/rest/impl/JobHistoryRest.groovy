package com.nike.mm.rest.impl

import com.nike.mm.dto.JobHistoryDto
import com.nike.mm.facade.IJobHistoryFacade
import com.nike.mm.rest.IJobHistoryRest
import com.nike.mm.rest.assembler.JobHistoryResourceAssembler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.PagedResources
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/jobs-history")
class JobHistoryRest implements IJobHistoryRest {

    @Autowired
    IJobHistoryFacade jobHistoryFacade

    @Autowired
    JobHistoryResourceAssembler jobHistoryResourceAssembler

    @Override
    @RequestMapping(value = "/{jobid}", method = RequestMethod.GET, produces = "application/json")
    PagedResources<JobHistoryDto> pageThrough(
            @PathVariable final String jobid, final Pageable pageable, final PagedResourcesAssembler assembler) {
        Page<JobHistoryDto> dtos = this.jobHistoryFacade.findAllByJobid(jobid, pageable)
        return assembler.toResource(dtos, this.jobHistoryResourceAssembler);
    }
}
