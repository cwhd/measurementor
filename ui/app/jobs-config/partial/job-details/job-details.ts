declare
var angular;

angular.module("jobsConfig").controller("JobDetailsCtrl", function($scope, $state, $http, $stateParams, jobsConfig, generalLayout) {
    generalLayout.checkLogInStatus();
    generalLayout.data.viewTitle = "Job details";

    var initJobDetailsData = function() {
        $scope.jobDetailsData = {
            name: "",
            cron: "",
            jobOn: false,
            configAsString: ""
        };
    };

    var initFormValidation = function() {
        $scope.formValidation = {
            showNameValidation: false,
            showCronValidation: false,
            showConfigValidation: false
        };
    };

    if ($stateParams.id !== "-1") {
        jobsConfig.getJobConfig($stateParams.id).then(function(data) {
            $scope.jobDetailsData = angular.copy(data);
        });
    } else {
        initJobDetailsData();
    }

    initFormValidation();

    $scope.onSave = function() {
        if (!$scope.jobDetailsData.name || !$scope.jobDetailsData.cron || !$scope.jobDetailsData.configAsString) {
            if (!$scope.jobDetailsData.name) {
                $scope.formValidation.showNameValidation = true;
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

        var onSuccess = function() {
            $state.go("app.jobs-list");
            generalLayout.displayToast({
                messageText: "Job has been successfully saved.",
                messageType: "success"
            });
        };

        var onError = function() {
            $state.go("app.jobs-list");
        };

        try {
            $scope.jobDetailsData.config = JSON.parse($scope.jobDetailsData.configAsString);
            delete $scope.jobDetailsData.configAsString;
        } catch (e) {
            generalLayout.displayToast({
                messageText: "Wrong config value (not a proper JSON string).",
                messageType: "error"
            });

            return;
        }

        jobsConfig.saveJobConfig(angular.copy($scope.jobDetailsData), onSuccess, onError);
    };

    $scope.onBack = function() {
        $state.go("app.jobs-list");
    };

    $scope.onBlurFormElement = function(event) {
        switch (event.currentTarget.name) {
            case "name":
                if (!event.currentTarget.value) {
                    $scope.formValidation.showNameValidation = true;
                } else {
                    $scope.formValidation.showNameValidation = false;
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
            case "name":
                if (!$scope.jobDetailsData.name) {
                    $scope.formValidation.showNameValidation = true;
                } else {
                    $scope.formValidation.showNameValidation = false;
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