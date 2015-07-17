declare
var angular;

declare
var versionSettings;

angular.module("generalLayout").controller("GeneralWrapperCtrl", function($scope, generalLayout) {
    $scope.onLogOut = function() {
        generalLayout.logOut();
    };

    $scope.generalLayoutData = generalLayout.data;
    
    $scope.version = versionSettings;
});
