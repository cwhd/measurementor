var Reflect = require('reflect-metadata');

var benchpress = require('benchpress');
var SeleniumWebDriverAdapter =
    require('benchpress/src/webdriver/selenium_webdriver_adapter').SeleniumWebDriverAdapter;
var runner = new benchpress.Runner([
    benchpress.bind(benchpress.WebDriverAdapter).toFactory(
        function() {
            return new SeleniumWebDriverAdapter(global.browser);
        }, []
    ),
    benchpress.bind(benchpress.Options.DEFAULT_DESCRIPTION).toValue({
        'tree': 'baseline'
    })
]);

var baseUrl = 'http://localhost:8080';

var SignInView = (function() {
    function SignInView() {
        this.userNameInput = element(by.id("UserName"));
        this.passwordInput = element(by.id("Password"));
        this.logInBtn = element(by.id("LogInBtn"));
    }
    SignInView.prototype.get = function() {
        browser.get("http://localhost:8080");
    };
    return SignInView;
})();
var GeneralLayoutView = (function() {
    function GeneralLayoutView() {
        this.toolbarLabel = element(by.id("ToolbarLabel"));
        this.logOutBtn = element(by.id("LogOutBtn"));
    }
    return GeneralLayoutView;
})();
var JobsListView = (function() {
    function JobsListView() {
        this.firstFolderBtn = element.all(by.css(".fa-folder")).get(0);
        this.firstDetailsBtn = element(by.css(".fa-eye"));
        this.firstHistoryBtn = element(by.css(".fa-list"));
        this.addJobBtn = element(by.id("AddJobBtn"));
    }
    return JobsListView;
})();
var JobDetailsView = (function() {
    function JobDetailsView() {
        this.backBtn = element(by.id("BackBtn"));
    }
    return JobDetailsView;
})();
var JobHistoryView = (function() {
    function JobHistoryView() {
        this.backBtn = element(by.id("BackBtn"));
    }
    return JobHistoryView;
})();

describe('deep tree baseline', function() {
    it('should work ', function(done) {
        //Tells protractor this isn't an Angular 1 application
        browser.ignoreSynchronization = true;
        //Load the benchmark
        //browser.get(baseUrl + '');
        return runner.sample({
            id: 'measurementor',
            //bindings: [benchpress.bind(benchpress.Options.CAPTURE_FRAMES).toValue(true)],
            execute: function() {
                browser.ignoreSynchronization = false;

                var signInView = new SignInView();
                signInView.get();

                signInView.userNameInput.sendKeys("Oleg");
                signInView.passwordInput.sendKeys("123");

                signInView.logInBtn.click();

                var generalLayoutView = new GeneralLayoutView();

                var sValue = generalLayoutView.toolbarLabel.getText();
                // expect(sValue).toEqual("List of jobs");

                var jobsListView = new JobsListView();
                //console.time('frameCapture');
                jobsListView.firstFolderBtn.click();
                //console.timeEnd('frameCapture');
                jobsListView.firstDetailsBtn.click();
                sValue = generalLayoutView.toolbarLabel.getText();
                // expect(sValue).toEqual("Job details");

                var jobDetailsView = new JobDetailsView();
                jobDetailsView.backBtn.click();
                sValue = generalLayoutView.toolbarLabel.getText();
                // expect(sValue).toEqual("List of jobs");

                jobsListView.firstFolderBtn.click();
                jobsListView.firstHistoryBtn.click();
                sValue = generalLayoutView.toolbarLabel.getText();
                // expect(sValue).toEqual("Job history");

                var jobHistoryView = new JobHistoryView();
                element(by.id("BackBtn")).click();
                sValue = generalLayoutView.toolbarLabel.getText();
                // expect(sValue).toEqual("List of jobs");

                jobsListView.addJobBtn.click();
                sValue = generalLayoutView.toolbarLabel.getText();
                //expect(sValue).toEqual("Job details");

                jobDetailsView.backBtn.click();
                sValue = element(by.id("ToolbarLabel")).getText();
                //expect(sValue).toEqual("List of jobs");

                generalLayoutView.logOutBtn.click();
                sValue = generalLayoutView.toolbarLabel.getText();
                //expect(sValue).toEqual("Log In");
            }
        }).then(done, done.fail);
    });
});