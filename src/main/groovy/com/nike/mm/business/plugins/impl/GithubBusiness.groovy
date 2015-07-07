package com.nike.mm.business.plugins.impl

import com.google.common.collect.Lists
import com.nike.mm.business.plugins.IGithubBusiness
import com.nike.mm.dto.HttpRequestDto
import com.nike.mm.dto.JobRunResponseDto
import com.nike.mm.entity.internal.JobHistory
import com.nike.mm.entity.plugins.Github
import com.nike.mm.repository.es.plugins.IGithubEsRepository
import com.nike.mm.repository.ws.IGithubWsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GithubBusiness extends AbstractBusiness implements IGithubBusiness {

    @Autowired
    IGithubWsRepository githubWsRepository

    @Autowired
    IGithubEsRepository githubEsRepository

    //TODO better pagination
    def start = 0
    def limit = 300

    @Override
    String type() {
        return "Github";
    }

    @Override
    String validateConfig(Object config) {
        List<String> errorMessages = Lists.newArrayList()
        if (!config.url) {
            errorMessages.add(MISSING_URL)
        }
        if (!config.access_token) {
            errorMessages.add("Missing access token")
        }
        if (!config.repository_owner) {
            errorMessages.add("Missing repository owner")
        }
        return buildValidationErrorString(errorMessages)
    }

    @Override
    JobRunResponseDto updateDataWithResponse(Date lastRunDate, Object configInfo) {
        List<String> repositories = this.findAllRepositories(configInfo, lastRunDate);
        for (String repo : repositories) {
            List commits = this.getAllCommitsForRepo(configInfo, repo);
            if (!commits.isEmpty()) {
                this.githubEsRepository.save(commits);
            }
            List pulls = this.getAllPullRequestsForRepo(configInfo, repo);
            if (!pulls.isEmpty()) {
                this.githubEsRepository.save(pulls);
            }
        }
        return [type: type(), status: JobHistory.Status.success, reccordsCount: 0] as JobRunResponseDto
    }

    private List<String> findAllRepositories(final Object configInfo, final Date fromDate) {
        String path = "/users/$configInfo.repository_owner/repos";
        HttpRequestDto dto = [url: configInfo.url, path: path, query: [access_token: configInfo.access_token, start:
				start, limit: limit]] as HttpRequestDto;
        return this.githubWsRepository.findAllRepositories(dto);
    }

    private List<Github> getAllCommitsForRepo(final Object configInfo, final String repo) {
        String path = "/repos/$configInfo.repository_owner/$repo/commits";
        HttpRequestDto dto = [url: configInfo.url, path: path, query: [access_token: configInfo.access_token, start:
				start, limit: limit]] as HttpRequestDto
        return this.githubWsRepository.findAllCommitsForRepository(dto);
    }

    private List<Github> getAllPullRequestsForRepo(final Object configInfo, final String repo) {
        String path = "/repos/$configInfo.repository_owner/$repo/pulls";
        HttpRequestDto dto = [url: configInfo.url, path: path, query: [access_token: configInfo.access_token, start: start, limit: limit]] as HttpRequestDto
        this.githubWsRepository.findAllPullRequests(dto);
    }
}
