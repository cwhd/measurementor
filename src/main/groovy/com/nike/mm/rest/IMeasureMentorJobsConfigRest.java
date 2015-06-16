package com.nike.mm.rest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;

import com.nike.mm.dto.MeasureMentorJobsConfigDto;

/**
 * API for publicly working with the configuration of Jobs in the MeasureMentor.
 *
 */
public interface IMeasureMentorJobsConfigRest {

    PagedResources<MeasureMentorJobsConfigDto> pageThroughJobConfigs(Pageable pageable, PagedResourcesAssembler assembler);

    MeasureMentorJobsConfigDto findById(final String id);

    Object save(MeasureMentorJobsConfigDto dto);
}
