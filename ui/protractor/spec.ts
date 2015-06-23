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

class SignInView {
    userNameInput: any;
    passwordInput: any;
    logInBtn: any;
    constructor() {
        this.userNameInput = element(by.id("UserName"));
        this.passwordInput = element(by.id("Password"));
        this.logInBtn = element(by.id("LogInBtn"));
    }
    get() {
        browser.get("http://localhost:8080");
    }
}

class GeneralLayoutView {
    toolbarLabel: any;
    logOutBtn: any;
    constructor() {
        this.toolbarLabel = element(by.id("ToolbarLabel"));
        this.logOutBtn = element(by.id("LogOutBtn"));
    }
}

class JobsListView {
    firstFolderBtn: any;
    firstDetailsBtn: any;
    firstHistoryBtn: any;
    addJobBtn: any;
    constructor() {
        this.firstFolderBtn = element.all(by.css(".fa-folder")).get(0);
        this.firstDetailsBtn = element(by.css(".fa-eye"));
        this.firstHistoryBtn = element(by.css(".fa-list"));
        this.addJobBtn = element(by.id("AddJobBtn"));
    }
}

class JobDetailsView {
    backBtn: any;
    constructor() {
        this.backBtn = element(by.id("BackBtn"));
    }
}

class JobHistoryView {
    backBtn: any;
    constructor() {
        this.backBtn = element(by.id("BackBtn"));
    }
}

describe("Navigation test", function() {
    it("Navigation test", function() {
        var signInView = new SignInView();
        signInView.get();

        signInView.userNameInput.sendKeys("Oleg");
        signInView.passwordInput.sendKeys("123");
        signInView.logInBtn.click();

        var generalLayoutView = new GeneralLayoutView();

        var sValue = generalLayoutView.toolbarLabel.getText();
        expect(sValue).toEqual("List of jobs");

        var jobsListView = new JobsListView();

        jobsListView.firstFolderBtn.click();
        jobsListView.firstDetailsBtn.click();
        sValue = generalLayoutView.toolbarLabel.getText();
        expect(sValue).toEqual("Job details");

        var jobDetailsView = new JobDetailsView();
        jobDetailsView.backBtn.click();
        sValue = generalLayoutView.toolbarLabel.getText();
        expect(sValue).toEqual("List of jobs");

        jobsListView.firstFolderBtn.click();
        jobsListView.firstHistoryBtn.click();
        sValue = generalLayoutView.toolbarLabel.getText();
        expect(sValue).toEqual("Job history");

        var jobHistoryView = new JobHistoryView();
        element(by.id("BackBtn")).click();
        sValue = generalLayoutView.toolbarLabel.getText();
        expect(sValue).toEqual("List of jobs");

        jobsListView.addJobBtn.click();
        sValue = generalLayoutView.toolbarLabel.getText();
        expect(sValue).toEqual("Job details");

        jobDetailsView.backBtn.click();
        sValue = element(by.id("ToolbarLabel")).getText();
        expect(sValue).toEqual("List of jobs");

        generalLayoutView.logOutBtn.click();
        sValue = generalLayoutView.toolbarLabel.getText();
        expect(sValue).toEqual("Log In");
    });
});