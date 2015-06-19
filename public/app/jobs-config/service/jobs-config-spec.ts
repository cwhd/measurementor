var angular, describe, beforeEach, module, inject, spyOn, it, expect, jasmine;

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

        $httpBackend.when("GET", "api/jobs-history/1?page=0&size=2&sort=endDate,desc").respond({
            _embedded: {
                jobHistoryDtoes: [{
                    id: 1
                }]
            },
            _links: {},
            page: {}
        });

        // $httpBackend.when("POST", "api/jobs-config").respond(200, {});
    }));

    it("getJobs method checks", inject(function(jobsConfig, generalLayout) {
        var jobs;
        jobsConfig.getJobs("api/jobs-config?page=0&size=2&sort=name,asc", false).then(function(data) {
            jobs = data.jobs;
        });
        $httpBackend.flush();

        expect(jobs.length > 0).toBeTruthy();

        jobs = jobsConfig.getJobs("api/jobs-config?page=0&size=2&sort=name,asc", true).jobs;
        expect(jobs.length > 0).toBeTruthy();
    }));

    it("getJobConfig method checks", inject(function(jobsConfig, generalLayout) {
        $httpBackend.expect("GET", "api/jobs-config/123").respond(200, {
            data: {
                config: {
                    url: "_"
                }
            }
        });
        jobsConfig.getJobConfig("123").then(function(data) {
            expect(data.configAsString !== "").toBeTruthy();

        });
        $httpBackend.flush();
    }));

    it("getJobHistoryData method checks", inject(function(jobsConfig) {
        var jobHistories;
        jobsConfig.getJobHistoryData("api/jobs-history/1?page=0&size=2&sort=endDate,desc", false).then(function(data) {
            jobHistories = data.jobHistories;
        });
        $httpBackend.flush();

        expect(jobHistories.length > 0).toBeTruthy();

        jobHistories = jobsConfig.getJobHistoryData("api/jobs-hostory/1?page=0&size=2&sort=endDate,desc", true).jobHistories;
        expect(jobHistories.length > 0).toBeTruthy();
    }));

    it("changeJobStatus method checks", inject(function(jobsConfig) {
        $httpBackend.expect("POST", "api/jobs-config").respond(200, {});

        var onSuccess = jasmine.createSpy();
        var onError = jasmine.createSpy();
        jobsConfig.changeJobStatus({id: "1"}, onSuccess, onError);
        $httpBackend.flush();
        expect(onSuccess).toHaveBeenCalled();

        $httpBackend.resetExpectations();

        $httpBackend.expect("POST", "api/jobs-config").respond(500, {});
        jobsConfig.changeJobStatus({id: "1"}, onSuccess, onError);
        $httpBackend.flush();
        expect(onError).toHaveBeenCalled();
    }));

    it("saveJobConfig method checks", inject(function(jobsConfig) {
        $httpBackend.expect("POST", "api/jobs-config").respond(200, {});

        var onSuccess = jasmine.createSpy();
        var onError = jasmine.createSpy();
        jobsConfig.saveJobConfig({
            configAsString: "{}"
        }, onSuccess, onError);
        $httpBackend.flush();
        expect(onSuccess).toHaveBeenCalled();

        $httpBackend.resetExpectations();

        $httpBackend.expect("POST", "api/jobs-config").respond(500, {});
        jobsConfig.saveJobConfig({
            configAsString: "{}"
        }, onSuccess, onError);
        $httpBackend.flush();
        expect(onError).toHaveBeenCalled();
    }));

    it("runJob method checks", inject(function(jobsConfig) {
        $httpBackend.expect("POST", "api/run-job/123").respond(200, {});

        var onSuccess = jasmine.createSpy();
        var onError = jasmine.createSpy();
        jobsConfig.runJob("123", onSuccess, onError);
        $httpBackend.flush();
        expect(onSuccess).toHaveBeenCalled();

        $httpBackend.resetExpectations();

        $httpBackend.expect("POST", "api/run-job/123").respond(500, {});
        jobsConfig.runJob("123", onSuccess, onError);
        $httpBackend.flush();
        expect(onError).toHaveBeenCalled();
    }));
});