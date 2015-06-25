var angular, describe, beforeEach, module, inject, spyOn, it, expect;

describe("JobsListCtrl", function() {
    var rootScope, scope, ctrl, stateName, stateParams, httpBackend;

    beforeEach(module("jobsConfig"));

    beforeEach(module(function($provide) {
        $provide.factory("constants", function() { //mocking factory
            return {
                mockdata: false
            };
        });

        $provide.factory("generalLayout", function() { //mocking factory
            return {
                data: {},
                checkLogInStatus: function() {
                    return true;
                }
            };
        });
    }));

    beforeEach(inject(function($state, _$httpBackend_) {
        httpBackend = _$httpBackend_;
        spyOn($state, "go").andCallFake(function(state, params) {
            stateName = state;
            stateParams = angular.copy(params);
        });
    }));

    beforeEach(inject(function($rootScope, $controller) {
        scope = $rootScope.$new();
        ctrl = $controller("JobsListCtrl", {
            $scope: scope
        });

        scope.jobsData = {
            links: {
                prev: {
                    href: ""
                },
                next: {
                    href: ""
                }
            }
        };
    }));

    it("JobsListCtrl general checks", inject(function(generalLayout) {
        expect(generalLayout.data.viewTitle).toEqual("List of jobs");
        expect(scope.showSpinner).toEqual(true);

        scope.onAdd();
        expect(stateName).toEqual("app.job-details");
        expect(stateParams).toEqual({
            id: "-1"
        });

        scope.onDetails("1");
        expect(stateName).toEqual("app.job-details");
        expect(stateParams).toEqual({
            id: "1"
        });

        scope.onHistory("1");
        expect(stateName).toEqual("app.job-history");
        expect(stateParams).toEqual({
            id: "1"
        });
    }));

    it("JobsListCtrl onChangeJobStatus function checks", inject(function(jobsConfig) {
        spyOn(jobsConfig, "saveJobConfig");

        scope.onChangeJobStatus(0, {
            id: "1",
            name: "test",
            jobOn: true,
            cron: "0 * * * * MON-FRI",
            config: {}
        });
        expect(jobsConfig.saveJobConfig).toHaveBeenCalled();
    }));

    it("JobsListCtrl onRunJob function checks", inject(function(jobsConfig) {
        spyOn(jobsConfig, "runJob");

        scope.onRunJob("123");
        expect(jobsConfig.runJob).toHaveBeenCalled();
    }));

    it("JobsListCtrl onPrevious function checks", inject(function(jobsConfig) {
        spyOn(scope, "getData");

        scope.onPrevious();
        expect(scope.showSpinner).toEqual(true);
        expect(scope.getData).toHaveBeenCalled();
    }));

    it("JobsListCtrl onNext function checks", inject(function(jobsConfig) {
        spyOn(scope, "getData");

        scope.onNext();
        expect(scope.showSpinner).toEqual(true);
        expect(scope.getData).toHaveBeenCalled();
    }));
});