var angular, describe, beforeEach, module, inject, spyOn, it, expect;

describe("constants", function() {

    beforeEach(module("generalLayout"));

    it("contants global checks", inject(function(constants) {
        expect(constants.mockdata).toEqual(true);
    }));

});
