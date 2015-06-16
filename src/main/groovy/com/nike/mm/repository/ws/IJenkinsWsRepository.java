package com.nike.mm.repository.ws;

import java.util.List;

import com.nike.mm.dto.HttpRequestDto;

public interface IJenkinsWsRepository {

    List<String> getListOfRepositoryNames(HttpRequestDto dto);
}
