declare
var browser: any;
declare
var element: any; //, element, describe, by, it, expect;
declare
var describe: any;
declare
var it: any;
declare
var expect: any;
declare
var by: any;

describe("Navigation test", function() {
    it("Navigation test", function() {
        browser.get("http://localhost:8080");

        element(by.id("UserName")).sendKeys("Oleg");
        element(by.id("Password")).sendKeys("123");
        element(by.id("LogInBtn")).click();

        var sValue = element(by.id("ToolbarLabel")).getText();
        expect(sValue).toEqual("List of jobs");

        element.all(by.css(".fa-folder")).get(0).click();
        element(by.css(".fa-eye")).click();
        sValue = element(by.id("ToolbarLabel")).getText();
        expect(sValue).toEqual("Job details");

        element(by.id("BackBtn")).click();
        sValue = element(by.id("ToolbarLabel")).getText();
        expect(sValue).toEqual("List of jobs");

        element.all(by.css(".fa-folder")).get(0).click();
        element(by.css(".fa-list")).click();
        sValue = element(by.id("ToolbarLabel")).getText();
        expect(sValue).toEqual("Job history");

        element(by.id("BackBtn")).click();
        sValue = element(by.id("ToolbarLabel")).getText();
        expect(sValue).toEqual("List of jobs");

        element(by.id("AddJobBtn")).click();
        sValue = element(by.id("ToolbarLabel")).getText();
        expect(sValue).toEqual("Job details");

        element(by.id("BackBtn")).click();
        sValue = element(by.id("ToolbarLabel")).getText();
        expect(sValue).toEqual("List of jobs");

        element(by.id("LogOutBtn")).click();
        sValue = element(by.id("ToolbarLabel")).getText();
        expect(sValue).toEqual("Log In");
    });
});