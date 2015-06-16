package org.cwhd.measure

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(UtilitiesService)
class UtilitiesServiceSpec extends Specification {
    def utilitiesService
    def fibonacciSeries = [1, 2, 3, 5, 8, 13]
    def powerOfTwoSeries = [1, 2, 4, 8, 16]

    def setup() {
        utilitiesService = new UtilitiesService()
    }

    def cleanup() {
    }

    void "Email Gets Clean"() {
        def cleanEmail = utilitiesService.cleanEmail(dirtyEmail)
        expect: cleanEmail == "chris_davis.com"
        where:
            dirtyEmail << ["chris@davis.com"]
    }

    void "Clean Email Returns Nothing If You Give It Nothing"() {
        def cleanEmail = utilitiesService.cleanEmail(dirtyEmail)
        expect: cleanEmail == null
        where:
            dirtyEmail << [null]
    }

    void "Make Username Token Friendly"() {
        def tokenFriendlyUserName = utilitiesService.makeNonTokenFriendly(nonTokenFriendlyUserName)
        expect: tokenFriendlyUserName == "chrisdavisauthor"
        where: nonTokenFriendlyUserName << ["chris-davis(author)_etw_whq"]
    }

    def "Token Cleaner Returns Nothing If You Give It Nothing"(){
        def tokenFriendlyUserName = utilitiesService.makeNonTokenFriendly(nonTokenFriendlyUserName)
        expect: tokenFriendlyUserName == null
        where: nonTokenFriendlyUserName << [null]
    }

//    void "JIRA Date Gets Clean"() {
//        def cleanJiraDate = utilitiesService.cleanJiraDate(rawJiraDate)
//        expect: cleanJiraDate == new Date()
//        where rawJiraDate << "2014-06-17T18:00:00.000+0000"
//    }


    void "Build Name From Jenkins Gets Clean"() {
        def cleanBuildName = utilitiesService.cleanFullBuildName(fullBuildName)
        expect: cleanBuildName == "Chris >> Builds"
        where:
            fullBuildName << ["Chris >> Builds #56", "Chris >> Builds #1"]
    }


    void "Test URL Cleaner"(){
        def cleanURL = utilitiesService.getPathFromUrl(url)
        expect: cleanURL == "/job/HEYOO/"

        where:
            url << ["https://jenkins.tools.whatever.com/job/HEYOO/", "http://www.test-url.net:8080/job/HEYOO/"]

    }

    void "Test Time Difference"() {
        def dateDiff =  utilitiesService.getDifferenceBetweenDatesInHours(firstDate, secondDate)
        expect: dateDiff == 0.9166666667
        where:
            firstDate << [1422639305990]
            secondDate << [1422642656258]
    }

    void "Timestamp String To Date Returns Nothing If You Give It Nothing"() {
        def theDate = utilitiesService.convertTimestampFromString(timestampString)
        expect: theDate == null
        where:
        timestampString << [null]
    }

    /*
    void "Test String Timestamp To Date"() {
        def theDate = utilitiesService.convertTimestampFromString(timestampString)
        expect: theDate == new Date(1421356703000L)
        where:
            timestampString << ["1421356703000"]
    }
    */

    void "Test time-estimate ratio for a fibonacci estimates"() {
        def estimateHealth = utilitiesService.estimateHealth(estimate, actualTime, 13, 9, fibonacciSeries).result
        expect: estimateHealth == 0

        where:
            estimate <<     [1, 2, 2, 3, 3, 5, 5, 8, 8, 8, 8, 13, 13, 13]
            actualTime <<   [1, 1, 2, 2, 3, 3, 4, 4, 5, 6, 6, 7, 8, 9]
    }

    void "Test time-estimate ratio for power of two estimates"() {
        def estimateHealth = utilitiesService.estimateHealth(estimate, actualTime, 13, 9, powerOfTwoSeries).result
        expect: estimateHealth == 0

        where:
        estimate <<     [1, 2, 2, 4, 4, 4, 8, 8, 8, 8, 16, 16, 16]
        actualTime <<   [1, 1, 2, 2, 3, 4, 4, 5, 6, 7, 8, 8, 9]
    }

    void "Test MinMax Works For Min"() {
        def minOutput = utilitiesService.minMax(minInput)
        expect: minOutput == 5
        where: minInput << [0, -1, -2, -50, -0.3, -2.2]
    }

    void "Test MinMax Works For Max"() {
        def maxOutput = utilitiesService.minMax(maxInput)
        expect: maxOutput == 0
        where: maxInput << [5, 5.5, 30, 50, 6, 22.2]
    }

    void "Test MinMax Works For Mid"() {
        def midOutput = utilitiesService.minMax(midInput)
        expect: midOutput == 5-midInput
        where: midInput << [1, 2, 3, 4]
    }

    /*
    void "Test CHD Calculation with perfect result"(){
        def chd = utilitiesService.calculateCHD(cloc, estimateHealth, recidivism, escapedDefects)
        expect: chd == 100
        where:
        cloc        <<      [49, 30]
        estimateHealth <<   [0,  0]
        recidivism     <<   [0, 0]
        escapedDefects <<   [0, 0]
    }

    void "Test CHD with 75% result"() {
        def chd = utilitiesService.calculateCHD(cloc, estimateHealth, recidivism, escapedDefects)
        expect: chd == 75
        where:
        cloc        <<      [100, 49, 49, 1, 30]
        estimateHealth <<   [0,  5, -5, 0, 0]
        recidivism     <<   [0, 0, 0, 0.5, 0]
        escapedDefects <<   [0, 0, 0, 0, 2]
    }

    void "Test a bunch of CHDs"() {
        def chd = utilitiesService.calculateCHD(cloc, estimateHealth, recidivism, escapedDefects)
        expect: chd == 100
        where:
        estimateHealth <<   [1,   0,  -2,  3,   -1,  0,  -1]
        cloc        <<      [65,  33, 150, 350, 45,  53, 77]
        recidivism     <<   [0.1, 1,  0,   0.5, 0.5, 0,  0.1]
        escapedDefects <<   [0,   1,  0,   0,   2,   0,  0]

    }
**/
}