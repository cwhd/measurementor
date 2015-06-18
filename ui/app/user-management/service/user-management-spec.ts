var angular, describe, beforeEach, module, inject, spyOn, it, expect;

describe("userManagement", function() {

    var stateName;

    beforeEach(module("userManagement"));
    beforeEach(module("generalLayout"));

    beforeEach(inject(function($state) {
        spyOn($state, "go").andCallFake(function(state, params) {
            stateName = state;
        });
    }));

    it("LogIn service test", inject(function(userManagement, generalLayout) {
        userManagement.logIn({});
        expect(generalLayout.data.showLogOutBtn).toEqual(true);
        expect(stateName).toEqual("app.jobs-list"); 


        
    }));
});
