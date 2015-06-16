package com.nike.mm.rest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;

import com.nike.mm.dto.JobHistoryDto;

public interface IJobHistoryRest {

    PagedResources<JobHistoryDto> pageThrough(String jobid, Pageable pageable, PagedResourcesAssembler assembler);
}
