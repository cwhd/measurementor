declare
var angular: any;

angular.module("userManagement").factory("userManagement", function($state, generalLayout) {
    var userManagement = {
        logIn: function(loginData) {
            generalLayout.data.showLogOutBtn = true;
            $state.go("app.jobs-list");
        }
    };

    return userManagement;
});
