declare
var angular;

angular.module("jobsConfig", ["ui.router"]);

angular.module("jobsConfig").config(function($stateProvider) {

    $stateProvider.state("app.jobs-list", {
        url: "/jobsList",
        templateUrl: "app/jobs-config/partial/jobs-list/jobs-list.html"
    });
    $stateProvider.state("app.job-details", {
        url: "/jobDetails",
        templateUrl: "app/jobs-config/partial/job-details/job-details.html"
    });
    $stateProvider.state("app.job-history", {
        url: "jobHistory",
        templateUrl: "app/jobs-config/partial/job-history/job-history.html"
    });
    /* Add New States Above */

});
