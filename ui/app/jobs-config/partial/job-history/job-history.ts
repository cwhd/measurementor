declare
var angular;

angular.module("jobsConfig").controller("JobHistoryCtrl", function($scope, $state, $stateParams, $timeout, jobsConfig, generalLayout) {
    generalLayout.checkLogInStatus();
    generalLayout.data.viewTitle = "Job history";
    $scope.showSpinner = true;

    $scope.jobId = $stateParams.id;

    $scope.getData = function(url) {
        jobsConfig.getJobHistoryData(url).then(function(data) {
            $scope.jobHistoryData = angular.copy(data);
        });
        $timeout(function() {
            $scope.showSpinner = false;
        }, 250);
    };

    var url = "api/jobs-history/" + $scope.jobId + "?page=0&size=5&sort=endDate,desc";

    $scope.getData(url);

    $scope.onPrevious = function() {
        $scope.getData($scope.jobHistoryData.links.prev.href);
    };

    $scope.onNext = function() {
        $scope.showSpinner = true;
        $scope.getData($scope.jobHistoryData.links.next.href);
    };

    $scope.onBack = function() {
        $scope.showSpinner = true;
        $state.go("app.jobs-list");
    };
});