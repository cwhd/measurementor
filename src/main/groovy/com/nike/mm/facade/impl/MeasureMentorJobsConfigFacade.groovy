package com.nike.mm.facade.impl

import com.nike.mm.business.internal.IJobHistoryBusiness
import com.nike.mm.business.internal.IMeasureMentorJobsConfigBusiness
import com.nike.mm.business.internal.IMeasureMentorRunBusiness
import com.nike.mm.dto.MeasureMentorJobsConfigDto
import com.nike.mm.entity.JobHistory
import com.nike.mm.entity.MeasureMentorJobsConfig
import com.nike.mm.facade.IMeasureMentorJobsConfigFacade
import com.nike.mm.service.ICronService
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.jasypt.util.text.TextEncryptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

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

    @Autowired
    TextEncryptor textEncryptor

    @Override
    MeasureMentorJobsConfigDto findById(final String id) {
        return this.toDto(this.measureMentorJobsConfigBusiness.findById(id))
    }

    @Override
    MeasureMentorJobsConfigDto saveJobsConfig(final MeasureMentorJobsConfigDto dto) {

        // persist changes
        final MeasureMentorJobsConfig entity = this.measureMentorJobsConfigBusiness.saveConfig(parseDto(dto))

        // create/update cron job
        this.cronService.processJob(entity.id)

        return this.toDto(entity)
    }

    @Override
    Page<MeasureMentorJobsConfigDto> findListOfJobs(final Pageable pageable) {
        final Page rpage = this.measureMentorJobsConfigBusiness.findAll(pageable);
        final List<MeasureMentorJobsConfigDto> dtos = [];
        for (final MeasureMentorJobsConfig entity : rpage.content) {

            MeasureMentorJobsConfigDto dto = this.toDto(entity)

            JobHistory jh = jobHistoryBusiness.findJobsLastBuildStatus(entity.id);
            if (jh) {
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
    private MeasureMentorJobsConfigDto toDto(final MeasureMentorJobsConfig entity) {
        MeasureMentorJobsConfigDto dto = null
        if (entity) {
            dto = new MeasureMentorJobsConfigDto(
                    id: entity.id,
                    name: entity.name,
                    jobOn: entity.jobOn,
                    cron: entity.cron,
                    config: new JsonSlurper().parseText(this.textEncryptor.decrypt(new String(Base64.getDecoder().decode
                            (entity.encryptedConfig)))))
        }
        return dto
    }

    /**
     * Transform a MeasureMentorJobsConfigDto instance to a MeasureMentorJobsConfig instance
     * @param dto
     * @return entity
     */
    private MeasureMentorJobsConfig parseDto(final MeasureMentorJobsConfigDto dto) {

        final String configString = new JsonBuilder(dto.config).toPrettyString()
        return new MeasureMentorJobsConfig(id: dto.id,
                name: dto.name,
                cron: dto.cron,
                jobOn: dto.jobOn,
                encryptedConfig: Base64.getEncoder().encodeToString(this.textEncryptor.encrypt(configString).bytes)
        )
    }
}
