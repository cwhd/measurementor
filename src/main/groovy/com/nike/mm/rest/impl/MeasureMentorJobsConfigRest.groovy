package com.nike.mm.rest.impl

import com.nike.mm.dto.MeasureMentorJobsConfigDto
import com.nike.mm.facade.IMeasureMentorJobsConfigFacade
import com.nike.mm.rest.IMeasureMentorJobsConfigRest
import com.nike.mm.rest.assembler.MeasureMentorJobsConfigResourceAssembler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.PagedResources
import org.springframework.web.bind.annotation.*

import javax.validation.Valid

@RestController
@RequestMapping("/api/jobs-config")
class MeasureMentorJobsConfigRest implements IMeasureMentorJobsConfigRest {

    @Autowired
    IMeasureMentorJobsConfigFacade measureMentorJobsConfigFacade

    @Autowired
    MeasureMentorJobsConfigResourceAssembler measureMentorJobsConfigResourceAssembler;

    //http://localhost:8080/api/jobs-config?page=2&size=2&sort=name,desc
    @Override
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public PagedResources<MeasureMentorJobsConfigDto> pageThroughJobConfigs(
            final Pageable pageable, final PagedResourcesAssembler assembler) {
        Page<MeasureMentorJobsConfigDto> dtos = this.measureMentorJobsConfigFacade.findListOfJobs(pageable);
        return assembler.toResource(dtos, this.measureMentorJobsConfigResourceAssembler);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public MeasureMentorJobsConfigDto findById(@PathVariable final String id) {
        return this.measureMentorJobsConfigFacade.findById(id);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public MeasureMentorJobsConfigDto save(@RequestBody @Valid MeasureMentorJobsConfigDto dto) {
        return this.measureMentorJobsConfigFacade.saveJobsConfig(dto);
    }
}
