package com.nike.mm.repository.es.plugins

import com.nike.mm.MeasurementorApplication
import com.nike.mm.entity.plugins.Stash
import com.nike.mm.service.IUtilitiesService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

import java.text.SimpleDateFormat

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MeasurementorApplication.class)
@WebAppConfiguration
class StashRepositorySpec extends Specification {

    @Autowired
    IStashEsRepository stashEsRepository

    @Autowired
    IUtilitiesService utilitiesService

    @Test
    def "retrieve the most recent record for a project and repo" () {

        setup:
        final Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2010")
        final Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse("02/01/2010")
        final Date date3 = new SimpleDateFormat("dd/MM/yyyy").parse("03/01/2010")
        final def stashRepoContent = [
                new Stash(id: "1", stashProject: "project1", repo: "repo1", scmAction: "commit", referenceDate: date1),
                new Stash(id: "2", stashProject: "project1", repo: "repo1", scmAction: "pull", referenceDate: date2),
                new Stash(id: "3", stashProject: "project1", repo: "repo2", scmAction: "commit", referenceDate: date2),
                new Stash(id: "4", stashProject: "project2", repo: "repo3", scmAction: "commit", referenceDate: date3)
        ]
        this.stashEsRepository.save(stashRepoContent)
        final def pageable = new PageRequest(0, 1, new Sort(Sort.Direction.DESC, "referenceDate"))

        when:
        final Page<Stash> stashList = this.stashEsRepository.findByStashProjectAndRepoAndScmAction("project1", "repo1", "commit", pageable)

        then:
        stashList
        stashList.content.size() == 1
        stashList.content[0].id  == "1"

    }
}
