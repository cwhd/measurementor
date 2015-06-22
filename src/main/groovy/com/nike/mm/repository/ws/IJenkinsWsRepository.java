package com.nike.mm.repository.ws;

import java.util.List;

import com.nike.mm.dto.HttpRequestDto;

public interface IJenkinsWsRepository {

    Object findListOfJobs(HttpRequestDto dto);

    Object findListOfJobsJobs(HttpRequestDto dto);

    Object findListOfJobsJobsJobs(HttpRequestDto dto);
}
