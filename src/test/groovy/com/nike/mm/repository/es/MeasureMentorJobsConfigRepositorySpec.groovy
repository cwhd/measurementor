package com.nike.mm.repository.es

import org.jasypt.util.text.TextEncryptor
import org.junit.After;
import org.junit.Before;
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration

import spock.lang.Specification

import com.nike.mm.MeasurementorApplication
import com.nike.mm.entity.MeasureMentorJobsConfig;
import com.nike.mm.repository.es.internal.IMeasureMentorJobsConfigRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MeasurementorApplication.class)
@WebAppConfiguration
class MeasureMentorJobsConfigRepositorySpec extends Specification {
	
	@Autowired IMeasureMentorJobsConfigRepository measureMentorConfigRepository;
	
	@Autowired TextEncryptor textEncryptor
	
	@Test
	def "get a config from the database with the encrypted string still encrypted"() {
		
		setup:
		this.measureMentorConfigRepository.deleteAll();
		String config = '[{"type":"Jira", "url":"http://myJira.com"},{"type":"Jenkins", "url":"http://myjenkins.com"}]';
		this.measureMentorConfigRepository.save([name:"TESTCONFIG", encryptedConfig: Base64.getEncoder().encodeToString(this.textEncryptor.encrypt(config).getBytes())] as MeasureMentorJobsConfig)
		
		when:
		MeasureMentorJobsConfig rmmc = this.measureMentorConfigRepository.findByName("TESTCONFIG");
		
		then:
		rmmc.name == "TESTCONFIG"; 
		this.textEncryptor.decrypt(new String(Base64.getDecoder().decode(rmmc.encryptedConfig))) == '[{"type":"Jira", "url":"http://myJira.com"},{"type":"Jenkins", "url":"http://myjenkins.com"}]';
		
//		cleanup:
//		this.measureMentorConfigRepository.deleteAll();
		
	}
}
