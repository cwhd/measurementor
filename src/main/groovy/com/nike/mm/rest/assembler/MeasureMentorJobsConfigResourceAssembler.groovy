package com.nike.mm.rest.assembler

import com.nike.mm.dto.MeasureMentorJobsConfigDto
import com.nike.mm.rest.impl.MeasureMentorJobsConfigRest
import org.springframework.hateoas.Resource
import org.springframework.hateoas.mvc.ResourceAssemblerSupport
import org.springframework.stereotype.Component

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo

@Component
class MeasureMentorJobsConfigResourceAssembler extends ResourceAssemblerSupport<MeasureMentorJobsConfigDto, Resource>{

	
	MeasureMentorJobsConfigResourceAssembler() {
		super(MeasureMentorJobsConfigRest.class, Resource.class)
	}
	
	@Override
	public Resource toResource(MeasureMentorJobsConfigDto entity) {
		return new Resource<MeasureMentorJobsConfigDto>(entity, linkTo(MeasureMentorJobsConfigRest.class).slash(entity.getId()).withSelfRel());
	}

	@Override
    public List<Resource> toResources(Iterable<? extends MeasureMentorJobsConfigDto> entities) {
        List<Resource> resources = new ArrayList<Resource>();
        for(MeasureMentorJobsConfigDto entity : entities) {
            resources.add(new Resource<MeasureMentorJobsConfigDto>(entity, linkTo(MeasureMentorJobsConfigRest.class).slash(entity.getId()).withSelfRel()));
        }
        return resources;
    }
}
