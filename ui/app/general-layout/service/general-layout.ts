declare
var angular: any;

angular.module("generalLayout").factory("generalLayout", function($state) {
    var generalLayout = {
        data: {
            viewTitle: "",
            showLogOutBtn: false
        },
        logOut: function() {
            this.data.showLogOutBtn = false;
            $state.go("app.signIn");
        },
        checkLogInStatus: function() {
            if (!this.data.showLogOutBtn) {
                $state.go("app.signIn");
            }
        }
    };

    return generalLayout;
});