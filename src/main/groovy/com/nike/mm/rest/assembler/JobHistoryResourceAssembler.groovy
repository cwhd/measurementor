package com.nike.mm.rest.assembler

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*

import org.springframework.hateoas.Resource
import org.springframework.hateoas.mvc.ResourceAssemblerSupport
import org.springframework.stereotype.Component

import com.nike.mm.dto.JobHistoryDto
import com.nike.mm.rest.impl.JobHistoryRest

@Component
class JobHistoryResourceAssembler extends ResourceAssemblerSupport<JobHistoryDto, Resource>{

	
	JobHistoryResourceAssembler() {
		super(JobHistoryRest.class, Resource.class)
	}
	
	@Override
	public Resource toResource(JobHistoryDto entity) {
		return new Resource<JobHistoryDto>(entity, linkTo(JobHistoryRest.class).slash(entity.getId()).withSelfRel());
	}

	@Override
    public List<Resource> toResources(Iterable<? extends JobHistoryDto> entities) {
        List<Resource> resources = new ArrayList<Resource>();
        for(JobHistoryDto entity : entities) {
            resources.add(new Resource<JobHistoryDto>(entity, linkTo(JobHistoryRest.class).slash(entity.getId()).withSelfRel()));
        }
        return resources;
    }
}
