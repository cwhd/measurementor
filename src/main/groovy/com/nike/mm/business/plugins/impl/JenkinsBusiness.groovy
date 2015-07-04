package com.nike.mm.business.plugins.impl

import com.nike.mm.business.plugins.IJenkinsBusiness
import com.nike.mm.dto.HttpRequestDto
import com.nike.mm.dto.JobRunResponseDto
import com.nike.mm.entity.Jenkins
import com.nike.mm.entity.JobHistory
import com.nike.mm.repository.es.plugins.IJenkinsEsRepository
import com.nike.mm.repository.ws.IJenkinsWsRepository
import com.nike.mm.service.IUtilitiesService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@Service
class JenkinsBusiness extends AbstractBusiness implements IJenkinsBusiness {

	@Autowired IJenkinsWsRepository jenkinsWsRepository

	@Autowired IUtilitiesService utilitiesService

    @Autowired IJenkinsEsRepository jenkinsEsRepository

	@Override
	String type() {
		return "Jenkins";
	}

    @Override
    String validateConfig(Object config) {
        String errorMessage = null
        if (!config.url) {
            errorMessage = prefixWithType("Missing url")
        }
        return errorMessage
    }

    @Override
    JobRunResponseDto updateDataWithResponse(Date lastRunDate, Object configInfo) {
        int recordsCount = this.findJobs(lastRunDate, configInfo)
        return new JobRunResponseDto(type: type(), status: JobHistory.Status.success, recordsCount: recordsCount)
    }

    int findJobs(final Date lastRunDate, final Object configInfo){
        int recordsCount = 0
		String path = "/api/json";
        log.debug("1.Path: $path")
		HttpRequestDto dto = [url: configInfo.url, path: path, query:[start: 0, limit: 300], credentials: configInfo.credentials, proxyDto: this.getProxyDto(configInfo)] as HttpRequestDto
        def json = this.jenkinsWsRepository.findListOfJobs(dto)
        if (json && json.jobs) {
            json.jobs.each { def i ->
                def newPath = this.utilitiesService.getPathFromUrl(i.url)
                log.debug("newPath: $newPath")
                recordsCount += this.findListOfJobsJobs(configInfo, newPath)
            }
        }
        return recordsCount
	}

    int findListOfJobsJobs(final Object configInfo, final String jobsJobPath) {
        int recordsCount = 0
        String path = jobsJobPath + "api/json";
        log.debug("2.Path: $path")
        HttpRequestDto dto = [url: configInfo.url, path: path, query:[start: 0, limit: 300], credentials: configInfo.credentials, proxyDto:this.getProxyDto(configInfo)] as HttpRequestDto
        def json = this.jenkinsWsRepository.findListOfJobsJobs(dto)
        json.jobs.each { def i ->
            def newPath = this.utilitiesService.getPathFromUrl(i.url)
            recordsCount += this.findListOfJobsJobsJobs(configInfo, newPath)
        }
        return recordsCount
    }

    int findListOfJobsJobsJobs(final Object configInfo, final String jobsJobPath) {
        int recordsCount = 0
        String path = jobsJobPath + "api/json";
        log.debug("3.Path: $path")
        HttpRequestDto dto = [url: configInfo.url, path: path, query:[start: 0, limit: 300], credentials: configInfo.credentials, proxyDto:this.getProxyDto(configInfo)] as HttpRequestDto
        def json = this.jenkinsWsRepository.findListOfBuilds(dto)

        json.jobs.each { def i ->
            def newPath = this.utilitiesService.getPathFromUrl(i.url)

            recordsCount += this.findBuildInformation(configInfo, newPath)
        }
        return recordsCount
    }

    int findBuildInformation(final Object configInfo, final String finalPath) {
        int recordsCount = 0
        String path = finalPath + "api/json";
        log.debug("4.Path: $path")
        HttpRequestDto dto = [url: configInfo.url, path: path, query:[start: 0, limit: 300], credentials: configInfo.credentials, proxyDto:this.getProxyDto(configInfo)] as HttpRequestDto

        def json = this.jenkinsWsRepository.findBuildInformation(dto)
        json.builds.each { def b ->
            def newPath = this.utilitiesService.getPathFromUrl(b.url)
            log.debug("5.ewnewNWEPath: $newPath")
            recordsCount += findAndSaveBuildData(configInfo, newPath)
        }
        return recordsCount
    }

    int findAndSaveBuildData(final Object configInfo, final String finalPath) {
        int recordsCount = 0
        String path = finalPath + "api/json";
        log.debug("6.Path: $path")
        HttpRequestDto dto = [url: configInfo.url, path: path, query:[start: 0, limit: 300], credentials: configInfo.credentials, proxyDto:this.getProxyDto(configInfo)] as HttpRequestDto
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
            recordsCount ++
        }
        return recordsCount
    }

}
