declare
var angular: any;

angular.module("generalLayout", ["ui.router"]);

angular.module("generalLayout").config(function($stateProvider) {

    $stateProvider.state("app", {
        url: "/app",
        templateUrl: "app/general-layout/partial/general-wrapper/general-wrapper.html"
    });
    /* Add New States Above */
});

angular.module("generalLayout").controller("ToastCtrl", function($scope, $mdToast, generalLayout) {
    if (angular.isArray(generalLayout.data.toastMessageText)) {
        $scope.isArrayOfMessages = true;
    }

    $scope.messageText = generalLayout.data.toastMessageText;

    $scope.messageType = generalLayout.data.toastMessageType;
});