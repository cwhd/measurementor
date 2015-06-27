package com.nike.mm.business.plugins.impl

import com.nike.mm.business.plugins.IJenkinsBusiness
import com.nike.mm.dto.HttpRequestDto
import com.nike.mm.dto.JobRunResponseDto
import com.nike.mm.dto.ProxyDto
import com.nike.mm.entity.Jenkins
import com.nike.mm.entity.JobHistory
import com.nike.mm.repository.es.plugins.IJenkinsEsRepository
import com.nike.mm.repository.ws.IJenkinsWsRepository
import com.nike.mm.service.IUtilitiesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class JenkinsBusiness implements IJenkinsBusiness {

	@Autowired IJenkinsWsRepository jenkinsWsRepository

	@Autowired IUtilitiesService utilitiesService

    @Autowired IJenkinsEsRepository jenkinsEsRepository

	@Override
	String type() {
		return "Jenkins";
	}
	
	@Override
	boolean validateConfig(Object config) {
		return config.url ? true:false;
	}
	
	@Override
	void updateData(final Object configInfo) {
        //TODO get last from date.
        this.findJobs(configInfo)
    }

    @Override
    JobRunResponseDto updateDataWithResponse(Date lastRunDate, Object configInfo) {
        return [type: type(), status: JobHistory.Status.success, reccordsCount: 0] as JobRunResponseDto
    }

    void findJobs(final Object configInfo){
		Date fromDate = Date.parse( 'dd-MM-yyyy', "01-01-2001" )
		String path = "/api/json";
        println "1.Path: $path"
		HttpRequestDto dto = [url: configInfo.url, path: path, query:[start: 0, limit: 300], credentials: configInfo.credentials, proxyDto:[]as ProxyDto] as HttpRequestDto
        def json = this.jenkinsWsRepository.findListOfJobs(dto)
        json.jobs.each { def i ->
			def newPath = this.utilitiesService.getPathFromUrl(i.url)
			println "newPath: $newPath"
            this.findListOfJobsJobs(configInfo, newPath)
		}
	}

    void findListOfJobsJobs(final Object configInfo, final String jobsJobPath) {

        String path = jobsJobPath + "api/json";
        println "2.Path: $path"
        HttpRequestDto dto = [url: configInfo.url, path: path, query:[start: 0, limit: 300], credentials: configInfo.credentials, proxyDto:[]as ProxyDto] as HttpRequestDto
        def json = this.jenkinsWsRepository.findListOfJobsJobs(dto)
        json.jobs.each { def i ->
            def newPath = this.utilitiesService.getPathFromUrl(i.url)
            this.findListOfJobsJobsJobs(configInfo, newPath)
        }
    }
    void findListOfJobsJobsJobs(final Object configInfo, final String jobsJobPath) {

        String path = jobsJobPath + "api/json";
        println "3.Path: $path"
        HttpRequestDto dto = [url: configInfo.url, path: path, query:[start: 0, limit: 300], credentials: configInfo.credentials, proxyDto:[]as ProxyDto] as HttpRequestDto
        def json = this.jenkinsWsRepository.findListOfBuilds(dto)

        json.jobs.each { def i ->
            def newPath = this.utilitiesService.getPathFromUrl(i.url)

            this.findBuildInformation(configInfo, newPath)
        }
    }

    void findBuildInformation(final Object configInfo, final String finalPath) {
        String path = finalPath + "api/json";
        println "4.Path: $path"
        HttpRequestDto dto = [url: configInfo.url, path: path, query:[start: 0, limit: 300], credentials: configInfo.credentials, proxyDto:[]as ProxyDto] as HttpRequestDto

        def json = this.jenkinsWsRepository.findBuildInformation(dto)
        json.builds.each { def b ->
            def newPath = this.utilitiesService.getPathFromUrl(b.url)
            println "5.ewnewNWEPath: $newPath"
            findAndSaveBuildData(configInfo, newPath)
        }
    }

    void findAndSaveBuildData(final Object configInfo, final String finalPath) {
        String path = finalPath + "api/json";
        println "6.Path: $path"
        HttpRequestDto dto = [url: configInfo.url, path: path, query:[start: 0, limit: 300], credentials: configInfo.credentials, proxyDto:[]as ProxyDto] as HttpRequestDto
        def json = this.jenkinsWsRepository.findFinalBuildInformation(dto)
        if (json) {
            def cleanTimestamp = this.utilitiesService.convertTimestampFromString(json.timestamp)
            def cleanDisplayName = ""
            if (json.fullDisplayName) {
                cleanDisplayName = this.utilitiesService.cleanFullBuildName(json.fullDisplayName)
            }
            def causedBy = ""
            def remoteUrl = ""
            def lastBuiltRevision = ""
            for (def a in json.actions) {
                if (a.causes) {
                    for (def c in a.causes) {
                        causedBy = c.userId
                    }
                }
                if (a.remoteUrls) {
                    for (def r in a.remoteUrls) { //TODO i should make sure to comma separate these...
                        remoteUrl += r
                    }
                }
                if (a.lastBuildRevision) {    //TODO not sure why this doesn't work yet...
                    lastBuiltRevision = a.lastBuiltRevision.SHA1
                }
            }

            def jenkinsData = this.jenkinsEsRepository.findOne(json.id)

            if (jenkinsData) {
                jenkinsData.timestamp = cleanTimestamp
                jenkinsData.jenkinsUrl = json.url
                jenkinsData.buildName = cleanDisplayName
                jenkinsData.result = json.result
                jenkinsData.duration = json.duration
                jenkinsData.causedBy = causedBy
                jenkinsData.remoteUrl = remoteUrl
                jenkinsData.lastBuiltRevision = lastBuiltRevision
                jenkinsData.dataType = "CI"
                jenkinsData.buildNumber = json.number
            } else {
                jenkinsData = [
                        buildId: json.id,
                        timestamp: cleanTimestamp,
                        jenkinsUrl: json.url,
                        buildName: cleanDisplayName,
                        result: json.result,
                        buildNumber: json.number,
                        duration: json.duration,
                        causedBy: causedBy,
                        remoteUrl: remoteUrl,
                        lastBuiltRevision: lastBuiltRevision,
                        dataType: "CI"] as Jenkins
            }
            this.jenkinsEsRepository.save(jenkinsData)
        }
    }
}
