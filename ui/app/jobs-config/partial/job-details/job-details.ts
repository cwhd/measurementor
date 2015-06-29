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
            name: {
                message: "This field is required",
                show: false
            },
            cron: {
                message: "This field is required",
                show: false
            },
            config: {
                message: "This field is required",
                show: false
            }
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
        var job;
        var isDataValid = true;

        if (!$scope.jobDetailsData.name) {
            $scope.formValidation.name.show = true;
            isDataValid = false;
        }

        if (!$scope.jobDetailsData.cron) {
            $scope.formValidation.cron.show = true;
            isDataValid = false;
        }

        if (!$scope.jobDetailsData.configAsString) {
            $scope.formValidation.config.show = true;
            isDataValid = false;
        } else {
            try {
                $scope.jobDetailsData.config = JSON.parse($scope.jobDetailsData.configAsString);
                job = angular.copy($scope.jobDetailsData);
                delete job.configAsString;
            } catch (e) {
                $scope.formValidation.config.message = "Wrong config value (not a proper JSON string)";
                $scope.formValidation.config.show = true;
                isDataValid = false;
            }
        }

        var onSuccess = function() {
            generalLayout.displayToast({
                messageText: "Job has been successfully saved.",
                messageType: "success"
            });

            $scope.jobDetailsData.configAsString = JSON.stringify(job.config, null, 4);
        };

        var onError = function(errorMessages) {
            if (errorMessages) {
                for (var i = 0; i < errorMessages.length; i++) {
                    $scope.formValidation[errorMessages[i].field].show = true;
                    $scope.formValidation[errorMessages[i].field].message = errorMessages[i].message;
                }
            }
        };

        if (isDataValid) {
            jobsConfig.saveJobConfig(angular.copy(job), onSuccess, onError);
        }
    };

    $scope.onBack = function() {
        $state.go("app.jobs-list");
    };

    $scope.onBlurFormElement = function(event) {
        switch (event.currentTarget.name) {
            case "name":
                if (!event.currentTarget.value) {
                    $scope.formValidation.name.show = true;
                } else {
                    $scope.formValidation.name.show = false;
                }
                break;
            case "cron":
                if (!event.currentTarget.value) {
                    $scope.formValidation.cron.show = true;
                } else {
                    $scope.formValidation.cron.show = false;
                }
                break;
            case "config":
                if (!event.currentTarget.value) {
                    $scope.formValidation.config.show = true;
                } else {
                    $scope.formValidation.config.show = false;
                }
                break;
        }
    };

    $scope.onChangeFormElement = function(elementName) {
        switch (elementName) {
            case "name":
                if (!$scope.jobDetailsData.name) {
                    $scope.formValidation.name.show = true;
                } else {
                    $scope.formValidation.name.show = false;
                }
                break;
            case "cron":
                if (!$scope.jobDetailsData.cron) {
                    $scope.formValidation.cron.show = true;
                } else {
                    $scope.formValidation.cron.show = false;
                }
                break;
            case "config":
                if (!$scope.jobDetailsData.configAsString) {
                    $scope.formValidation.config.show = true;
                } else {
                    $scope.formValidation.config.show = false;
                }
                break;
        }
    };
});