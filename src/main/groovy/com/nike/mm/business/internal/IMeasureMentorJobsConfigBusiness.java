package com.nike.mm.business.internal;

import com.nike.mm.dto.MeasureMentorJobsConfigDto;
import com.nike.mm.entity.MeasureMentorJobsConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMeasureMentorJobsConfigBusiness {

    MeasureMentorJobsConfig saveConfig(MeasureMentorJobsConfigDto dto);

    MeasureMentorJobsConfigDto findById(final String id);

    Page<MeasureMentorJobsConfig> findAll(Pageable pageable);
}
