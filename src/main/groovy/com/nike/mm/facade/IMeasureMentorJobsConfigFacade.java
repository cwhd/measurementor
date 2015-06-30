package com.nike.mm.facade;

import com.nike.mm.dto.MeasureMentorJobsConfigDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMeasureMentorJobsConfigFacade {

    Object saveJobsConfig(MeasureMentorJobsConfigDto dto);

    Page<MeasureMentorJobsConfigDto> findListOfJobs(Pageable pageable);

    MeasureMentorJobsConfigDto findById(String id);
}
