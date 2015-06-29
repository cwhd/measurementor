declare
var angular: any;

angular.module("jobsConfig").factory("jobsConfig", function($http, $q, constants, generalLayout) {

    var jobsConfig = {
        getJobs: function(url) {
            var deferred = $q.defer();
            $http.get(url).then(function(resp) {
                var jobs = resp.data._embedded.measureMentorJobsConfigDtoes;
                var links = resp.data._links;
                var page = resp.data.page;

                var result = {
                    jobs: jobs,
                    links: links,
                    page: page
                };
                deferred.resolve(result);
            });
            return deferred.promise;
        },
        runJob: function(jobId, onSuccess, onError) {
            $http.post("api/run-job/" + jobId).
            success(function(data, status, headers, config) {
                if (onSuccess) {
                    onSuccess();
                }
                generalLayout.displayToast({
                    messageText: "Job has been successfully started.",
                    messageType: "success"
                });
            }).
            error(function(data, status, headers, config) {
                if (onError) {
                    onError();
                }
            });
        },
        getJobConfig: function(jobId) {
            var deferred = $q.defer();
            $http.get("api/jobs-config/" + jobId).then(function(data) {
                if (data) {
                    data.data.configAsString = JSON.stringify(data.data.config, null, 4);
                    return deferred.resolve(data.data);
                }
            });

            return deferred.promise;
        },
        saveJobConfig: function(jobConfig, onSuccess, onError) {
            var req = {
                method: "POST",
                url: "api/jobs-config",
                data: jobConfig
            };
            $http(req).
            success(function(data, status, headers, config) {
                if (onSuccess) {
                    onSuccess();
                }

            }).
            error(function(data, status, headers, config) {
                var errorMessages = [];
                var errorMessage = "";
                var fieldName = "";

                if (data && data.message) {
                    while (data.message.indexOf("default message") >= 0) {
                        data.message = data.message.substring(data.message.indexOf("default message") + 17);
                        fieldName = data.message.substring(0, data.message.indexOf("]"));
                        data.message = data.message.substring(data.message.indexOf("default message") + 17);
                        errorMessage = data.message.substring(0, data.message.indexOf("]"));
                        errorMessages.push({ field: fieldName, message: errorMessage});
                    }
                }

                if (onError) {
                    onError(errorMessages);
                }
            });
        },
        getJobHistoryData: function(url) {
            var deferred = $q.defer();

            $http.get(url).then(function(resp) {
                var result;
                if (resp.data && resp.data._embedded) {
                    var jobHistories = resp.data._embedded.jobHistoryDtoes;
                    var links = resp.data._links;
                    var page = resp.data.page;

                    result = {
                        jobHistories: jobHistories,
                        links: links,
                        page: page
                    };
                }

                deferred.resolve(result);
            });
            return deferred.promise;
        }
    };
    return jobsConfig;
});