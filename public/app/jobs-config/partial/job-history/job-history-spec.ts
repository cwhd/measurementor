var describe, beforeEach, module, inject, spyOn, it, expect;

describe("JobHistoryCtrl", function() {

    beforeEach(module("jobsConfig"));

    var rootScope, scope, ctrl, stateName;

    beforeEach(module(function($provide) {
        $provide.factory("jobsConfig", function($q) { //mocking factory
            return {
                getJobHistoryData: function() {
                    var deferred = $q.defer();
                    return deferred.promise;
                }
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
        });
    }));

    beforeEach(inject(function($rootScope, $controller) {
        scope = $rootScope.$new();
        ctrl = $controller("JobHistoryCtrl", {
            $scope: scope
        });
    }));

    it("JobHistoryListCtrl global checks", inject(function(generalLayout) {
        expect(generalLayout.data.viewTitle).toEqual("Job history");

        scope.onBack();
        expect(stateName).toEqual("app.jobs-list");
    }));

});
