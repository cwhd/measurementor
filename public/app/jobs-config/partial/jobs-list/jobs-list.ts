declare
var angular: any;

angular.module("jobsConfig").controller("JobsListCtrl", function($rootScope, $scope, $state, jobsConfig, generalLayout) {
    generalLayout.checkLogInStatus();
    generalLayout.data.viewTitle = "List of jobs";

    var currentPage = "api/jobs-config?page=0&size=3&sort=name,asc";

    $scope.getData = function(url: string) {
        currentPage = url;
        jobsConfig.getJobs(url).then(function(data) {
            $scope.jobsData = angular.copy(data);
        });
    };

    $scope.getData(currentPage); 

    $scope.onRunJob = function(id) {
        jobsConfig.runJob(id);
    };

    $scope.onChangeJobStatus = function(index, id, status) {
        var onSuccess = function() {
            $scope.jobsData.jobs[index].jobOn = !$scope.jobsData.jobs[index].jobOn;
        };

        jobsConfig.changeJobStatus(id, !status, onSuccess);
    };

    $scope.onAdd = function() {
        $state.go("app.job-details", {
            id: "-1"
        });
    };

    $scope.onDetails = function(id) {
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
        $scope.getData($scope.jobsData.links.prev.href);
    };

    $scope.onNext = function() {
        $scope.getData($scope.jobsData.links.next.href);
    };
});