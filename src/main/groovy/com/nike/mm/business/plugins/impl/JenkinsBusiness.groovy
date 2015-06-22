package com.nike.mm.business.plugins.impl

import com.nike.mm.business.plugins.IJenkinsBusiness
import com.nike.mm.dto.HttpRequestDto
import com.nike.mm.dto.ProxyDto
import com.nike.mm.repository.ws.IJenkinsWsRepository
import com.nike.mm.service.IUtilitiesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class JenkinsBusiness implements IJenkinsBusiness {

	@Autowired IJenkinsWsRepository jenkinsWsRepository

	@Autowired IUtilitiesService utilitiesService

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

    void findJobs(final Object configInfo){
		Date fromDate = Date.parse( 'dd-MM-yyyy', "01-01-2001" )
		String path = "/api/json";
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
        HttpRequestDto dto = [url: configInfo.url, path: path, query:[start: 0, limit: 300], credentials: configInfo.credentials, proxyDto:[]as ProxyDto] as HttpRequestDto
        def json = this.jenkinsWsRepository.findListOfJobsJobs(dto)
        json.jobs.each { def i ->
            def newPath = this.utilitiesService.getPathFromUrl(i.url)
            println "newnewPath: $newPath"
            this.findListOfJobsJobsJobs(configInfo, newPath)
        }
    }
    void findListOfJobsJobsJobs(final Object configInfo, final String jobsJobPath) {

        String path = jobsJobPath + "api/json";
        println "J3PATH: $path"
        HttpRequestDto dto = [url: configInfo.url, path: path, query:[start: 0, limit: 300], credentials: configInfo.credentials, proxyDto:[]as ProxyDto] as HttpRequestDto
        def json = this.jenkinsWsRepository.findListOfJobsJobsJobs(dto)

        json.jobs.each { def i ->
            def newPath = this.utilitiesService.getPathFromUrl(i.url)
            println "newnewNWEPath: $newPath"
//            this.findListOfJobsJobs(configInfo, newPath)
        }
    }

    void findAndStoreBuildInformation(final Object configInfo, String finalPath) {}

}
