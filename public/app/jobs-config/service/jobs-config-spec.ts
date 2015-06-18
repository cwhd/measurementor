var angular, describe, beforeEach, module, inject, spyOn, it, expect;

describe("jobsConfig", function() {
    var $httpBackend;

    beforeEach(module("jobsConfig"));

    beforeEach(module(function($provide) {
        $provide.factory("constants", function() { //mocking factory
            return {
                mockdata: false
            };
        });

        $provide.factory("generalLayout", function() { //mocking factory
            return {
                checkLogInStatus: function() {
                    return true;
                }
            };
        });
    }));

    beforeEach(inject(function(_$httpBackend_) {
        $httpBackend = _$httpBackend_;
        $httpBackend.when("GET", "api/jobs-config?page=0&size=2&sort=name,asc").respond({
            _embedded: {
                measureMentorJobsConfigDtoes: [{
                    id: 1
                }]
            },
            _links: {},
            page: {}
        });

        $httpBackend.when("GET", "api/jobs-hostory/1?page=0&size=2&sort=endDate,desc").respond({
            _embedded: {
                jobHistoryDtoes: [{
                    id: 1
                }]
            },
            _links: {},
            page: {}
        });
    }));

    it("jobs-config service checks", inject(function(jobsConfig, generalLayout) {
        var jobs;
        jobsConfig.getJobs("api/jobs-config?page=0&size=2&sort=name,asc", false).then(function(data) {
            jobs = data.jobs;
        });
        $httpBackend.flush();

        expect(jobs.length > 0).toBeTruthy();

        jobs = jobsConfig.getJobs("api/jobs-config?page=0&size=2&sort=name,asc", true).jobs;
        expect(jobs.length > 0).toBeTruthy();
    }));

    it("jobHistory service checks", inject(function(jobsConfig) {
        var jobHistories;
        jobsConfig.getJobHistoryData("api/jobs-hostory/1?page=0&size=2&sort=endDate,desc", false).then(function(data) {
            jobHistories = data.jobHistories;
        });
        $httpBackend.flush();

        expect(jobHistories.length > 0).toBeTruthy();

        jobHistories = jobsConfig.getJobHistoryData("api/jobs-hostory/1?page=0&size=2&sort=endDate,desc", true).jobHistories;
        expect(jobHistories.length > 0).toBeTruthy();
    }));
});
