declare
var angular; 

angular.module("jobsConfig").controller("JobDetailsCtrl", function($scope, $state, $http, $stateParams, jobsConfig, generalLayout) {
    generalLayout.checkLogInStatus();
    generalLayout.data.viewTitle = "Job details";

    var initJobDetailsData = function() {
        $scope.jobDetailsData = {
            jobName: "",
            cron: "",
            jobOn: false,
            configAsString: ""
        };
    };

    var initFormValidation = function() {
        $scope.formValidation = {
            showJobNameValidation: false,
            showCronValidation: false,
            showConfigValidation: false
        };
    };

    if ($stateParams.id !== "-1") {
        // $scope.jobDetailsData = jobsConfig.getJobConfig($stateParams.id);
        // $scope.jobDetailsData.configAsString = JSON.stringify($scope.jobDetailsData);
        initJobDetailsData();
    } else {
        initJobDetailsData();
    }

    initFormValidation();

    $scope.onSave = function(successForTesting) {
        if (!$scope.jobDetailsData.jobName || !$scope.jobDetailsData.cron || !$scope.jobDetailsData.configAsString) {
            if (!$scope.jobDetailsData.jobName) {
                $scope.formValidation.showJobNameValidation = true;
            }

            if (!$scope.jobDetailsData.cron) {
                $scope.formValidation.showCronValidation = true;
            }

            if (!$scope.jobDetailsData.configAsString) {
                $scope.formValidation.showConfigValidation = true;
            }
            return;
        }
        initFormValidation();
        //$scope.jobDetailsData.config = JSON.parse($scope.jobDetailsData.configAsString);

        var onSuccess = function() {
            $state.go("app.jobs-list");
        };

        var onError = function() {
            $state.go("app.jobs-list");
        };

        jobsConfig.saveJobConfig($scope.jobDetailsData, onSuccess, onError, successForTesting);
    };

    $scope.onBack = function() {
        $state.go("app.jobs-list");
    };

    $scope.onBlurFormElement = function(event) {
        switch (event.currentTarget.name) {
            case "jobName":
                if (!event.currentTarget.value) {
                    $scope.formValidation.showJobNameValidation = true;
                } else {
                    $scope.formValidation.showJobNameValidation = false;
                }
                break;
            case "cron":
                if (!event.currentTarget.value) {
                    $scope.formValidation.showCronValidation = true;
                } else {
                    $scope.formValidation.showCronValidation = false;
                }
                break;
            case "configAsString":
                if (!event.currentTarget.value) {
                    $scope.formValidation.showConfigValidation = true;
                } else {
                    $scope.formValidation.showConfigValidation = false;
                }
                break;
        }
    };

    $scope.onChangeFormElement = function(elementName) {
        switch (elementName) {
            case "jobName":
                if (!$scope.jobDetailsData.jobName) {
                    $scope.formValidation.showJobNameValidation = true;
                } else {
                    $scope.formValidation.showJobNameValidation = false;
                }
                break;
            case "cron":
                if (!$scope.jobDetailsData.cron) {
                    $scope.formValidation.showCronValidation = true;
                } else {
                    $scope.formValidation.showCronValidation = false;
                }
                break;
            case "configAsString":
                if (!$scope.jobDetailsData.configAsString) {
                    $scope.formValidation.showConfigValidation = true;
                } else {
                    $scope.formValidation.showConfigValidation = false;
                }
                break;
        }
    };
});
