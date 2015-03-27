package org.cwhd.measure

import grails.test.mixin.TestFor
import org.cwhd.connect.CouchConnectorService
import spock.lang.Specification

import java.text.SimpleDateFormat

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(JiraDataService)
class JiraDataServiceSpec extends Specification {
    def jiraDataService

    def setup() {
        jiraDataService = new JiraDataService() // mockService(JiraDataService)
        jiraDataService.httpRequestService = Mock(HttpRequestService)
        jiraDataService.couchConnectorService = Mock(CouchConnectorService)
    }

    def cleanup() {
    }

    //void "convert ISO8601 date to java date"() {
    //    expect: jiraDataService.convertJiraDateToJavaDate("2014-06-17T18:02:24.000+0000") == new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SSSZ").parse("2014-06-1718:02:24.000+0000")
    //}

    //TODO should have a test that gets a JIRA response from a JSON file and tests just the parsing part
    //TODO need to refactor my code a bit to make it more testable

}
