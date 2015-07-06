package com.nike.mm.rest.assembler

import com.nike.mm.dto.JobHistoryDto
import com.nike.mm.rest.impl.JobHistoryRest
import org.springframework.hateoas.Resource
import org.springframework.hateoas.mvc.ResourceAssemblerSupport
import org.springframework.stereotype.Component

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo

@Component
class JobHistoryResourceAssembler extends ResourceAssemblerSupport<JobHistoryDto, Resource>{

	
	JobHistoryResourceAssembler() {
		super(JobHistoryRest.class, Resource.class)
	}
	
	@Override
	public Resource toResource(final JobHistoryDto entity) {
		return new Resource<JobHistoryDto>(entity, linkTo(JobHistoryRest.class).slash(entity.getId()).withSelfRel());
	}

	@Override
    public List<Resource> toResources(final Iterable<? extends JobHistoryDto> entities) {
        final List<Resource> resources = new ArrayList<Resource>();
        for(final JobHistoryDto entity : entities) {
            resources.add(new Resource<JobHistoryDto>(entity, linkTo(JobHistoryRest.class).slash(entity.getId()).withSelfRel()));
        }
        return resources;
    }
}
