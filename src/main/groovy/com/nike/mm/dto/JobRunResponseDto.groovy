package com.nike.mm.dto

import com.nike.mm.entity.JobHistory


class JobRunResponseDto {

    String type

    int recordsCount

    JobHistory.Status status

    String errorMessage
}
