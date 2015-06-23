declare
var angular: any;

angular.module("measurementor", ["ui.router", "ngMaterial", "ngMessages", "generalLayout", "userManagement", "jobsConfig"]);

angular.module("measurementor").config(function($stateProvider, $urlRouterProvider, $mdThemingProvider) {
    /* Add New States Above */
    $urlRouterProvider.otherwise("/app/signIn");
});

angular.module("measurementor").run(function($rootScope) {
    $rootScope.safeApply = function(fn) {
        var phase = $rootScope.$$phase;
        if (phase === "$apply" || phase === "$digest") {
            if (fn && (typeof(fn) === "function")) {
                fn();
            }
        } else {
            this.$apply(fn);
        }
    };
});