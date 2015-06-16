package com.nike.mm.repository.ws;

import java.util.List;

import com.nike.mm.dto.HttpRequestDto;
import com.nike.mm.entity.Github;

public interface IGithubWsRepository {

    List<String> findAllRepositories(final HttpRequestDto dto);

    List<Github> findAllCommitsForRepository(final HttpRequestDto dto);

    List<Github> findAllPullRequests(HttpRequestDto dto);
}
