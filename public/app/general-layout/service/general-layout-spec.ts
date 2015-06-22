var angular, describe, beforeEach, module, inject, spyOn, it, expect;
describe("generalLayout", function() {
    var stateName;
    beforeEach(module("generalLayout", "ngMaterial"));

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

    it("displayToast method", inject(function(generalLayout, $mdToast) {
        spyOn($mdToast, "show");

        generalLayout.displayToast({
            messageText: "testMessage",
            messageType: "error"
        });

        expect(generalLayout.data.toastMessageText).toEqual("testMessage");
        expect(generalLayout.data.toastMessageType).toEqual("error");
        expect($mdToast.show).toHaveBeenCalled();
    }));
});