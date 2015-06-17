declare
var angular: any;

angular.module("generalLayout", ["ui.router"]);

angular.module("generalLayout").config(function($stateProvider) {

    $stateProvider.state("app", {
        url: "/app",
        templateUrl: "app/general-layout/partial/general-wrapper/general-wrapper.html"
    });
    /* Add New States Above */
});
