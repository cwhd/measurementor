package com.nike.mm.repository.ws

import com.nike.mm.dto.HttpRequestDto
import com.nike.mm.dto.JiraRequestDto

/**
 * Created by rparr2 on 6/17/15.
 */
interface IJiraWsRepository {

    List<String> getProjectsList(HttpRequestDto dto)

    Object getData(JiraRequestDto requestDto )
}