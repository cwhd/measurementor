var angular, describe, beforeEach, module, inject, spyOn, it, expect;

describe("JobDetailsCtrl", function() {

    beforeEach(module("jobsConfig"));

    var rootScope, scope, ctrl, stateName, stateParams;

    beforeEach(module(function($provide) {
        $provide.factory("jobsConfig", function() { //mocking factory
            return {
                getJobConfig: function() {
                    return {};
                },
                saveJobConfig: function(data, onSuccess, onError, successForTesting) {
                    if (successForTesting) {
                        onSuccess();
                    } else {
                        onError();
                    }
                }
            };
        });
        $provide.factory("generalLayout", function() { //mocking factory
            return {
                data: {},
                checkLogInStatus: function() {
                    return true;
                }
            };
        });
    }));

    beforeEach(inject(function($state) {
        spyOn($state, "go").andCallFake(function(state, params) {
            stateName = state;
            stateParams = angular.copy(params);
        });
    }));

    beforeEach(inject(function($rootScope, $controller) {
        scope = $rootScope.$new();
        ctrl = $controller("JobDetailsCtrl", {
            $scope: scope
        });
    }));

    it("JobsConfigDetailsCtrl global checks", inject(function(generalLayout) {
        expect(generalLayout.data.viewTitle).toEqual("Job details");

        scope.onBack();
        expect(stateName).toEqual("app.jobs-list");
    }));

    it("Form validation checks (onBlur logic)", inject(function() {
        var event = {
            currentTarget: {
                name: "jobName",
                value: ""
            }
        };
        scope.onBlurFormElement(event);
        expect(scope.formValidation.showJobNameValidation).toEqual(true);

        event = {
            currentTarget: {
                name: "cron",
                value: ""
            }
        };
        scope.onBlurFormElement(event);
        expect(scope.formValidation.showCronValidation).toEqual(true);

        event = {
            currentTarget: {
                name: "configAsString",
                value: ""
            }
        };
        scope.onBlurFormElement(event);
        expect(scope.formValidation.showConfigValidation).toEqual(true);

        event = {
            currentTarget: {
                name: "jobName",
                value: "TestJob"
            }
        };
        scope.onBlurFormElement(event);
        expect(scope.formValidation.showJobNameValidation).toEqual(false);

        event = {
            currentTarget: {
                name: "cron",
                value: "123"
            }
        };
        scope.onBlurFormElement(event);
        expect(scope.formValidation.showCronValidation).toEqual(false);

        event = {
            currentTarget: {
                name: "configAsString",
                value: "test"
            }
        };
        scope.onBlurFormElement(event);
        expect(scope.formValidation.showConfigValidation).toEqual(false);
    }));

    it("Form validation checks (onChange logic)", inject(function() {
        scope.jobDetailsData = {
            jobName: "",
            cron: "",
            configAsString: ""
        };
        scope.onChangeFormElement("jobName");
        expect(scope.formValidation.showJobNameValidation).toEqual(true);
        scope.onChangeFormElement("cron");
        expect(scope.formValidation.showCronValidation).toEqual(true);
        scope.onChangeFormElement("configAsString");
        expect(scope.formValidation.showConfigValidation).toEqual(true);

        scope.jobDetailsData = {
            jobName: "TestJob",
            cron: "123",
            configAsString: "test"
        };
        scope.onChangeFormElement("jobName");
        expect(scope.formValidation.showJobNameValidation).toEqual(false);
        scope.onChangeFormElement("cron");
        expect(scope.formValidation.showCronValidation).toEqual(false);
        scope.onChangeFormElement("configAsString");
        expect(scope.formValidation.showConfigValidation).toEqual(false);
    }));

    it("Form validation checks (onSave logic)", inject(function() {
        scope.jobDetailsData = {
            jobName: "",
            cron: "",
            configAsString: ""
        };
        scope.onSave();
        expect(scope.formValidation.showJobNameValidation).toEqual(true);
        expect(scope.formValidation.showCronValidation).toEqual(true);
        expect(scope.formValidation.showConfigValidation).toEqual(true);

        scope.jobDetailsData = {
            jobName: "TestJob",
            cron: "123",
            configAsString: "test"
        };
        scope.onSave(true);
        expect(scope.formValidation.showJobNameValidation).toEqual(false);
        expect(scope.formValidation.showCronValidation).toEqual(false);
        expect(scope.formValidation.showConfigValidation).toEqual(false);
        expect(stateName).toEqual("app.jobs-list");

        scope.onSave(false);
        expect(stateName).toEqual("app.jobs-list");
    }));
});
