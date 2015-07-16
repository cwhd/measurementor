package com.nike.mm.business.plugin

import com.nike.mm.business.plugin.data.StashDataForTests
import com.nike.mm.business.plugins.IStashBusiness
import com.nike.mm.business.plugins.impl.StashBusiness
import com.nike.mm.entity.plugins.Stash
import com.nike.mm.repository.es.plugins.IStashEsRepository
import com.nike.mm.repository.ws.IStashWsRepository
import com.nike.mm.service.IUtilitiesService
import com.nike.mm.service.impl.UtilitiesService
import spock.lang.Specification

class StashBusinessUnitSpec extends Specification {

    IStashBusiness stashBusiness

    IStashWsRepository stashWsRepository

    IStashEsRepository stashEsRepository

    IUtilitiesService utilitiesService

    def setup() {
        this.stashBusiness                      = new StashBusiness()
        this.stashWsRepository                  = Mock(IStashWsRepository)
        this.stashEsRepository                  = Mock(IStashEsRepository)
        this.utilitiesService                   = new UtilitiesService()
        this.stashBusiness.stashWsRepository    = this.stashWsRepository
        this.stashBusiness.stashEsRepository    = this.stashEsRepository
        this.stashBusiness.utilitiesService     = this.utilitiesService
    }

    def "validate type" () {

        when:
        def type = this.stashBusiness.type()

        then:
        type == "Stash"
    }

    def "invalid config - emtpy"() {

        setup:
        def config = []

        when:
        String errorMessage = this.stashBusiness.validateConfig(config)

        then:
        errorMessage  == "Stash: Missing url"
    }

    def "validate config url is valid" () {

        setup:
        def configInfo = [url:"http://google.com"]

        when:
        String errorMessage = this.stashBusiness.validateConfig(configInfo)

        then:
        !errorMessage
    }

    def "validate config url is not valid" () {

        setup:
        def configInfo = [url:"http://google.notreal"]

        when:
        String errorMessage = this.stashBusiness.validateConfig(configInfo)

        then:
        errorMessage == "Stash: Invalid url"
    }

    def "no projects returned"() {

        setup:
        def config = [url:"http://made.up", credentials:"credentials"]
        def fromDate = Date.parse( 'dd-MM-yyyy', "01-01-2001" )

        when:
        this.stashBusiness.updateDataWithResponse(fromDate, config);

        then:
        1 * this.stashWsRepository.findAllProjects(_) >> []
    }

    def "find one project but no repos" () {

        setup:
        def config = [url:"http://made.up", credentials:"credentials"]
        def fromDate = Date.parse( 'dd-MM-yyyy', "01-01-2001" )

        when:
        this.stashBusiness.updateDataWithResponse(fromDate, config);

        then:
        1 * this.stashWsRepository.findAllProjects(_)               >> ["PROJECT1"]
        1 * this.stashWsRepository.findAllReposForProject(_, _)     >> []
        0 * this.stashWsRepository.findAllCommits(_)
        0 * this.stashWsRepository.findAllPullRequests(_)
    }

    def "find one project and one repo no existing stash record" () {

        setup:
        def config = [url:"http://made.up", credentials:"credentials"]
        def fromDate = Date.parse( 'dd-MM-yyyy', "01-01-2001" )

        when:
        this.stashBusiness.updateDataWithResponse(fromDate, config);

        then:
        1 * this.stashWsRepository.findAllProjects(_)               >> ["PROJECT1"]
        1 * this.stashWsRepository.findAllReposForProject(_, _)     >> [new Expando(projectKey:'PROJECT1', repo: 'REPO1', start:0, limit:300)]
        1 * this.stashWsRepository.findAllCommits(_)                >> StashDataForTests.COMMIT_HISTORY
        1 * this.stashEsRepository.findOne(_)                       >> null
        1 * this.stashWsRepository.findCommitDataFromSha(_)         >> StashDataForTests.FILE_DIFFS
        1 * this.stashEsRepository.save(_)
        1 * this.stashWsRepository.findAllPullRequests(_)           >> []
    }

    def "find one project and one repo existing stash record" () {

        setup:
        def config = [url:"http://made.up", credentials:"credentials"]
        def fromDate = Date.parse( 'dd-MM-yyyy', "01-01-2001" )

        when:
        this.stashBusiness.updateDataWithResponse(fromDate, config);

        then:
        1 * this.stashWsRepository.findAllProjects(_)               >> ["PROJECT1"]
        1 * this.stashWsRepository.findAllReposForProject(_, _)     >> [new Expando(projectKey:'PROJECT1', repo: 'REPO1', start:0, limit:300)]
        1 * this.stashWsRepository.findAllCommits(_)                >> StashDataForTests.COMMIT_HISTORY
        1 * this.stashEsRepository.findOne(_)                       >> new Stash()
        1 * this.stashWsRepository.findCommitDataFromSha(_)         >> StashDataForTests.FILE_DIFFS
        1 * this.stashEsRepository.save(_)
        1 * this.stashWsRepository.findAllPullRequests(_)           >> []
    }

    def "find one project and one repo and page commits once" () {

        setup:
        def config = [url:"http://made.up", credentials:"credentials", proxyUrl:"", proxyPort:0]
        def fromDate = Date.parse( 'dd-MM-yyyy', "01-01-2001" )

        when:
        this.stashBusiness.updateDataWithResponse(fromDate, config);

        then:
        1 * this.stashWsRepository.findAllProjects(_)               >> ["PROJECT1"]
        1 * this.stashWsRepository.findAllReposForProject(_, _)     >> [new Expando(projectKey:'PROJECT1', repo: 'REPO1', start:0, limit:300)]
        2 * this.stashWsRepository.findAllCommits(_)                >>> [ StashDataForTests.COMMIT_HISTORY_LAST_PAGE_FALSE, StashDataForTests.OLD_COMMIT_HISTORY]
        1 * this.stashEsRepository.findOne(_)                       >> new Stash()
        1 * this.stashWsRepository.findCommitDataFromSha(_)         >> StashDataForTests.FILE_DIFFS
        1 * this.stashEsRepository.save(_)
        1 * this.stashWsRepository.findAllPullRequests(_)           >> []
    }

    def "find one project and one repo but record before from date" () {

        setup:
        def config = [url:"http://made.up", credentials:"credentials", proxyUrl:"", proxyPort:0]
        def fromDate = Date.parse( 'dd-MM-yyyy', "01-01-2001" )

        when:
        this.stashBusiness.updateDataWithResponse(fromDate, config);

        then:
        1 * this.stashWsRepository.findAllProjects(_)               >> ["PROJECT1"]
        1 * this.stashWsRepository.findAllReposForProject(_, _)     >> [new Expando(projectKey:'PROJECT1', repo: 'REPO1', start:0, limit:300)]
        1 * this.stashWsRepository.findAllCommits(_)                >> StashDataForTests.OLD_COMMIT_HISTORY
        0 * this.stashEsRepository.findOne(_)
        0 * this.stashWsRepository.findCommitDataFromSha(_)
        0 * this.stashEsRepository.save(_)
        1 * this.stashWsRepository.findAllPullRequests(_)           >> []
    }

    def "find one project and one repo and get the pull requests" () {

        setup:
        def config = [url:"http://made.up", credentials:"credentials", proxyUrl:"", proxyPort:0]
        def fromDate = Date.parse( 'dd-MM-yyyy', "01-01-2001" )

        when:
        this.stashBusiness.updateDataWithResponse(fromDate, config);

        then:
        1 * this.stashWsRepository.findAllProjects(_)               >> ["PROJECT1"]
        1 * this.stashWsRepository.findAllReposForProject(_, _)     >> [new Expando(projectKey:'PROJECT1', repo: 'REPO1', start:0, limit:300)]
        1 * this.stashWsRepository.findAllCommits(_)                >> []
        1 * this.stashWsRepository.findAllPullRequests(_)           >> StashDataForTests.PULL_REQUESTS
        1 * this.stashWsRepository.findCommitCount(_)               >> StashDataForTests.COMMIT_COUNT
        1 * this.stashEsRepository.findOne(_)                       >> null
        1 * this.stashEsRepository.save(_)
    }

    def "find one project and one repo and get the pull requests with multiple pages" () {

        setup:
        def config = [url:"http://made.up", credentials:"credentials", proxyUrl:"", proxyPort:0]
        def fromDate = Date.parse( 'dd-MM-yyyy', "01-01-2001" )

        when:
        this.stashBusiness.updateDataWithResponse(fromDate, config);

        then:
        1 * this.stashWsRepository.findAllProjects(_)               >> ["PROJECT1"]
        1 * this.stashWsRepository.findAllReposForProject(_, _)     >> [new Expando(projectKey:'PROJECT1', repo: 'REPO1', start:0, limit:300)]
        1 * this.stashWsRepository.findAllCommits(_)                >> []
        2 * this.stashWsRepository.findAllPullRequests(_)           >>> [StashDataForTests.PULL_REQUESTS_PAGE_FALSE, StashDataForTests.PULL_REQUESTS]
        2 * this.stashWsRepository.findCommitCount(_)               >> StashDataForTests.COMMIT_COUNT
        2 * this.stashEsRepository.findOne(_)                       >> null
        2 * this.stashEsRepository.save(_)
    }

    def "test null on comments"() {

        setup:
        def three   = [attributes:[]]
        def four    = StashDataForTests.PULL_REQUESTS.values[0]

        when:
        int rthree  = this.stashBusiness.getCommentCount(three);
        int rfour   = this.stashBusiness.getCommentCount(four);

        then:
        rthree  == 0
        rfour   == 1
    }
}
