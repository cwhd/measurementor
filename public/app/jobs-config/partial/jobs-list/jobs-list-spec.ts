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
    }));

    it("JobsListCtrl general checks", inject(function(generalLayout) {
        expect(generalLayout.data.viewTitle).toEqual("List of jobs");

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

    it("JobsListCtrl onRunJob function checks", inject(function(jobsConfig) {
        spyOn(jobsConfig, "runJob");

        scope.onRunJob("123");
        expect(jobsConfig.runJob).toHaveBeenCalled();
    }));

    it("JobsListCtrl onRunJob function checks", inject(function(jobsConfig) {
        spyOn(jobsConfig, "runJob");

        scope.onRunJob("123");
        expect(jobsConfig.runJob).toHaveBeenCalled();
    }));

    it("JobsListCtrl onChangeJobStatus function checks", inject(function(jobsConfig) {
        spyOn(jobsConfig, "changeJobStatus");
        //httpBackend.expect("POST", "api/jobs-config").respond(200, {});
        scope.onChangeJobStatus(0, "123");
        expect(jobsConfig.changeJobStatus).toHaveBeenCalled();
    }));
});