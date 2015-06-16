package com.nike.mm.business.internal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nike.mm.dto.MeasureMentorJobsConfigDto;
import com.nike.mm.entity.MeasureMentorJobsConfig;

public interface IMeasureMentorJobsConfigBusiness {

    Object saveConfig(MeasureMentorJobsConfigDto dto);

    MeasureMentorJobsConfigDto findById(final String id);

    Page<MeasureMentorJobsConfig> findAll(Pageable pageable);
}
