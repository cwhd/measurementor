declare
var angular: any;

angular.module("generalLayout").factory("constants", function() {

    var constants = {mockdata: false};

    constants.mockdata = true;

    return constants;
});
