package com.nike.mm.repository.ws.impl

import com.nike.mm.dto.HttpRequestDto
import com.nike.mm.repository.ws.IJiraWsRepository
import com.nike.mm.service.IHttpRequestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * Created by rparr2 on 6/17/15.
 */
@Repository
class JiraWsRepository implements IJiraWsRepository {

    @Autowired IHttpRequestService httpRequestService

    @Override
    List<String> getProjectsList(final HttpRequestDto dto) {
        def json = this.httpRequestService.callRestfulUrl(dto)
        def projects = []
        for (def project : json) {
            projects.add(project.key)
        }
        return projects
    }

    @Override
    Object getDataForProject(final HttpRequestDto dto) {
        return this.httpRequestService.callRestfulUrl(dto)
    }
}
