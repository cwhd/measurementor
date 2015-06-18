package com.nike.mm.facade.impl

import com.nike.mm.service.IDateService
import com.nike.mm.business.internal.IJobHistoryBusiness
import com.nike.mm.business.internal.IMeasureMentorJobsConfigBusiness
import com.nike.mm.business.internal.IMeasureMentorRunBusiness
import com.nike.mm.business.plugins.IMeasureMentorBusiness
import com.nike.mm.dto.MeasureMentorConfigValidationDto
import com.nike.mm.dto.RunnableMeasureMentorBusinessAndConfigDto
import com.nike.mm.entity.JobHistory
import com.nike.mm.facade.IMeasureMentorRunFacade
import groovy.util.logging.Log4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

import java.text.SimpleDateFormat

@Service
@Log4j
class MeasureMentorRunFacade implements IMeasureMentorRunFacade {

    @Autowired
    Set<IMeasureMentorBusiness> measureMentorBusinesses

    @Autowired
    IMeasureMentorJobsConfigBusiness measureMentorConfigBusiness

    @Autowired
    IMeasureMentorRunBusiness measureMentorRunBusiness

    @Autowired
    IJobHistoryBusiness jobHistoryBusiness

    @Autowired
    IDateService dateService

    @Override
    @Async
    void runJobId(String jobid) {

        def startDate = this.dateService.currentDateTime;
        try {
            this.measureMentorRunBusiness.startJob(jobid);
            def configDto = this.measureMentorConfigBusiness.findById(jobid);

            MeasureMentorConfigValidationDto mmcvDto = this.validateTheConfigFilesAndPlugins(configDto.config);
            mmcvDto.configId = configDto.id;

            JobHistory previousJh = this.jobHistoryBusiness.findLastSuccessfulJobRanForJobid(jobid);
            if (previousJh) {
                mmcvDto.previousJobId = previousJh.id;
            }
            if (!mmcvDto.errors.isEmpty()) {
                this.jobHistoryBusiness.save([jobid: jobid, startDate: startDate, endDate: this.dateService
                        .currentDateTime, success:
                        'false', status            : "error", comments: mmcvDto.getMessageAsString()] as JobHistory);
            } else {
                //TODO We need to get agreeget data as well as success monicers from this.
                this.runMmbs(getLastRunDateOrDefault(previousJh), mmcvDto);
                //TODO: Error handling.
                this.jobHistoryBusiness.save([jobid: jobid, startDate: startDate, endDate: this.dateService
                        .currentDateTime, success:
                        false, status              : "success", comments: ("Success for jobid: " + jobid)] as
                        JobHistory);
            }
        } finally {
            this.measureMentorRunBusiness.stopJob(jobid);
        }
    }

    private Date getLastRunDateOrDefault(JobHistory previousJh) {
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1901");
        if (previousJh) {
            date = previousJh.endDate;
        }
        return date;
    }

    private void runMmbs(Date lastRunDate, MeasureMentorConfigValidationDto mmcvDto) {
        //TODO run in parallel.
        //TODO error tracking.
        for (RunnableMeasureMentorBusinessAndConfigDto dto : mmcvDto.runnableMmbs) {
            dto.measureMentorBusiness.updateData(dto.config, lastRunDate)
        }
    }

    private MeasureMentorConfigValidationDto validateTheConfigFilesAndPlugins(def configs) {
        MeasureMentorConfigValidationDto mmcvDto = new MeasureMentorConfigValidationDto();
        configs.each { config ->
            if (config.type) {
                IMeasureMentorBusiness mmb = this.findByType(config.type)
                if (mmb == null) {
                    mmcvDto.errors.add("No measure mentor configured for: $config.type");
                } else if (!mmb.validateConfig(config)) {
                    mmcvDto.errors.add("Config not valid config for: $config.type");
                } else {
                    mmcvDto.runnableMmbs.add([measureMentorBusiness: mmb, config: config] as
                            RunnableMeasureMentorBusinessAndConfigDto)
                }
            } else {
                mmcvDto.errors.add("No config")
            }
        }
        return mmcvDto;
    }

    private IMeasureMentorBusiness findByType(final String type) {
        for (IMeasureMentorBusiness mmb : this.measureMentorBusinesses) {
            if (mmb.type() == type) {
                return mmb;
            }
        }
        return null;
    }
}
