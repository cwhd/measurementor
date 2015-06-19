declare
var angular;

angular.module("jobsConfig").controller("JobHistoryCtrl", function($scope, $state, $stateParams, jobsConfig, generalLayout) {
    generalLayout.checkLogInStatus();
    generalLayout.data.viewTitle = "Job history";

    $scope.jobId = $stateParams.id;

    var getData = function(url) {
        jobsConfig.getJobHistoryData(url).then(function(data) {
            $scope.jobHistoryData = angular.copy(data);
        });
    };

    var url = "api/jobs-history/" + $scope.jobId + "?page=0&size=5&sort=endDate,desc";

    getData(url);

    $scope.onPrevious = function() {
        getData($scope.jobHistoryData.links.prev.href);
    };

    $scope.onNext = function() {
        getData($scope.jobHistoryData.links.next.href);
    };

    $scope.onBack = function() {
        $state.go("app.jobs-list");
    };
});