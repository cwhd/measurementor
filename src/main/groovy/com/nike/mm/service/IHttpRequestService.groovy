package com.nike.mm.service

import com.nike.mm.dto.HttpRequestDto

public interface IHttpRequestService {

    Object callRestfulUrl(HttpRequestDto httpRequestDto)

    Object postSomething(url, body)
}
