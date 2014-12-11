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

    void "Test time-estimate ratio for a fibonacci estimates"() {
        def estimateHealth = utilitiesService.estimateHealth(estimate, actualTime, 13, 9, fibonacciSeries)
        expect: estimateHealth == 0

        where:
            estimate <<     [1, 2, 2, 3, 3, 5, 5, 8, 8, 8, 8, 13, 13, 13]
            actualTime <<   [1, 1, 2, 2, 3, 3, 4, 4, 5, 6, 6, 7, 8, 9]
    }

    void "Test time-estimate ratio for power of two estimates"() {
        def estimateHealth = utilitiesService.estimateHealth(estimate, actualTime, 13, 9, powerOfTwoSeries)
        expect: estimateHealth == 0

        where:
        estimate <<     [1, 2, 2, 4, 4, 4, 8, 8, 8, 8, 16, 16, 16]
        actualTime <<   [1, 1, 2, 2, 3, 4, 4, 5, 6, 7, 8, 8, 9]
    }


}

/**
 16 points		9 days		7-9 days		devTime:[7 TO 9] AND storyPoints:16
 8 points		4.5 days		4-7 days		devTime:[0 TO 1] AND storyPoints:8
 4 points		2.25 days		2-4 days		devTime:[2 TO 4] AND storyPoints:4
 2 points		1.125 days	1-2 days		devTime:[1 TO 2] AND storyPoints:2
 1 point		.56 days		less than 1 day	devTime:[0 TO 1] AND storyPoints:1
 Fibbonaci series estimation with 2 week sprints
 13 points		9		6-9 days
 8 points		5.53		4-6 days
 5 points		3.46		3-4 days
 3 points		2.07		2-3 days
 2 points		1.38		1-2 days
 1 point		.69		less than a day
 **/