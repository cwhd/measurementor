declare
var angular: any;

angular.module("generalLayout").factory("generalLayout", function($state, $mdToast) {
    var generalLayout = {
        data: {
            viewTitle: "",
            showLogOutBtn: false,
            toastMessageText: "",
            toastMessageType: ""
        },
        logOut: function() {
            this.data.showLogOutBtn = false;
            $state.go("app.signIn");
        },
        checkLogInStatus: function() {
            if (!this.data.showLogOutBtn) {
                $state.go("app.signIn");
            }
        },
        displayToast: function(parameters) {
            var templateUrl = "app/general-layout/templates/toast-template.html";

            generalLayout.data.toastMessageText = parameters.messageText;
            generalLayout.data.toastMessageType = parameters.messageType;

            var oToast = {
                controller: "ToastCtrl",
                templateUrl: templateUrl,
                hideDelay: 3000,
                position: "top right"
            };

            $mdToast.show(oToast);
        }
    };

    return generalLayout;
});