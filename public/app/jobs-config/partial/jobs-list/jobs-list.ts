declare
var angular: any;

angular.module("jobsConfig").controller("JobsListCtrl", function($rootScope, $scope, $state, jobsConfig, generalLayout) {
    generalLayout.checkLogInStatus();
    generalLayout.data.viewTitle = "List of jobs";

    var currentPage = "http://localhost:8080/api/jobs-config?page=0&size=3&sort=name,asc";

    var getData = function(url: string) {
        currentPage = url;
        jobsConfig.getJobs(url).then(function(data) {
            $scope.jobsData = angular.copy(data);
        });
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
        getData($scope.jobsData.links.prev.href);
    };

    $scope.onNext = function() {
        getData($scope.jobsData.links.next.href);
    };
});