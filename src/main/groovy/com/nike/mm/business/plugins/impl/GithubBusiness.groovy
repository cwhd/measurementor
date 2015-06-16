package com.nike.mm.business.plugins.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import com.nike.mm.business.plugins.IGithubBusiness;
import com.nike.mm.dto.HttpRequestDto
import com.nike.mm.entity.Github
import com.nike.mm.repository.es.plugins.IGithubEsRepository;
import com.nike.mm.repository.ws.IGithubWsRepository

@Service
class GithubBusiness implements IGithubBusiness {

	@Autowired IGithubWsRepository githubWsRepository
	
	@Autowired IGithubEsRepository githubEsRepository
	
	//TODO better pagination
	def start = 0
	def limit = 300
	
	@Override
	String type() {
		return "Github";
	}
	
	@Override
	boolean validateConfig(Object config) {
		boolean rbool = false;
		if (config.url && config.access_token && config.repository_owner) {
			rbool = true;
		}
		return rbool;
	}
	
	@Override
	void updateData(Object configInfo, Date fromDate) {
		List<String> repositories = this.findAllRepositories(configInfo, fromDate);
		for (String repo: repositories) {
			List commits = this.getAllCommitsForRepo(configInfo, repo);
			if (!commits.isEmpty()) {
				this.githubEsRepository.save(commits);
			}
			List pulls = this.getAllPullRequestsForRepo(configInfo, repo);
			if (!pulls.isEmpty()) {
				this.githubEsRepository.save(pulls);
			}
		}
	}
	
	private List<String> findAllRepositories(final Object configInfo, final Date fromDate) {
		String path = "/users/$configInfo.repository_owner/repos";
		HttpRequestDto dto = [url: configInfo.url, path: path, query:[access_token: configInfo.access_token, start: start, limit: limit]] as HttpRequestDto;
		return this.githubWsRepository.findAllRepositories(dto);
	}
	
	private List<Github> getAllCommitsForRepo(final Object configInfo, final String repo) {
		String path = "/repos/$configInfo.repository_owner/$repo/commits";
		HttpRequestDto dto = [url: configInfo.url, path: path, query:[access_token: configInfo.access_token, start: start, limit: limit]] as HttpRequestDto
		return this.githubWsRepository.findAllCommitsForRepository(dto);
	}
	
	private List<Github> getAllPullRequestsForRepo(final Object configInfo, final String repo) {
		String path = "/repos/$configInfo.repository_owner/$repo/pulls";
		HttpRequestDto dto = [url: configInfo.url, path: path, query:[access_token: configInfo.access_token, start: start, limit: limit]] as HttpRequestDto
		this.githubWsRepository.findAllPullRequests(dto);
	}
}
