package com.nike.mm.business.plugins.impl

import com.google.common.collect.Lists
import com.nike.mm.business.plugins.IJiraBusiness
import com.nike.mm.dto.HttpRequestDto
import com.nike.mm.dto.JobRunResponseDto
import com.nike.mm.dto.ProxyDto
import com.nike.mm.entity.internal.JobHistory
import com.nike.mm.entity.plugins.Jira
import com.nike.mm.entity.plugins.JiraHistory
import com.nike.mm.repository.es.plugins.IJiraEsRepository
import com.nike.mm.repository.es.plugins.IJiraHistoryEsRepository
import com.nike.mm.repository.ws.IJiraWsRepository
import com.nike.mm.service.IUtilitiesService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

import java.util.concurrent.TimeUnit

@Slf4j
@Service
class JiraBusiness extends AbstractBusiness implements IJiraBusiness {

//    /**
//     * Error message when proxy url is missing
//     */
//    static final String MISSING_PROXY_URL = "Missing proxy url"
//
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

    @Autowired
    IJiraWsRepository jiraWsRepository

    @Autowired
    IUtilitiesService utilitiesService

    @Autowired
    IJiraHistoryEsRepository jiraHistoryEsRepository

    @Autowired
    IJiraEsRepository jiraEsRepository

    @Override
    String type() {
        return "Jira";
    }

    @Override
    String validateConfig(final Object config) {
        final List<String> validationErrors = Lists.newArrayList()
        if (!config.url) {
            validationErrors.add(MISSING_URL)
        }
        if (!config.credentials) {
            validationErrors.add(MISSING_CREDENTIALS)
        }
        if (config.proxyUrl) {
//            validationErrors.add(MISSING_PROXY_URL)
//        }
            if (!config.proxyPort) {
                validationErrors.add(MISSING_PROXY_PORT)
            } else {
//        if (config.proxyPort) {
                if (!config.proxyPort.toString().isInteger() || (0 >= config.proxyPort.toString().toInteger())) {
                    validationErrors.add(INVALID_PROXY_PORT)
                }
            }
        }
        return buildValidationErrorString(validationErrors)
    }

    @Override
    JobRunResponseDto updateDataWithResponse(final Date defaultFromDate, final Object configInfo) {
        int recordsCount = 0
        this.getProjects(configInfo).each { def final projectName ->

            Date fromDate = defaultFromDate
            final def pageable = new PageRequest(0, 1, new Sort(Sort.Direction.DESC, "created"))
            final Page<Jira> jiraPage = this.jiraEsRepository.findByJiraProject(projectName, pageable)
            if (jiraPage.content.size()) {
                fromDate = jiraPage.content[0].created
            }

            def final path = "/rest/api/2/search"
            def final jiraQuery = "project=$projectName AND updatedDate>" + fromDate.getTime() + " order by updatedDate " +
                    "asc"
            def final query = [jql: jiraQuery, expand: "changelog", startAt: 0, maxResults: 100, fields: "*all"]
            def final proxyDto = [url: configInfo.proxyUrl, port: configInfo.proxyPort] as ProxyDto
            final HttpRequestDto dto = [url: configInfo.url, path: path, query: query, credentials: configInfo.credentials,
                                  proxyDto: proxyDto] as HttpRequestDto
            recordsCount += this.updateProjectData(projectName, dto)
        }

        final def jobResponseDto = new JobRunResponseDto(type: type(), status: JobHistory.Status.success, recordsCount:
                recordsCount)
        return jobResponseDto
    }

    List<String> getProjects(final Object configInfo) {
        final def path = "/rest/api/2/project"
        final def proxyDto = [url: configInfo.proxyUrl, port: configInfo.proxyPort] as ProxyDto
        HttpRequestDto dto = [url: configInfo.url, path: path, query: [start: 0, limit: 300], credentials: configInfo
                .credentials, proxyDto: proxyDto] as HttpRequestDto
        return this.jiraWsRepository.getProjectsList(dto)
    }

    int updateProjectData(final String projectName, final HttpRequestDto dto, final Object projectConfig) {
        boolean keepGoing = false
        int updatedRecordsCount = 0

        final def json = this.jiraWsRepository.getDataForProject(dto)

        if (json && json.issues && json.issues?.size() > 0) {
            keepGoing = true
            json.issues.each { def i ->

                ChangelogHistoryItemDto changelogHistoryItemDto = new ChangelogHistoryItemDto()
                if (i.changelog) {
                    changelogHistoryItemDto = new ChangelogHistoryItemDto(i, projectConfig.taskStatusMap)
                }
                final LeadTimeDevTimeDto leadTimeDevTimeDto = new LeadTimeDevTimeDto(i, changelogHistoryItemDto
                        .movedToDevList.min())
                final OtherItemsDto otherItemsDto = new OtherItemsDto(i)
                this.saveJiraData(projectName, i, changelogHistoryItemDto, leadTimeDevTimeDto, otherItemsDto)
                updatedRecordsCount++
            }
            log.debug("Retrieved $updatedRecordsCount records for Project $projectName ")
        } else {
            log.debug("Skipping project $projectName as no updated records where found")
        }

        if (keepGoing) {
            log.debug("NEXT PAGE starting at $dto.query.startAt")
            dto.query.startAt += dto.query.maxResults
            updatedRecordsCount += this.updateProjectData(projectName, dto, projectConfig)
        }
        return updatedRecordsCount
    }

    def saveJiraData(final String projectName,
                     final def i,
                     final ChangelogHistoryItemDto changelogHistoryItemDto,
                     final LeadTimeDevTimeDto leadTimeDevTimeDto,
                     final OtherItemsDto otherItemsDto) {
        //TODO - need a way to figure out estimates based on input
        final def estimateHealth = this.utilitiesService.estimateHealth(otherItemsDto.storyPoints, leadTimeDevTimeDto
                .devTime, 13, 9, [1, 2, 3, 5, 8, 13])

        def jiraData = this.jiraEsRepository.findOne(i.key)
        if (jiraData) {
            jiraData.createdBy = this.utilitiesService.cleanEmail(i.fields.creator?.emailAddress)
            jiraData.issuetype = otherItemsDto.issueType
            jiraData.movedForward = changelogHistoryItemDto.moveForward
            jiraData.movedBackward = changelogHistoryItemDto.moveBackward
            jiraData.storyPoints = otherItemsDto.storyPoints
            jiraData.finished = this.utilitiesService.cleanJiraDate(i.fields.resolutiondate)
            jiraData.assignees = changelogHistoryItemDto.assignees
            jiraData.tags = i.fields.labels
            jiraData.dataType = "PTS"
            jiraData.leadTime = leadTimeDevTimeDto.leadTime
            jiraData.devTime = leadTimeDevTimeDto.devTime
            jiraData.commentCount = i.fields.comment?.total
            jiraData.jiraProject = projectName
            jiraData.rawEstimateHealth = estimateHealth.raw
            jiraData.estimateHealth = estimateHealth.result
            jiraData.components = otherItemsDto.components
            jiraData.product = otherItemsDto.product
        } else {
            jiraData = new Jira(
                    key              : i.key,
                    created          : this.utilitiesService.cleanJiraDate(i.fields.created),
                    createdBy        : this.utilitiesService.cleanEmail(i.fields.creator?.emailAddress),
                    issuetype        : otherItemsDto.issueType,
                    movedForward     : changelogHistoryItemDto.moveForward,
                    movedBackward    : changelogHistoryItemDto.moveBackward,
                    storyPoints      : otherItemsDto.storyPoints,
                    finished         : this.utilitiesService.cleanJiraDate(i.fields.resolutiondate),
                    assignees        : changelogHistoryItemDto.assignees,
                    tags             : i.fields.labels,
                    dataType         : "PTS",
                    leadTime         : leadTimeDevTimeDto.leadTime,
                    devTime          : leadTimeDevTimeDto.devTime,
                    commentCount     : i.fields.comment?.total,
                    jiraProject      : projectName,
                    estimateHealth   : estimateHealth.result,
                    rawEstimateHealth: estimateHealth.raw,
                    components       : otherItemsDto.components,
                    product          : otherItemsDto.product)
        }
        this.jiraEsRepository.save(jiraData)
    }

    class OtherItemsDto {
        String issueType = ""
        List components = []
        String product = ""
        Integer storyPoints = 0

        OtherItemsDto(final def i) {
            this.issueType = this.getIssueType(i)
            this.components = this.getComponentsList(i)
            this.product = this.getProductString(i)
            this.storyPoints = this.getStoryPoints(i)
        }

        String getIssueType(final def i) {
            def issueType = ""
            if (i.fields.issuetype?.name) {
                issueType = i.fields.issuetype.name.replace(" ", "_")
            }
            return issueType
        }

        List getComponentsList(final def i) {
            final List components = []
            for (def c : i.fields.components) {
                components.add(c.name)
            }
            return components
        }

        String getProductString(final def i) {
            String product = ""
            if (i.fields.customfield_12040) {
                product = i.fields.customfield_12040.value
                if (i.fields.customfield_12040.child) {
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
        def moveForward = 0
        def moveBackward = 0
        def assignees = []
        def movedToDevList = []

        /**
         * Default constructor in the case that we have no change log information
         */
        ChangelogHistoryItemDto() { }

        /**
         * In the event that we have changelog infomation.
         * @param i - The json array from the result list.
         */
        ChangelogHistoryItemDto(final def i, final def taskStatusMap) {

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
                    if (history == null) {
                        history = new JiraHistory(
                                dataType   : "PTS",
                                sourceId   : h.id,
                                timestamp  : JiraBusiness.this.utilitiesService.cleanJiraDate(h.created),
                                changeField: t.field,
                                newValue   : t.toString,
                                changedBy  : h.author.emailAddress,
                                key        : i.key)
                        JiraBusiness.this.jiraHistoryEsRepository.save(history)
                    }
                }
            }
        }
    }

    class LeadTimeDevTimeDto {
        def leadTime = 0
        def devTime = 0

        LeadTimeDevTimeDto(final def i, final def movedToDev) {
            log.debug("Fields: " + i.fields.created)
            final def createdDate = JiraBusiness.this.utilitiesService.cleanJiraDate(i.fields.created)
            final def fin = JiraBusiness.this.utilitiesService.cleanJiraDate(i.fields.resolutiondate)
            if (createdDate) {
                def endLeadTime = new Date()
                if (fin) {
                    endLeadTime = fin
                }
                final long duration = endLeadTime.getTime() - createdDate.getTime()
                leadTime = TimeUnit.MILLISECONDS.toDays(duration)
                if (leadTime == 0) {
                    leadTime = 1
                }
            }

            if (movedToDev) {
                def endLeadTime = new Date()
                if (fin) {
                    endLeadTime = fin
                }
                final long duration = endLeadTime.getTime() - movedToDev.getTime()
                devTime = TimeUnit.MILLISECONDS.toDays(duration)
                if (devTime == 0) {
                    devTime = 1
                }
            }
        }
    }
}
