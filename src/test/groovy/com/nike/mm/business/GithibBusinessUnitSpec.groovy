package com.nike.mm.business

import groovy.json.JsonSlurper
import spock.lang.Specification

import com.nike.mm.business.plugins.IGithubBusiness;
import com.nike.mm.business.plugins.impl.GithubBusiness;

class GithibBusinessUnitSpec extends Specification {
	
	IGithubBusiness githubBusiness = new GithubBusiness();

	def "Ensure type is Github" (){
		when:
		def rtype = this.githubBusiness.type();
		
		then:
		rtype == "Github"
	}
	
	def "config is valid" () {
		
		when:
		def config = new JsonSlurper().parseText('{"url":"https://api.github.com","access_token":"secret","repository_owner":"bob"}');
		
		then:
		this.githubBusiness.validateConfig(config) == true;
	}
	
	def "url is null returns false" () {
		when:
		def config1 = new JsonSlurper().parseText('{"url":"","access_token":"secret","repository_owner":"bob"}');
		def config2 = new JsonSlurper().parseText('{"access_token":"secret","repository_owner":"bob"}');
		
		then:
		this.githubBusiness.validateConfig(config1) == false;
		this.githubBusiness.validateConfig(config2) == false;
	}
	
	def "access_token is null return false" () {
		when:
		def config1 = new JsonSlurper().parseText('{"url":"https://api.github.com","access_token":"","repository_owner":"bob"}');
		def config2 = new JsonSlurper().parseText('{"url":"https://api.github.com","repository_owner":"bob"}');
		
		then:
		this.githubBusiness.validateConfig(config1) == false;
		this.githubBusiness.validateConfig(config2) == false;
	}
	
	def "repository_owner is null return false" () {
		when:
		def config1 = new JsonSlurper().parseText('{"url":"https://api.github.com","access_token":"secret","repository_owner":""}');
		def config2 = new JsonSlurper().parseText('{"url":"https://api.github.com","access_token":"secret"}');
		
		then:
		this.githubBusiness.validateConfig(config1) == false;
		this.githubBusiness.validateConfig(config2) == false;
	}
}
