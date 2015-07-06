package com.nike.mm.business.internal.impl

import com.nike.mm.business.internal.IMeasureMentorJobsConfigBusiness
import com.nike.mm.entity.MeasureMentorJobsConfig
import com.nike.mm.repository.es.internal.IMeasureMentorJobsConfigRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class MeasureMentorJobsConfigBusiness implements IMeasureMentorJobsConfigBusiness {

    @Autowired
    IMeasureMentorJobsConfigRepository measureMentorConfigRepository

    @Override
    Page<MeasureMentorJobsConfig> findAll(final Pageable pageable) {
        return this.measureMentorConfigRepository.findAll(pageable)
    }

    @Override
    MeasureMentorJobsConfig findById(final String id) {
        return this.measureMentorConfigRepository.findOne(id)
    }

    @Override
    MeasureMentorJobsConfig saveConfig(final MeasureMentorJobsConfig entity) {
        return this.measureMentorConfigRepository.save(entity)
    }
}
