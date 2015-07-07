package com.nike.mm.repository.ws;

import com.nike.mm.dto.HttpRequestDto;
import com.nike.mm.entity.plugins.Github;

import java.util.List;

public interface IGithubWsRepository {

    List<String> findAllRepositories(final HttpRequestDto dto);

    List<Github> findAllCommitsForRepository(final HttpRequestDto dto);

    List<Github> findAllPullRequests(HttpRequestDto dto);
}
