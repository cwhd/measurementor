package com.nike.mm.rest

import com.nike.mm.dto.JobHistoryDto
import com.nike.mm.facade.IJobHistoryFacade
import com.nike.mm.rest.assembler.JobHistoryResourceAssembler
import com.nike.mm.rest.impl.JobHistoryRest
import org.springframework.data.domain.PageImpl
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.ResourceAssembler
import spock.lang.Specification

/**
 * Created by rparr2 on 6/13/15.
 */
class JobHistoryRestUnitSpec extends Specification {

    IJobHistoryRest jobHistoryRest

    IJobHistoryFacade jobHistoryFacade

    JobHistoryResourceAssembler jobHistoryResourceAssembler

    def setup() {
        this.jobHistoryRest = new JobHistoryRest()
        this.jobHistoryFacade                           = Mock(IJobHistoryFacade)
        this.jobHistoryRest.jobHistoryFacade            = this.jobHistoryFacade
    }

    def "do a simple rest call" () {

        setup:
        def pageAssembler = Mock(PagedResourcesAssembler)

        when:
        this.jobHistoryRest.pageThrough("", null, pageAssembler);

        then:
        1 * this.jobHistoryFacade.findAllByJobid(_, _)  >> new PageImpl<JobHistoryDto>([], null, 1)
        1 * pageAssembler.toResource(_, _)              >> null
        this.jobHistoryRest.jobHistoryFacade            != null
    }
}
