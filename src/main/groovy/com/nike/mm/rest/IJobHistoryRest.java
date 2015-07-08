package com.nike.mm.rest;

import com.nike.mm.dto.JobHistoryDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;

public interface IJobHistoryRest {

    /**
     * Retrieve a Paginated history for a given job
     *
     * @param jobid the jobid
     * @param pageable the pageable
     * @param assembler the assembler
     * @return the paged resources
     */
    PagedResources<JobHistoryDto> pageThrough(String jobid, Pageable pageable, PagedResourcesAssembler assembler);
}
