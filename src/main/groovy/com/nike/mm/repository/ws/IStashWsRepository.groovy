package com.nike.mm.repository.ws

import com.nike.mm.dto.HttpRequestDto
import com.nike.mm.entity.Stash

/**
 * Created by rparr2 on 6/16/15.
 */
interface IStashWsRepository {

    List<String> findAllProjects(HttpRequestDto dto)

    List<Expando> findAllReposForProject(String projectKey, HttpRequestDto dto)

    Object findAllCommits(HttpRequestDto dto)

    Object findCommitDataFromSha(HttpRequestDto dto)

    Object findAllPullRequests(HttpRequestDto dto)

    Object findCommitCount(HttpRequestDto dto)
}