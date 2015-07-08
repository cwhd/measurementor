package com.nike.mm.rest;

import com.nike.mm.dto.MeasureMentorJobsConfigDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;

/**
 * API for publicly working with the configuration of Jobs in the MeasureMentor.
 *
 */
public interface IMeasureMentorJobsConfigRest {

    /**
     * Retrieve a paginated list of jobs
     * @param pageable - Pagination parameters
     * @return - Paginated list of job configurations
     */
    PagedResources<MeasureMentorJobsConfigDto> pageThroughJobConfigs(Pageable pageable, PagedResourcesAssembler assembler);

    /**
     * Retrieve a specific instance of MeasureMentorJobsConfig
     * @param id - Unique ID
     * @return - MeasureMentorJobsConfigDto instance or null if no entry found
     */
    MeasureMentorJobsConfigDto findById(String id);

    /**
     * Save a job configuration instance
     * @param dto - MeasureMentorJobsConfigDto instance
     * @return - Persisted MeasureMentorJobsConfigDto
     */
    MeasureMentorJobsConfigDto save(MeasureMentorJobsConfigDto dto);
}
