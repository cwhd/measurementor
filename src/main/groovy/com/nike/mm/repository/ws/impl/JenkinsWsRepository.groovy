package com.nike.mm.repository.ws.impl

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nike.mm.dto.HttpRequestDto;
import com.nike.mm.repository.ws.IJenkinsWsRepository
import com.nike.mm.service.IHttpRequestService;

@Repository
class JenkinsWsRepository implements IJenkinsWsRepository {

	@Autowired IHttpRequestService httpRequestService;
	
	@Override
	public List<String> getListOfRepositoryNames(final HttpRequestDto dto) {
		return null;
	}
}
