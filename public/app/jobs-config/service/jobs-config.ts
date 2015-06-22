declare
var angular: any;

angular.module("jobsConfig").factory("jobsConfig", function($http, $q, constants) {

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
            }).
            error(function(data, status, headers, config) {
                if (onError) {
                    onError();
                }
            });
        },
        changeJobStatus: function(jobData, onSuccess, onError) {
            var req = {
                method: "POST",
                url: "api/jobs-config",
                data: jobData
            };
            $http(req).
            success(function(data, status, headers, config) {
                if (onSuccess) {
                    onSuccess();
                }
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
                    data.data.configAsString = JSON.stringify(data.data.config);
                    return deferred.resolve(data.data);
                }
            });

            return deferred.promise;
        },
        saveJobConfig: function(jobConfig, onSuccess, onError) {
            jobConfig.config = JSON.parse(jobConfig.configAsString);
            delete jobConfig.configAsString;

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
                if (onError) {
                    onError();
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