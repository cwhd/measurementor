package com.nike.mm.repository.ws;

import com.nike.mm.dto.HttpRequestDto;

public interface IJenkinsWsRepository {

    Object findListOfJobs(HttpRequestDto dto);

    Object findListOfJobsJobs(HttpRequestDto dto);

    Object findListOfBuilds(HttpRequestDto dto);

    Object findBuildInformation(HttpRequestDto dto);

    Object findFinalBuildInformation(HttpRequestDto dto);
}
