declare
var angular: any;

angular.module("jobsConfig").controller("JobsListCtrl", function($rootScope, $scope, $state, $timeout, jobsConfig, generalLayout) {
    generalLayout.checkLogInStatus();
    generalLayout.data.viewTitle = "List of jobs";
    $scope.showSpinner = true;


    var currentPage = "api/jobs-config?page=" + generalLayout.data.jobsListCurrentPageNumber + "&size=5&sort=name,asc";

    $scope.getData = function(url: string) {
        currentPage = url;
        jobsConfig.getJobs(url).then(function(data) {
            $scope.jobsData = angular.copy(data);
        });
        $timeout(function() {
            $scope.showSpinner = false;
        }, 250);
    };

    $scope.getData(currentPage);

    $scope.onRunJob = function(id) {
        jobsConfig.runJob(id);
    };

    $scope.onChangeJobStatus = function(index, job) {
        var onSuccess = function() {
            $scope.jobsData.jobs[index].jobOn = !$scope.jobsData.jobs[index].jobOn;
            generalLayout.displayToast({
                messageText: "Job status has been successfully updated.",
                messageType: "success"
            });
        };

        var jobData = {
            id: job.id,
            name: job.name,
            jobOn: !job.jobOn,
            cron: job.cron,
            config: job.config
        };

        jobsConfig.saveJobConfig(jobData, onSuccess);
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
        generalLayout.data.jobsListCurrentPageNumber--;
        $scope.showSpinner = true;
        $scope.getData($scope.jobsData.links.prev.href);
    };

    $scope.onNext = function() {
        generalLayout.data.jobsListCurrentPageNumber++;
        $scope.showSpinner = true;
        $scope.getData($scope.jobsData.links.next.href);
    };
});