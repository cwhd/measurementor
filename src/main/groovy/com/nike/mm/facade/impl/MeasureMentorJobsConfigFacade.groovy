package com.nike.mm.facade.impl

import com.nike.mm.service.ICronService
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

    @Override
    MeasureMentorJobsConfigDto findById(final String id) {
        return this.measureMentorJobsConfigBusiness.findById(id);
    }

    @Override
    Object saveJobsConfig(MeasureMentorJobsConfigDto dto) {
        Object result = this.measureMentorJobsConfigBusiness.saveConfig(dto)
        this.cronService.processJob(dto.id)
//        if (dto.cron && dto.jobOn) {
//            this.cronService.addJob(dto.id)
//        } else {
//            this.cronService.removeJob(dto.id)
//        }

        return result
    }

    @Override
    Page<MeasureMentorJobsConfigDto> findListOfJobs(final Pageable pageable) {
        Page rpage = this.measureMentorJobsConfigBusiness.findAll(pageable);
        List<MeasureMentorJobsConfigDto> dtos = [];
        for (MeasureMentorJobsConfig entity : rpage.content) {
            JobHistory jh = jobHistoryBusiness.findJobsLastBuildStatus(entity.id);
            MeasureMentorJobsConfigDto dto = null;
            if (jh != null) {
                dto = [id                      : entity.id, name: entity.name, jobOn:
                        entity.jobOn, cron     : entity.cron, lastbuildstatus: jh
                        .success, lastBuildDate: jh.endDate] as
                        MeasureMentorJobsConfigDto
            } else {
                dto = [id: entity.id, name: entity.name, jobOn: entity.jobOn, cron: entity.cron] as
                        MeasureMentorJobsConfigDto
            }
            dtos.add(dto)
        }
        return new PageImpl(dtos, pageable, rpage.getTotalElements())
    }
}
