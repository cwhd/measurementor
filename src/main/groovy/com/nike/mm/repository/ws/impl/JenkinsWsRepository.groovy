package com.nike.mm.repository.ws.impl

import com.nike.mm.dto.HttpRequestDto
import com.nike.mm.repository.ws.IJenkinsWsRepository
import com.nike.mm.service.IHttpRequestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class JenkinsWsRepository implements IJenkinsWsRepository {

	@Autowired IHttpRequestService httpRequestService;
	
	@Override
	Object findListOfJobs(final HttpRequestDto dto) {
		return this.httpRequestService.callRestfulUrl(dto)
	}

	@Override
	Object findListOfJobsJobs(final HttpRequestDto dto) {
		return this.httpRequestService.callRestfulUrl(dto)
	}

    @Override
    Object findListOfBuilds(final HttpRequestDto dto) {
        return this.httpRequestService.callRestfulUrl(dto)
    }

	@Override
	Object findBuildInformation(final HttpRequestDto dto) {
		return this.httpRequestService.callRestfulUrl(dto)
	}

	@Override
	Object findFinalBuildInformation(HttpRequestDto dto) {
		return this.httpRequestService.callRestfulUrl(dto)
	}
}
