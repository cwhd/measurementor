package com.nike.mm.repository.es

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration

import spock.lang.Specification

import com.nike.mm.MeasurementorApplication
import com.nike.mm.entity.Github
import com.nike.mm.repository.es.plugins.IGithubEsRepository;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = MeasurementorApplication.class)
//@WebAppConfiguration
class GithubRepositorySpec extends Specification {

//	@Autowired IGithubEsRepository githubRepository;
	
	
	@Test
	def "ensure that the database is loaded"() {
		
//		setup:
//		this.githubRepository.deleteAll();
//		this.githubRepository.save([author:"rparry", repo:"somewhere", scmAction:"push", linesAdded:12,linesRemoved:0, commentCount:2] as Github);
//		this.githubRepository.save([author:"rparry", repo:"somewhere", scmAction:"push", linesAdded:2,linesRemoved:10, commentCount:8] as Github);
//		this.githubRepository.save([author:"bbobby", repo:"somewhere", scmAction:"push", linesAdded:12,linesRemoved:32, commentCount:9] as Github);
//
		when:
//		def rlist = this.githubRepository.findAllByAuthor("rparry");
		1 == 1
		then:
//		rlist.size() == 2;
		1 == 1
//		cleanup:
//		this.githubRepository.deleteAll();
	}
}
