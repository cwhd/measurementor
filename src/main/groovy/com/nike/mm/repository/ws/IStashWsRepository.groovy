package com.nike.mm.repository.ws

import com.nike.mm.dto.HttpRequestDto

/**
 * Created by rparr2 on 6/16/15.
 */
interface IStashWsRepository {

    Object findAllProjects(HttpRequestDto dto);

}