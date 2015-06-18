var angular, describe, beforeEach, module, inject, spyOn, it, expect;

describe("SignInCtrl", function() {

    var scope, ctrl;

    beforeEach(module("userManagement"));

    beforeEach(module(function($provide) {
        $provide.factory("userManagement", function() {
            return {
                logIn: function(loginData) {
                    return {};
                }
            };
        });

        $provide.factory("generalLayout", function() { //mocking factory
            return {
                data: {}
            };
        });
    }));

    beforeEach(inject(function($rootScope, $controller) {
        scope = $rootScope.$new();
        ctrl = $controller("SignInCtrl", {
            $scope: scope
        });
    }));

    it("Form validation checks (onBlur logic)", inject(function() {
        var event = {
            currentTarget: {
                name: "userName",
                value: ""
            }
        };
        scope.onBlurFormElement(event);
        expect(scope.formValidation.showUserNameValidation).toEqual(true);

        event = {
            currentTarget: {
                name: "userName",
                value: "Oleg"
            }
        };
        scope.onBlurFormElement(event);
        expect(scope.formValidation.showUserNameValidation).toEqual(false);

        event = {
            currentTarget: {
                name: "password",
                value: ""
            }
        };
        scope.onBlurFormElement(event);
        expect(scope.formValidation.showPasswordValidation).toEqual(true);

        event = {
            currentTarget: {
                name: "password",
                value: "123"
            }
        };
        scope.onBlurFormElement(event);
        expect(scope.formValidation.showPasswordValidation).toEqual(false);
    }));

    it("Form validation checks (onChange logic)", inject(function() {
        scope.loginData = {
            userName: "",
            password: ""
        };
        scope.onChangeFormElement("userName");
        expect(scope.formValidation.showUserNameValidation).toEqual(true);
        scope.onChangeFormElement("password");
        expect(scope.formValidation.showPasswordValidation).toEqual(true);

        scope.loginData = {
            userName: "Oleg",
            password: "123"
        };
        scope.onChangeFormElement("userName");
        expect(scope.formValidation.showUserNameValidation).toEqual(false);
        scope.onChangeFormElement("password");
        expect(scope.formValidation.showPasswordValidation).toEqual(false);
    }));

    it("Form validation checks (onLogin logic)", inject(function() {
        scope.loginData = {
            userName: "Oleg",
            password: "123"
        };
        scope.onLogIn();
        expect(scope.formValidation.showUserNameValidation).toEqual(false);
        expect(scope.formValidation.showPasswordValidation).toEqual(false);

        scope.loginData = {
            userName: "",
            password: ""
        };
        scope.onLogIn();
        expect(scope.formValidation.showUserNameValidation).toEqual(true);
        expect(scope.formValidation.showPasswordValidation).toEqual(true);
    }));
});