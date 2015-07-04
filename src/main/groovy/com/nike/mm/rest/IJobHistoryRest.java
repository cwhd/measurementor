package com.nike.mm.rest;

import com.nike.mm.dto.JobHistoryDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;

public interface IJobHistoryRest {

    PagedResources<JobHistoryDto> pageThrough(String jobid, Pageable pageable, PagedResourcesAssembler assembler);
}
