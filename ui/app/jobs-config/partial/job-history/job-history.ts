declare
var angular;

angular.module("jobsConfig").controller("JobHistoryCtrl", function($scope, $state, $stateParams, jobsConfig, generalLayout) {
    generalLayout.checkLogInStatus();
    generalLayout.data.viewTitle = "Job history";

    $scope.jobid = $stateParams.id;
    $scope.jobHistories = [];
    $scope.page = {};
    var links = {
        next: {
            href: ""
        },
        prev: {
            href: ""
        }
    };

    var getData = function(path) {
        var jobHistoryData = jobsConfig.getJobHistoryData(path);
        $scope.jobHistories = jobHistoryData.jobHistories;
        links = angular.copy(jobHistoryData.links);
        $scope.page = angular.copy(jobHistoryData.page);
    };

    getData("api/jobs-hostory/" + $scope.jobid + "?page=0&size=2&sort=endDate,desc");

    $scope.onPrevious = function() {
        getData(links.prev.href);
    };

    $scope.onNext = function() {
        getData(links.next.href);
    };

    $scope.onBack = function() {
        $state.go("app.jobs-list");
    };
});
