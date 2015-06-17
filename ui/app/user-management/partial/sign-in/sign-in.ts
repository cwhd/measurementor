declare
var angular: any;

angular.module("userManagement").controller("SignInCtrl", function($scope, userManagement, generalLayout) {
    generalLayout.data.viewTitle = "Log In";

    $scope.formValidation = {
        showUserNameValidation: false,
        showPasswordValidation: false
    };

    var initLoginData = function() {
        $scope.loginData = {
            userName: "",
            password: ""
        };
    };

    initLoginData();

    $scope.onLogIn = function(event) {
        if (!$scope.loginData.userName || !$scope.loginData.password) {
            if (!$scope.loginData.userName) {
                $scope.formValidation.showUserNameValidation = true;
            }

            if (!$scope.loginData.password) {
                $scope.formValidation.showPasswordValidation = true;
            }
            return;
        }

        userManagement.logIn($scope.loginData);
    };

    $scope.onBlurFormElement = function(event) {
        switch (event.currentTarget.name) {
            case "userName":
                if (!event.currentTarget.value) {
                    $scope.formValidation.showUserNameValidation = true;
                } else {
                    $scope.formValidation.showUserNameValidation = false;
                }
                break;
            case "password":
                if (!event.currentTarget.value) {
                    $scope.formValidation.showPasswordValidation = true;
                } else {
                    $scope.formValidation.showPasswordValidation = false;
                }
                break;
        }
    };

    $scope.onChangeFormElement = function(elementName) {
        switch (elementName) {
            case "userName":
                if (!$scope.loginData.userName) {
                    $scope.formValidation.showUserNameValidation = true;
                } else {
                    $scope.formValidation.showUserNameValidation = false;
                }
                break;
            case "password":
                if (!$scope.loginData.password) {
                    $scope.formValidation.showPasswordValidation = true;
                } else {
                    $scope.formValidation.showPasswordValidation = false;
                }
                break;
        }
    };
});
