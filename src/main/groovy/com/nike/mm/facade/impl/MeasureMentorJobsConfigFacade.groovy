package com.nike.mm.facade.impl

import com.nike.mm.service.ICronService
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.jasypt.util.text.TextEncryptor
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
@Slf4j
class MeasureMentorJobsConfigFacade implements IMeasureMentorJobsConfigFacade {

    @Autowired
    IMeasureMentorJobsConfigBusiness measureMentorJobsConfigBusiness;

    @Autowired
    IJobHistoryBusiness jobHistoryBusiness

    @Autowired
    IMeasureMentorRunBusiness measureMentorRunBusiness

    @Autowired
    ICronService cronService

    @Autowired TextEncryptor textEncryptor;

    @Override
    MeasureMentorJobsConfigDto findById(final String id) {
        return this.measureMentorJobsConfigBusiness.findById(id);
    }

    @Override
    MeasureMentorJobsConfigDto saveJobsConfig(MeasureMentorJobsConfigDto dto) {

        // persist changes
        MeasureMentorJobsConfig entity = this.measureMentorJobsConfigBusiness.saveConfig(dto)

        // create/update cron job
        this.cronService.processJob(entity.id)

        return this.toDto(entity)
    }

    @Override
    Page<MeasureMentorJobsConfigDto> findListOfJobs(final Pageable pageable) {
        Page rpage = this.measureMentorJobsConfigBusiness.findAll(pageable);
        List<MeasureMentorJobsConfigDto> dtos = [];
        for (MeasureMentorJobsConfig entity : rpage.content) {

            MeasureMentorJobsConfigDto dto = this.toDto(entity)

            JobHistory jh = jobHistoryBusiness.findJobsLastBuildStatus(entity.id);
            if (jh != null) {
                dto.lastbuildstatus = jh.status
                dto.lastBuildDate = jh.endDate
            }
            dtos.add(dto)
        }
        log.debug("Returning list of job configurations: {} of {}", dtos.size(), rpage.getTotalElements())
        return new PageImpl(dtos, pageable, rpage.getTotalElements())
    }

    /**
     * Transform a MeasureMentorJobsConfig instance to a DTO
     * @param entity
     * @return MeasureMentorJobsConfigDto instance
     */
    private MeasureMentorJobsConfigDto toDto(MeasureMentorJobsConfig entity) {

        MeasureMentorJobsConfigDto dto = [
                id: entity.id,
                name: entity.name,
                jobOn: entity.jobOn,
                cron: entity.cron,
                config: new JsonSlurper().parseText(this.textEncryptor.decrypt(new String(Base64.getDecoder().decode(entity.encryptedConfig))))
        ] as MeasureMentorJobsConfigDto
    }
}
