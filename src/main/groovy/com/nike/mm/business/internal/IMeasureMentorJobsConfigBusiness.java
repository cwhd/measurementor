package com.nike.mm.business.internal;

import com.nike.mm.entity.internal.MeasureMentorJobsConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMeasureMentorJobsConfigBusiness {

    MeasureMentorJobsConfig saveConfig(MeasureMentorJobsConfig dto);

    MeasureMentorJobsConfig findById(String id);

    Page<MeasureMentorJobsConfig> findAll(Pageable pageable);
}
