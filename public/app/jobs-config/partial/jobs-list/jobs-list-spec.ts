var angular, describe, beforeEach, module, inject, spyOn, it, expect;

describe("JobsListCtrl", function() {
    var rootScope, scope, ctrl, stateName, stateParams;

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

    beforeEach(inject(function($state) {
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

        scope.onEdit("1");
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
});
