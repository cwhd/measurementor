var angular, describe, beforeEach, module, inject, spyOn, it, expect;

describe("GeneralWrapperCtrl", function() {

    beforeEach(module("generalLayout"));

    var rootScope, scope, ctrl, stateName;

    beforeEach(module(function($provide) {
        $provide.factory("generalLayout", function() { //mocking factory
            return {
                logOut: function() {
                    return;
                }
            };
        });
    }));

    beforeEach(inject(function($rootScope, $controller) {
        scope = $rootScope.$new();
        rootScope = $rootScope;
        ctrl = $controller("GeneralWrapperCtrl", {
            $scope: scope
        });
    }));

    it("GeneralLayoutCtrl global checks", inject(function() {
        scope.onLogOut();
    }));
});
