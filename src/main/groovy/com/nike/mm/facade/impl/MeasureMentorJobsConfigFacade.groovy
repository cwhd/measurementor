package com.nike.mm.facade.impl

import com.nike.mm.service.ICronService
import org.jasypt.util.text.StrongTextEncryptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

import com.nike.mm.business.internal.IJobHistoryBusiness
import com.nike.mm.business.internal.IMeasureMentorJobsConfigBusiness
import com.nike.mm.business.internal.IMeasureMentorRunBusiness
import com.nike.mm.dto.MeasureMentorJobsConfigDto
import com.nike.mm.entity.JobHistory
import com.nike.mm.entity.MeasureMentorJobsConfig
import com.nike.mm.facade.IMeasureMentorJobsConfigFacade

@Service
class MeasureMentorJobsConfigFacade implements IMeasureMentorJobsConfigFacade {

    @Autowired
    IMeasureMentorJobsConfigBusiness measureMentorJobsConfigBusiness;

    @Autowired
    IJobHistoryBusiness jobHistoryBusiness

    @Autowired
    IMeasureMentorRunBusiness measureMentorRunBusiness

    @Autowired
    ICronService cronService

    @Autowired StrongTextEncryptor strongTextEncryptor;

    @Override
    MeasureMentorJobsConfigDto findById(final String id) {
        return this.measureMentorJobsConfigBusiness.findById(id);
    }

    @Override
    Object saveJobsConfig(MeasureMentorJobsConfigDto dto) {
        Object result = this.measureMentorJobsConfigBusiness.saveConfig(dto)
        this.cronService.processJob(dto.id)
        return result
    }

    @Override
    Page<MeasureMentorJobsConfigDto> findListOfJobs(final Pageable pageable) {
        Page rpage = this.measureMentorJobsConfigBusiness.findAll(pageable);
        List<MeasureMentorJobsConfigDto> dtos = [];
        for (MeasureMentorJobsConfig entity : rpage.content) {

            String configString = ""
            if (entity.encryptedConfig) {
                this.strongTextEncryptor.decrypt(new String(Base64.getDecoder().decode(entity.encryptedConfig)))
            }
            MeasureMentorJobsConfigDto dto = [
                    id: entity.id,
                    name: entity.name,
                    jobOn: entity.jobOn,
                    cron: entity.cron,
                    config: configString
            ] as MeasureMentorJobsConfigDto

            JobHistory jh = jobHistoryBusiness.findJobsLastBuildStatus(entity.id);
            if (jh != null) {
                dto.lastbuildstatus = jh.status.toString()
                dto.lastBuildDate = jh.endDate
            }
            dtos.add(dto)
        }
        return new PageImpl(dtos, pageable, rpage.getTotalElements())
    }
}
