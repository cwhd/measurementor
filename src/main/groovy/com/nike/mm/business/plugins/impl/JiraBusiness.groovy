package com.nike.mm.business.plugins.impl

import com.google.common.collect.Lists
import com.nike.mm.dto.HttpRequestDto
import com.nike.mm.dto.JobRunResponseDto
import com.nike.mm.dto.ProxyDto
import com.nike.mm.entity.Jira
import com.nike.mm.entity.JiraHistory
import com.nike.mm.entity.JobHistory
import com.nike.mm.repository.es.plugins.IJiraEsRepository
import com.nike.mm.repository.es.plugins.IJiraHistoryEsRepository
import com.nike.mm.repository.ws.IJiraWsRepository
import com.nike.mm.service.IUtilitiesService
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import com.nike.mm.business.plugins.IJiraBusiness

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
class JiraBusiness extends AbstractBusiness implements IJiraBusiness {


    /**
     * Error message when proxy url is missing
     */
    static final String MISSING_PROXY_URL = "Missing proxy url"

    /**
     * Error message when proxy port is missing
     */
    static final String MISSING_PROXY_PORT = "Missing proxy port"

    /**
     * Error message when proxy port is not a positive integer
     */
    static final String INVALID_PROXY_PORT = "Proxy must be a positive integer"

    /**
     * Error message when credentials are missing
     */
    static final String MISSING_CREDENTIALS = "Missing credentials"

    @Autowired IJiraWsRepository jiraWsRepository

    @Autowired IUtilitiesService utilitiesService

    @Autowired IJiraHistoryEsRepository jiraHistoryEsRepository

    @Autowired IJiraEsRepository jiraEsRepository

	@Override
	String type() {
		return "Jira";
	}

    @Override
    String validateConfig(Object config) {
        List<String> validationErrors = Lists.newArrayList()
        if (!config.url) {
            validationErrors.add(MISSING_URL)
        }
        if (!config.credentials) {
            validationErrors.add(MISSING_CREDENTIALS)
        }
        if (!config.proxyUrl) {
            validationErrors.add(MISSING_PROXY_URL)
        }
        if (!config.proxyPort) {
            validationErrors.add(MISSING_PROXY_PORT)
        } else {
            if (!config.proxyPort.toString().isInteger() || (0 >= config.proxyPort.toString().toInteger())) {
                validationErrors.add(INVALID_PROXY_PORT)
            }
        }
        return buildValidationErrorString(validationErrors)
    }

    @Override
    JobRunResponseDto updateDataWithResponse(Date lastRunDate, Object configInfo) {
        int reccordsCount = 0
        this.getProjects(configInfo).each { def projectName ->
            def path = "/rest/api/2/search"

            def jiraQuery      = "project=$projectName AND updateDate>" + lastRunDate.getTime()
            log.error("Query: " + jiraQuery)
            def query          = [jql: jiraQuery, expand: "changelog", startAt: 0, maxResults: 100, fields: "*all"]
            def proxyDto       = [url: configInfo.proxyUrl, port: configInfo.proxyPort] as ProxyDto
            HttpRequestDto dto = [url: configInfo.url, path: path, query: query, credentials: configInfo.credentials, proxyDto: proxyDto] as HttpRequestDto

            reccordsCount = this.updateProjectData(projectName, dto)
        }
        def jobResponseDto = new JobRunResponseDto(type: type(), status: JobHistory.Status.success, recordsCount: reccordsCount)
        return jobResponseDto
    }

	List<String> getProjects(final Object configInfo) {
		def path            = "/rest/api/2/project"
        def proxyDto        = [url: configInfo.proxyUrl, port: configInfo.proxyPort] as ProxyDto
		HttpRequestDto dto  = [url: configInfo.url, path: path, query:[start: 0, limit: 300], credentials: configInfo.credentials, proxyDto: proxyDto] as HttpRequestDto
		return this.jiraWsRepository.getProjectsList(dto)
	}

    int updateProjectData(final String projectName, final HttpRequestDto dto) {
        boolean keepGoing = false
        int updatedReccordsCount = 0

        def json = this.jiraWsRepository.getDataForProject(dto)

        if (json && json.issues?.size() > 0 ) {
            keepGoing = true
            def movedToDev
            json.issues.each{ def i ->

                ChangelogHistoryItemDto changelogHistoryItemDto = new ChangelogHistoryItemDto()
                if (i.changelog) {
                    changelogHistoryItemDto  = new ChangelogHistoryItemDto(i)
                }
                LeadTimeDevTimeDto leadTimeDevTimeDto               = new LeadTimeDevTimeDto(i, changelogHistoryItemDto.movedToDevList.min())
                OtherItemsDto otherItemsDto                         = new OtherItemsDto(i)
                this.saveJiraData(projectName, i, changelogHistoryItemDto, leadTimeDevTimeDto, otherItemsDto)
                updatedReccordsCount ++
            }
        }
        if(keepGoing) {
            JiraBusiness.log.debug("NEXT PAGE starting at $dto.query.startAt")
            dto.query.startAt += dto.query.maxResults
            updatedReccordsCount += this.updateProjectData(projectName, dto)
        }
        return updatedReccordsCount
    }

    def saveJiraData(final String projectName,
                     final def i,
                     final ChangelogHistoryItemDto changelogHistoryItemDto,
                     final LeadTimeDevTimeDto leadTimeDevTimeDto,
                     final OtherItemsDto otherItemsDto) {
        //TODO - need a way to figure out estimates based on input
        def estimateHealth = this.utilitiesService.estimateHealth(otherItemsDto.storyPoints, leadTimeDevTimeDto.devTime, 13, 9, [1, 2, 3, 5, 8, 13])

        def jiraData = this.jiraEsRepository.findOne(i.key)
        if (jiraData) {
            jiraData.createdBy          = this.utilitiesService.cleanEmail(i.fields.creator?.emailAddress)
            jiraData.issuetype          = otherItemsDto.issueType
            jiraData.movedForward       = changelogHistoryItemDto.moveForward
            jiraData.movedBackward      = changelogHistoryItemDto.moveBackward
            jiraData.storyPoints        = otherItemsDto.storyPoints
            jiraData.finished           = this.utilitiesService.cleanJiraDate(i.fields.resolutiondate)
            jiraData.assignees          = changelogHistoryItemDto.assignees
            jiraData.tags               = i.fields.labels
            jiraData.dataType           = "PTS"
            jiraData.leadTime           = leadTimeDevTimeDto.leadTime
            jiraData.devTime            = leadTimeDevTimeDto.devTime
            jiraData.commentCount       = i.fields.comment?.total
            jiraData.jiraProject        = projectName
            jiraData.rawEstimateHealth  = estimateHealth.raw
            jiraData.estimateHealth     = estimateHealth.result
            jiraData.components         = otherItemsDto.components
            jiraData.product            = otherItemsDto.product
        } else {
            jiraData = [
                    key: i.key,
                    created:            this.utilitiesService.cleanJiraDate(i.fields.created),
                    createdBy:          this.utilitiesService.cleanEmail(i.fields.creator?.emailAddress),
                    issuetype:          otherItemsDto.issueType,
                    movedForward:       changelogHistoryItemDto.moveForward,
                    movedBackward:      changelogHistoryItemDto.moveBackward,
                    storyPoints:        otherItemsDto.storyPoints,
                    finished:           this.utilitiesService.cleanJiraDate(i.fields.resolutiondate),
                    assignees:          changelogHistoryItemDto.assignees,
                    tags:               i.fields.labels,
                    dataType:           "PTS",
                    leadTime:           leadTimeDevTimeDto.leadTime,
                    devTime:            leadTimeDevTimeDto.devTime,
                    commentCount:       i.fields.comment?.total,
                    jiraProject:        projectName,
                    estimateHealth:     estimateHealth.result,
                    rawEstimateHealth:  estimateHealth.raw,
                    components:         otherItemsDto.components,
                    product:            otherItemsDto.product] as Jira
        }
        this.jiraEsRepository.save(jiraData)
    }

    class OtherItemsDto {
        String issueType    = ""
        List components  = []
        String product      = ""
        Integer storyPoints = 0

        OtherItemsDto(final def i) {
            this.issueType      = this.getIssueType(i)
            this.components     = this.getComponentsList(i)
            this.product        = this.getProductString(i)
            this.storyPoints    = this.getStoryPoints(i)
        }

        String getIssueType(final def i) {
            def issueType = ""
            if (i.fields.issuetype?.name) {
                issueType = i.fields.issuetype.name.replace(" ", "_")
            }
            return issueType
        }

        List getComponentsList(final def i) {
            List components = []
            for(def c : i.fields.components) {
                components.add(c.name)
            }
            return components
        }

        String getProductString(final def i) {
            String product = ""
            if(i.fields.customfield_12040) {
                product = i.fields.customfield_12040.value
                if(i.fields.customfield_12040.child) {
                    product += " " + i.fields.customfield_12040.child.value
                }
            }
            return product
        }

        Integer getStoryPoints(final def i) {
            def storyPoints = 0
            if (i.fields.customfield_10013) {
                storyPoints = i.fields.customfield_10013.toInteger()
            }
            return storyPoints;
        }
    }

    class ChangelogHistoryItemDto {
        def moveForward     = 0
        def moveBackward    = 0
        def assignees       = []
        def movedToDevList  = []

        //NOTE we need to set the map so we know what direction things are moving in; this relates to the moveForward & moveBackward stuff
        //TODO this needs to be a parameter that gets passed in based on the project
        def taskStatusMap = ["In Definition": 1, "Dev Ready":2, "Dev":3, "QA Ready":4, "QA":5, "Deploy Ready":6, "Done":7]

        /**
         * Default constructor in the case that we have no change log information
         */
        ChangelogHistoryItemDto() {}

        /**
         * In the event that we have changelog infomation.
         * @param i - The json array from the result list.
         */
        ChangelogHistoryItemDto(def i) {

            for (def h : i.changelog.histories) {
                for (def t : h.items) {
                    //NOTE the following conditionals flatten history into stuff we can work with easier
                    if (t.field == "status") { //NOTE get the progression for churn
                        if (taskStatusMap[t.fromString] > taskStatusMap[t.toString]) {
                            this.moveBackward++
                        } else {
                            this.moveForward++
                            this.movedToDevList.add(JiraBusiness.this.utilitiesService.cleanJiraDate(h.created))
                        }
                    } else if (t.field == "assignee") {
                        //NOTE get everyone that worked on this issue, or at least was assigned to it
                        if (t.toString) {
                            this.assignees.add(JiraBusiness.this.utilitiesService.makeNonTokenFriendly(t.toString))
                        }
                    }
                    def history = JiraBusiness.this.jiraHistoryEsRepository.findOne(h.id)

                    //not sure we care about updates
                    if(history == null) {
                        history = [
                                dataType: "PTS",
                                sourceId: h.id,
                                timestamp: h.created,
                                changeField: t.field,
                                newValue: t.toString,
                                changedBy: h.author.emailAddress,
                                key: i.key
                        ] as JiraHistory
                        JiraBusiness.this.jiraHistoryEsRepository.save(history)
                    }
                }
            }
        }
    }

    class LeadTimeDevTimeDto {
        def leadTime = 0
        def devTime = 0

        LeadTimeDevTimeDto(def i, def movedToDev) {
            println "FIELDS: " + i.fields.created
            def createdDate = JiraBusiness.this.utilitiesService.cleanJiraDate(i.fields.created)
            def fin = JiraBusiness.this.utilitiesService.cleanJiraDate(i.fields.resolutiondate)
            if (createdDate) {
                def endLeadTime = new Date()
                if(fin) {
                    endLeadTime = fin
                }
                long duration = endLeadTime.getTime() - createdDate.getTime()
                leadTime = TimeUnit.MILLISECONDS.toDays(duration)
                if(leadTime == 0) {
                    leadTime = 1
                }
            }

            if (movedToDev) {
                def endLeadTime = new Date()
                if(fin) {
                    endLeadTime = fin
                }
                long duration = endLeadTime.getTime() - movedToDev.getTime()
                devTime = TimeUnit.MILLISECONDS.toDays(duration)
                if(devTime == 0) {
                    devTime = 1
                }
            }
        }
    }
}
