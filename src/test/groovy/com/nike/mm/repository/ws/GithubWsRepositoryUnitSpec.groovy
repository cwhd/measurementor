package com.nike.mm.repository.ws

import com.nike.mm.dto.HttpRequestDto
import com.nike.mm.repository.es.plugins.IGithubEsRepository
import com.nike.mm.repository.ws.impl.GithubWsRepository
import com.nike.mm.service.IHttpRequestService
import spock.lang.Specification

/**
 * Created by rparr2 on 6/13/15.
 */
class GithubWsRepositoryUnitSpec extends Specification {

    IGithubWsRepository githubWsRepository

    IHttpRequestService httpRequestService

    IGithubEsRepository githubEsRepository

    def setup() {
        this.githubWsRepository                             = new GithubWsRepository()
        this.httpRequestService                             = Mock(IHttpRequestService)
        this.githubEsRepository                             = Mock(IGithubEsRepository)
        this.githubWsRepository.httpRequestService          = this.httpRequestService
        this.githubWsRepository.githubEsRepository          = this.githubEsRepository
    }

    def "find all repositories" () {

        setup:
        def rjson = [[name:"sillyName"]]

        when:
        def projects = this.githubWsRepository.findAllRepositories(null)

        then:
        1 * this.httpRequestService.callRestfulUrl(_) >> rjson
        1 == projects.size()
        projects[0] == 'sillyName'
    }

    def "find all pull requests" () {

        when:
        def rlist = this.githubWsRepository.findAllPullRequests(null)

        then:
        rlist.isEmpty()
    }

    def "no commits returned" () {

        when:
        def rlist = this.githubWsRepository.findAllCommitsForRepository([] as HttpRequestDto)

        then:
        1 * this.httpRequestService.callRestfulUrl(_) >> []
        rlist.isEmpty()
    }

    def "one commit returned new record created" () {

        when:
        def rlist = this.githubWsRepository.findAllCommitsForRepository([path:'somepath'] as HttpRequestDto)

        then:
        2 * this.httpRequestService.callRestfulUrl(_) >>> [[[sha: "sha"]], [commit:[committer:[date: new Date(), name: 'Bob']], stats:[additions: 20, deletions: 12]]]
        1 * this.githubEsRepository.findBySha(_) >> null
        1 == rlist.size()
        rlist[0].sha            == 'sha'
        rlist[0].linesAdded     == 20
        rlist[0].linesRemoved   == 12
    }

    def "one commit returned old record updated " () {

        when:
        def rlist = this.githubWsRepository.findAllCommitsForRepository([path:'somepath'] as HttpRequestDto)

        then:
        2 * this.httpRequestService.callRestfulUrl(_) >>> [[[sha: "sha"]], [commit:[committer:[date: new Date(), name: 'Bob']], stats:[additions: 20, deletions: 12]]]
        1 * this.githubEsRepository.findBySha(_) >> [id: "id", sha: "sha"]
        1 == rlist.size()
        rlist[0].sha            == 'sha'
        rlist[0].linesAdded     == 20
        rlist[0].linesRemoved   == 12
    }
}
