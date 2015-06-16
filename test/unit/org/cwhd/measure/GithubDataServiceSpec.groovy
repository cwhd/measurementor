package org.cwhd.measure

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(GithubDataService)
class GithubDataServiceSpec extends Specification {
    def githubDataService

    def setup() {
        githubDataService = new GithubDataService()
    }

    def "Make An HTTP Request To GitHub"() {
//        def githubResponse = githubDataService.makeGitHubRequest("/users/cwhd/repos", [start: 0, limit: 300])
//        expect: githubResponse == "{ one:one }"
    }

    def cleanup() {
    }

    void "test something"() {
    }
}
