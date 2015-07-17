    package com.nike.mm.business.plugin

import com.nike.mm.MeasurementorApplication
import com.nike.mm.business.plugins.IJiraBusiness
import com.nike.mm.dto.HttpRequestDto
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

import java.text.SimpleDateFormat

/**
 * Created by rparry on 7/15/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MeasurementorApplication.class)
@WebAppConfiguration
class PleaseWorkJiraSpec extends Specification {

    @Autowired IJiraBusiness jiraBusiness

    @Test
    def "lets see if we can get some data"() {

        setup:
        //def config = [url:"", credentials: ""] as HttpRequestDto
        1 == 1

        when:
        //this.jiraBusiness.updateDataWithResponse(new SimpleDateFormat("dd/MM/yyyy").parse("01/07/2015"), config);
        1 == 1
        then:
        1 == 1
    }
}
