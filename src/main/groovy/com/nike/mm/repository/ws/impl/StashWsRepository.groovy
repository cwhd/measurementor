package com.nike.mm.repository.ws.impl

import com.nike.mm.dto.HttpRequestDto
import com.nike.mm.repository.ws.IStashWsRepository
import com.nike.mm.service.IHttpRequestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * Created by rparr2 on 6/16/15.
 */
@Repository
class StashWsRepository implements IStashWsRepository {

    @Autowired IHttpRequestService httpRequestService;

    @Override
    List<String> findAllProjects(final HttpRequestDto dto) {
        def json = this.httpRequestService.callRestfulUrl(dto)
        def projectList = []
        if (json) {
            for (def project : json.values) {
                projectList.add(project.key)
            }
            if (!json.isLastPage) {
                dto.query.start = dto.query.start + dto.query.limit
                projectList.addAll(this.findAllProjects(dto))
            }
        }
        return projectList
    }

    @Override
    List<Expando> findAllReposForProject(final String projectKey, final HttpRequestDto dto) {
        final def json = this.httpRequestService.callRestfulUrl(dto)
        def repoList = []
        if(!json.isLastPage) {
            dto.query.start += dto.query.limit
            repoList.addAll(findAllReposForProject(projectKey, dto))
        }
        for (def i : json.values) {
            repoList.add(new Expando(projectKey:projectKey, repo: i.slug, start:0, limit:300))
        }
        return repoList
    }

    @Override
    Object findAllCommits(final HttpRequestDto dto) {
        return this.httpRequestService.callRestfulUrl(dto)
    }

    @Override
    Object findCommitDataFromSha(final HttpRequestDto dto){
        return this.httpRequestService.callRestfulUrl(dto)
    }

    @Override
    Object findAllPullRequests(final HttpRequestDto dto) {
        return this.httpRequestService.callRestfulUrl(dto)
    }

    @Override
    Object findCommitCount(HttpRequestDto dto) {
        return this.httpRequestService.callRestfulUrl(dto)
    }
}
