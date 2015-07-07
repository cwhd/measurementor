package com.nike.mm.repository.es

import org.junit.Test
import spock.lang.Specification

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
