package com.nike.mm.dto

/**
 * Created by rparr2 on 6/17/15.
 */
class JiraRequestDto {
    long startAt = 0
    int maxResults = 100
    String project
    Date fromDate
    String url
    String credentials
}
