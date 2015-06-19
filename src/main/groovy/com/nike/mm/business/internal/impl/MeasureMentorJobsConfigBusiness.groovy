package com.nike.mm.business.internal.impl

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

import org.jasypt.util.text.StrongTextEncryptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

import com.nike.mm.business.internal.IMeasureMentorJobsConfigBusiness;
import com.nike.mm.dto.MeasureMentorJobsConfigDto
import com.nike.mm.entity.MeasureMentorJobsConfig
import com.nike.mm.repository.es.internal.IMeasureMentorJobsConfigRepository;

@Service
class MeasureMentorJobsConfigBusiness implements IMeasureMentorJobsConfigBusiness {
	
	@Autowired IMeasureMentorJobsConfigRepository measureMentorConfigRepository
	
	@Autowired StrongTextEncryptor strongTextEncryptor;

	@Override Page<MeasureMentorJobsConfig> findAll(Pageable pageable) {
		return this.measureMentorConfigRepository.findAll(pageable);
	}

	@Override MeasureMentorJobsConfigDto findById(final String id) {
		MeasureMentorJobsConfig rmmc = this.measureMentorConfigRepository.findOne(id);
		MeasureMentorJobsConfigDto rdto = null
		if (rmmc) {
			String configString = this.strongTextEncryptor.decrypt(new String(Base64.getDecoder().decode(rmmc.encryptedConfig)))
			rdto = [id: rmmc.id, name: rmmc.name, jobOn: rmmc.jobOn, config: new JsonSlurper().parseText(configString), cron: rmmc.cron] as MeasureMentorJobsConfigDto
		}
		return rdto
	}

	@Override Object saveConfig(MeasureMentorJobsConfigDto dto) {
		String configString = new JsonBuilder(dto.config).toPrettyString();
		MeasureMentorJobsConfig mmjc = null;
		if ( dto.id ) {//jobOn
			mmjc = [id: dto.id, cron: dto.cron, name:dto.name, jobOn:dto.jobOn, encryptedConfig: Base64.getEncoder().encodeToString(this.strongTextEncryptor.encrypt(configString).getBytes())] as MeasureMentorJobsConfig
			
		} else {
			mmjc = [name:dto.name, cron: dto.cron, jobOn:dto.jobOn, encryptedConfig: Base64.getEncoder().encodeToString(this.strongTextEncryptor.encrypt(configString).getBytes())] as MeasureMentorJobsConfig
		}
		return this.measureMentorConfigRepository.save(mmjc)
	}
}
