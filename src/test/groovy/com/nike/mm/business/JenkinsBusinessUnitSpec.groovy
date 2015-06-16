package com.nike.mm.business

import groovy.json.JsonSlurper
import spock.lang.Specification

import com.nike.mm.business.plugins.IJenkinsBusiness;
import com.nike.mm.business.plugins.impl.JenkinsBusiness;

class JenkinsBusinessUnitSpec extends Specification {
	
	IJenkinsBusiness jenkinsBusiness = new JenkinsBusiness();
	
	def "check type is jenkins" () {
		when:
		String type = this.jenkinsBusiness.type();
		
		then:
		type == "Jenkins"
	}
	
	def "valid configuration" () {
		when:
		def config = new JsonSlurper().parseText('{"url":"https://api.github.com"}');
		
		then:
		jenkinsBusiness.validateConfig(config) == true;
	}
	
	def "invalid configuration" () {
		when:
		def config1 = new JsonSlurper().parseText('{"url":""}');
		def config2 = new JsonSlurper().parseText('{}');
		
		then:
		jenkinsBusiness.validateConfig(config1) == false;
		jenkinsBusiness.validateConfig(config2) == false;
	}

}
