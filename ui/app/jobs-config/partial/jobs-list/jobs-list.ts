declare
var angular: any;

angular.module("jobsConfig").controller("JobsListCtrl", function($rootScope, $scope, $state, jobsConfig, generalLayout) {
    generalLayout.checkLogInStatus();
    generalLayout.data.viewTitle = "List of jobs";

    $scope.jobs = [];
    $scope.page = {};

    var links = {
        next: {
            href: ""
        },
        prev: {
            href: ""
        }
    };
    var currentPage = "api/jobs-config?page=0&size=2&sort=name,asc";

    var getData = function(url: string) {
        currentPage = url;
        var jobsData = jobsConfig.getJobs(url);
        $scope.jobs = jobsData.jobs;
        links = angular.copy(jobsData.links);
        $scope.page = angular.copy(jobsData.page);
    };

    getData(currentPage);

    $scope.runJob = function(id) {
        jobsConfig.runJob(id, getData);
    };

    $scope.onAdd = function() {
        $state.go("app.job-details", {
            id: "-1"
        });
    };

    $scope.onEdit = function(id) {
        $state.go("app.job-details", {
            id: id
        });
    };

    $scope.onHistory = function(id) {
        $state.go("app.job-history", {
            id: id
        });
    };

    $scope.onPrevious = function() {
        getData(links.prev.href);
    };

    $scope.onNext = function() {
        getData(links.next.href);
    };
});
