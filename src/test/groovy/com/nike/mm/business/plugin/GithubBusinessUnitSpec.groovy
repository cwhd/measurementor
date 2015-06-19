package com.nike.mm.business.plugin

import com.nike.mm.business.plugins.IGithubBusiness
import com.nike.mm.business.plugins.impl.GithubBusiness
import com.nike.mm.entity.Github
import com.nike.mm.repository.es.plugins.IGithubEsRepository
import com.nike.mm.repository.ws.IGithubWsRepository
import spock.lang.Specification

/**
 * Created by rparr2 on 6/13/15.
 */
class GithubBusinessUnitSpec extends Specification {

    IGithubBusiness githubBusiness

    IGithubWsRepository githubWsRepository

    IGithubEsRepository githubEsRepository

    def setup() {
        this.githubBusiness                     = new GithubBusiness()
        this.githubWsRepository                 = Mock(IGithubWsRepository)
        this.githubEsRepository                 = Mock(IGithubEsRepository)
        this.githubBusiness.githubWsRepository  = this.githubWsRepository
        this.githubBusiness.githubEsRepository  = this.githubEsRepository
    }


    def "validate that the objects type is Github" () {

        when:
        def type = this.githubBusiness.type()

        then:
        type == 'Github'
    }

    def "validate that config is valid" () {

        setup:
        def config = [url:'http://nike.com', access_token: "access_token", repository_owner: 'testy']

        when:
        boolean valid = this.githubBusiness.validateConfig(config)

        then:
        valid
    }

    def "invalid missing repo owner" () {

        setup:
        def config = [url:'http://nike.com', access_token: "access_token"]

        when:
        boolean valid = this.githubBusiness.validateConfig(config)

        then:
        !valid
    }

    def "invalid missing access token" () {

        setup:
        def config = [url:'http://nike.com', repository_owner: 'testy']

        when:
        boolean valid = this.githubBusiness.validateConfig(config)

        then:
        !valid
    }

    def "invalid missing url" () {

        setup:
        def config = [access_token: "access_token", repository_owner: 'testy']

        when:
        boolean valid = this.githubBusiness.validateConfig(config)

        then:
        !valid
    }

    def "no  repositories found" () {

        setup:
        def configinfo = [url:'http://nike.com', access_token:'access_token', repository_owner:'repoowner']

        when:
        def rlist = this.githubBusiness.updateData(configinfo)

        then:
        1 * this.githubWsRepository.findAllRepositories(_) >> []
    }

    def "one repository found no commits no pulls" () {
        setup:
        def configinfo = [url:'http://nike.com', access_token:'access_token', repository_owner:'repoowner']

        when:
        def rlist = this.githubBusiness.updateData(configinfo)

        then:
        1 * this.githubWsRepository.findAllRepositories(_)          >> ['repo1']
        1 * this.githubWsRepository.findAllCommitsForRepository(_)  >> [ ]
        1 * this.githubWsRepository.findAllPullRequests(_)          >> [ ]
        0 * this.githubEsRepository.save(_)
    }

    def "one repository found with commits but no pulls" () {
        setup:
        def configinfo = [url:'http://nike.com', access_token:'access_token', repository_owner:'repoowner']

        when:
        def rlist = this.githubBusiness.updateData(configinfo)

        then:
        1 * this.githubWsRepository.findAllRepositories(_)          >> ['repo1']
        1 * this.githubWsRepository.findAllCommitsForRepository(_)  >> [ [id: 'id'] as Github ]
        1 * this.githubWsRepository.findAllPullRequests(_)          >> [ ]
        1 * this.githubEsRepository.save(_)
    }

    def "one repository found no  commits but pulls" () {
        setup:
        def configinfo = [url:'http://nike.com', access_token:'access_token', repository_owner:'repoowner']

        when:
        def rlist = this.githubBusiness.updateData(configinfo)

        then:
        1 * this.githubWsRepository.findAllRepositories(_)          >> ['repo1']
        1 * this.githubWsRepository.findAllCommitsForRepository(_)  >> [ ]
        1 * this.githubWsRepository.findAllPullRequests(_)          >> [ [id: 'id'] as Github ]
        1 * this.githubEsRepository.save(_)
    }

    def "one repository found with commits and pull requests" () {
        setup:
        def configinfo = [url:'http://nike.com', access_token:'access_token', repository_owner:'repoowner']

        when:
        def rlist = this.githubBusiness.updateData(configinfo)

        then:
        1 * this.githubWsRepository.findAllRepositories(_)          >> ['repo1']
        1 * this.githubWsRepository.findAllCommitsForRepository(_)  >> [ [id: 'id'] as Github ]
        1 * this.githubWsRepository.findAllPullRequests(_)          >> [ [id: 'id'] as Github ]
        2 * this.githubEsRepository.save(_)
    }
}
