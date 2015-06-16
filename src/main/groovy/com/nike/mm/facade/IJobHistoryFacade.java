package com.nike.mm.facade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nike.mm.dto.JobHistoryDto;

public interface IJobHistoryFacade {

    Page<JobHistoryDto> findAllByJobid(String jobid, Pageable pageable);
}
