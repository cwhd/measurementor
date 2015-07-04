package com.nike.mm.repository.ws

import com.nike.mm.dto.HttpRequestDto

/**
 * Created by rparr2 on 6/17/15.
 */
interface IJiraWsRepository {

    List<String> getProjectsList(HttpRequestDto dto)

    Object getDataForProject(HttpRequestDto dto)
}