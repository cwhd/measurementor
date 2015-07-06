package com.nike.mm.facade;

import com.nike.mm.dto.MeasureMentorJobsConfigDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMeasureMentorJobsConfigFacade {

    /**
     * Save a job configuration instance
     * @param dto - MeasureMentorJobsConfigDto instance
     * @return - Persisted MeasureMentorJobsConfigDto
     */
    Object saveJobsConfig(MeasureMentorJobsConfigDto dto);

    /**
     * Retrieve a paginated list of jobs configuration
     * @param pageable - Pagination parameters
     * @return - Paginated list of job configurations
     */
    Page<MeasureMentorJobsConfigDto> findListOfJobs(Pageable pageable);

    /**
     * Retrieve a specific instance of MeasureMentorJobsConfig
     * @param id - Unique ID
     * @return - MeasureMentorJobsConfigDto instance or null if no entry found
     */
    MeasureMentorJobsConfigDto findById(String id);
}
