package com.nike.mm.business.plugins.impl

import com.nike.mm.business.plugins.IJenkinsBusiness
import com.nike.mm.dto.HttpRequestDto
import com.nike.mm.dto.JobRunResponseDto
import com.nike.mm.entity.internal.JobHistory
import com.nike.mm.entity.plugins.Jenkins
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
    String validateConfig(final Object config) {
        String errorMessage = null
        if (!config.url) {
            errorMessage = prefixWithType("Missing url")
        }
        return errorMessage
    }

    @Override
    JobRunResponseDto updateDataWithResponse(final Date defaultFromDate, final Object configInfo) {
        final int recordsCount = this.findJobs(defaultFromDate, configInfo)
        return new JobRunResponseDto(type: type(), status: JobHistory.Status.success, recordsCount: recordsCount)
    }

    int findJobs(final Date defaultFromDate, final Object configInfo){
        int recordsCount = 0
        final String path = "/api/json";
        log.debug("1.Path: $path")
        final HttpRequestDto dto = [url: configInfo.url, path: path, query:[start: 0, limit: 300], credentials: configInfo.credentials, proxyDto: this.getProxyDto(configInfo)] as HttpRequestDto
        final def json = this.jenkinsWsRepository.findListOfJobs(dto)
        if (json?.jobs) {
            json.jobs.each { def i ->
                final def newPath = this.utilitiesService.getPathFromUrl(i.url)
                log.debug("newPath: $newPath")
                recordsCount += this.findListOfJobsJobs(defaultFromDate, configInfo, newPath)
            }
        }
        return recordsCount
	}

    int findListOfJobsJobs(final Date fromDate, final Object configInfo, final String jobsJobPath) {
        int recordsCount = 0
        final String path = jobsJobPath + "api/json";
        log.debug("2.Path: $path")
        final HttpRequestDto dto = [url: configInfo.url, path: path, query:[start: 0, limit: 300], credentials: configInfo.credentials, proxyDto:this.getProxyDto(configInfo)] as HttpRequestDto
        final def json = this.jenkinsWsRepository.findListOfJobsJobs(dto)
        json?.jobs?.each { def i ->
            def newPath = this.utilitiesService.getPathFromUrl(i.url)
            recordsCount += this.findListOfJobsJobsJobs(fromDate, configInfo, newPath)
        }
        return recordsCount
    }

    int findListOfJobsJobsJobs(final Date fromDate, final Object configInfo, final String jobsJobPath) {
        int recordsCount = 0
        final String path = jobsJobPath + "api/json";
        log.debug("3.Path: $path")
        final HttpRequestDto dto = [url: configInfo.url, path: path, query:[start: 0, limit: 300], credentials: configInfo.credentials, proxyDto:this.getProxyDto(configInfo)] as HttpRequestDto
        final def json = this.jenkinsWsRepository.findListOfBuilds(dto)
        json?.jobs?.each { def i ->
            final def newPath = this.utilitiesService.getPathFromUrl(i.url)
            recordsCount += this.findBuildInformation(fromDate, configInfo, newPath)
        }
        return recordsCount
    }

    int findBuildInformation(final Date fromDate, final Object configInfo, final String finalPath) {
        int recordsCount = 0
        final String path = finalPath + "api/json";
        log.debug("4.Path: $path")
        HttpRequestDto dto = [url: configInfo.url, path: path, query:[start: 0, limit: 300], credentials: configInfo.credentials, proxyDto:this.getProxyDto(configInfo)] as HttpRequestDto

        final def json = this.jenkinsWsRepository.findBuildInformation(dto)
        if (json?.builds) {
            json.builds.each { def b ->
                final def newPath = this.utilitiesService.getPathFromUrl(b.url)
                log.debug("5.ewnewNWEPath: $newPath")
                recordsCount += findAndSaveBuildData(fromDate, configInfo, newPath)
            }
        }
        return recordsCount
    }

    int findAndSaveBuildData(final Date fromDate, final Object configInfo, final String finalPath) {
        int recordsCount = 0
        final String path = finalPath + "api/json";
        log.debug("6.Path: $path")
        final HttpRequestDto dto = [url: configInfo.url, path: path, query:[start: 0, limit: 300], credentials: configInfo.credentials, proxyDto:this.getProxyDto(configInfo)] as HttpRequestDto
        final def json = this.jenkinsWsRepository.findFinalBuildInformation(dto)
        if (json) {
            final def cleanTimestamp = this.utilitiesService.convertTimestampFromString(json.timestamp)
            if (cleanTimestamp >= fromDate) {
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
                    jenkinsData = new Jenkins(
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
                            dataType: "CI")
                }
                this.jenkinsEsRepository.save(jenkinsData)
                recordsCount++
            }
        }
        return recordsCount
    }

}
