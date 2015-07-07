package com.nike.mm.dto

import com.nike.mm.entity.internal.JobHistory


class JobRunResponseDto {

    String type

    int recordsCount

    JobHistory.Status status

    String errorMessage
}
