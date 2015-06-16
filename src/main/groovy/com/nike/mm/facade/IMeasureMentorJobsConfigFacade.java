package com.nike.mm.facade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nike.mm.dto.MeasureMentorJobsConfigDto;

public interface IMeasureMentorJobsConfigFacade {

    Object saveJobsConfig(MeasureMentorJobsConfigDto dto);

    Page<MeasureMentorJobsConfigDto> findListOfJobs(Pageable pageable);

    MeasureMentorJobsConfigDto findById(String id);
}
