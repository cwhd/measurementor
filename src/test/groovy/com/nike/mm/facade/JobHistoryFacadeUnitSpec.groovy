package com.nike.mm.facade

import com.nike.mm.business.internal.IJobHistoryBusiness
import com.nike.mm.entity.JobHistory
import com.nike.mm.facade.impl.JobHistoryFacade
import org.springframework.data.domain.PageImpl
import spock.lang.Specification

class JobHistoryFacadeUnitSpec extends Specification {

    IJobHistoryFacade jobHistoryFacade

    IJobHistoryBusiness jobHistoryBusiness;

    def setup() {
        this.jobHistoryFacade                       = new JobHistoryFacade()
        this.jobHistoryBusiness                     = Mock(IJobHistoryBusiness.class)
        this.jobHistoryFacade.jobHistoryBusiness    = this.jobHistoryBusiness
    }

    def "test that the facade converts from an entity to a dto"() {

        setup:
        def dtos = [[id:"ThisId", jobid:"SomeJobId", startDate: new Date(), endDate: new Date(), success:true, status:"Healthy", comments: "Comments"] as  JobHistory]

        when:
        def rdtos = this.jobHistoryFacade.findAllByJobid("", null);

        then:
        1 * this.jobHistoryBusiness.findByJobIdAndPage(_, _) >> new PageImpl(dtos, null, 1)
        rdtos.size()        == 1
        rdtos[0].id         == "ThisId"
        rdtos[0].jobid      == "SomeJobId"
        rdtos[0].startDate  != null
        rdtos[0].endDate    != null
        rdtos[0].success    == true
        rdtos[0].comments   == "Comments"
    }
}
