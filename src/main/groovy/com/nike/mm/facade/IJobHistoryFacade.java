package com.nike.mm.facade;

import com.nike.mm.dto.JobHistoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IJobHistoryFacade {

    Page<JobHistoryDto> findAllByJobid(String jobid, Pageable pageable);
}
