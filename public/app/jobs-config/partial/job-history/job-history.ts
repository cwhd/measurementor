declare
var angular;

angular.module("jobsConfig").controller("JobHistoryCtrl", function($scope, $state, $stateParams, jobsConfig, generalLayout) {
    generalLayout.checkLogInStatus();
    generalLayout.data.viewTitle = "Job history";

    $scope.jobId = $stateParams.id;
    // $scope.jobHistories = [];
    // $scope.page = {};
    // var links = {
    //     next: {
    //         href: ""
    //     },
    //     prev: {
    //         href: ""
    //     }
    // };

    var getData = function(url) {
        jobsConfig.getJobHistoryData(url).then(function(data) {
            $scope.jobHistoryData.angular.copy(data);
        });
    };

    // getData("http://localhost:8080/api/jobs-hostory/" + $scope.jobId + "?page=0&size=5&sort=endDate,desc");
    var url = "http://localhost:8080/api/jobs-hostory/" + $scope.jobId + "?page=0&size=5&sort=endDate,desc";

    getData(url);
    // jobsConfig.getJobHistoryData(url).then(function(data) {
    //     $scope.jobHistoryData.angular.copy(data);
    // });
    $scope.onPrevious = function() {
        getData($scope.links.prev.href);
    };

    $scope.onNext = function() {
        getData($scope.links.next.href);
    };

    $scope.onBack = function() {
        $state.go("app.jobs-list");
    };
});