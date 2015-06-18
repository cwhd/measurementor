declare
var angular: any;

angular.module("userManagement", ["ui.router"]);

angular.module("userManagement").config(function($stateProvider) {

    $stateProvider.state("app.signIn", {
        url: "/signIn",
        templateUrl: "app/user-management/partial/sign-in/sign-in.html"
    });
    /* Add New States Above */

});

