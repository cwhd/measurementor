var angular, describe, beforeEach, module, inject, spyOn, it, expect;
describe("generalLayout", function() {
    var stateName;
    beforeEach(module("generalLayout"));

    beforeEach(inject(function($state) {
        spyOn($state, "go").andCallFake(function(state, params) {
            stateName = state;
        });
    }));

    it("LogOut service test", inject(function(generalLayout) {
        generalLayout.logOut();
        expect(generalLayout.data.showLogOutBtn).toEqual(false);

        generalLayout.checkLogInStatus();
        expect(stateName).toEqual("app.signIn");
    }));
});
