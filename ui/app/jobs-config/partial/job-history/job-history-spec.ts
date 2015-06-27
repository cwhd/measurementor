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

        scope.jobHistoryData = {
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

    it("JobHistoryListCtrl global checks", inject(function(generalLayout) {
        expect(generalLayout.data.viewTitle).toEqual("Job history");
        expect(scope.showSpinner).toEqual(true);

        scope.onBack();
        expect(stateName).toEqual("app.jobs-list");
    }));

    it("JobHistoryListCtrl onPrevious function checks", inject(function(jobsConfig) {
        spyOn(scope, "getData");

        scope.onPrevious();
        expect(scope.showSpinner).toEqual(true);
        expect(scope.getData).toHaveBeenCalled();
    }));

    it("JobHistoryListCtrl onNext function checks", inject(function(jobsConfig) {
        spyOn(scope, "getData");

        scope.onNext();
        expect(scope.showSpinner).toEqual(true);
        expect(scope.getData).toHaveBeenCalled();
    }));
});