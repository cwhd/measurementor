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

    Object findAllProjects(HttpRequestDto dto) {
        def json = this.httpRequestService.callRestfulUrl(dto)
        def projectList = []
        for (def project : json.values) {
            projectList.add(project.key)
        }
        return projectList
    }
}
