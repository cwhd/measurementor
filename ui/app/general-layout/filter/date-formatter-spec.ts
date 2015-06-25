var angular, jasmine, describe, beforeEach, module, inject, spyOn, it, expect;

var filter;

describe("dateFormatter", function() {

    // beforeEach(module("generalLayout"));

    beforeEach(function() {
        module("generalLayout");

        inject(function(_$filter_) {
            filter = _$filter_;
        });
    });

    it("dateFormatter filter checks", inject(function() {
        var result = filter("dateFormatter")("2015-06-23T15:01:52.047+0000");
        expect(result).toBe("2015-05-23 11:01:52");
    }));

});