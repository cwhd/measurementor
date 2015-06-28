package com.nike.mm.business.plugins.impl

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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import com.nike.mm.business.plugins.IJiraBusiness

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
class JiraBusiness implements IJiraBusiness {

	@Autowired IJiraWsRepository jiraWsRepository

    @Autowired IUtilitiesService utilitiesService

    @Autowired IJiraHistoryEsRepository jiraHistoryEsRepository

    @Autowired IJiraEsRepository jiraEsRepository

	@Override
	String type() {
		return "Jira";
	}

	@Override
	boolean validateConfig(Object config) {
		return config.url ? true:false;
	}

	@Override
	void updateData(final Object configInfo) {
		this.getProjects(configInfo).each { def projectName ->
            def path            = "/rest/api/2/search"

            //TODO: UpdateDate to the jql
            def jiraQuery       = "project=$projectName"
            def query           = [jql: jiraQuery, expand:"changelog",startAt: 0, maxResults: 100, fields:"*all"]
            HttpRequestDto dto  = [url: configInfo.url, path: path, query:query, credentials: configInfo.credentials, proxyDto:[]as ProxyDto] as HttpRequestDto
            this.updateProjectData(projectName, dto, configInfo.projectConfigs[projectName])
        }
	}

    @Override
    JobRunResponseDto updateDataWithResponse(Date lastRunDate, Object configInfo) {
        updateData(configInfo)
        return [type: type(), status: JobHistory.Status.success, reccordsCount: 0] as JobRunResponseDto
    }

	List<String> getProjects(final Object configInfo) {
		def path            = "/rest/api/2/project"
		HttpRequestDto dto  = [url: configInfo.url, path: path, query:[start: 0, limit: 300], credentials: configInfo.credentials, proxyDto:[]as ProxyDto] as HttpRequestDto
		return this.jiraWsRepository.getProjectsList(dto)
	}

    def updateProjectData(final String projectName, final HttpRequestDto dto, final Object projectConfig) {
        boolean keepGoing = false

        def json = this.jiraWsRepository.getDataForProject(dto)

        if (json.issues?.size() > 0 ) {
            keepGoing = true
            def movedToDev
            json.issues.each{ def i ->

                ChangelogHistoryItemDto changelogHistoryItemDto = new ChangelogHistoryItemDto()
                if (i.changelog) {
                    changelogHistoryItemDto  = new ChangelogHistoryItemDto(i, projectConfig.taskStatusMap)
                }
                LeadTimeDevTimeDto leadTimeDevTimeDto               = new LeadTimeDevTimeDto(i, changelogHistoryItemDto.movedToDevList.min())
                OtherItemsDto otherItemsDto                         = new OtherItemsDto(i)
                this.saveJiraData(projectName, i, changelogHistoryItemDto, leadTimeDevTimeDto, otherItemsDto)
            }
        }
        if(keepGoing) {
            JiraBusiness.log.debug("NEXT PAGE starting at $dto.query.startAt")
            dto.query.startAt += dto.query.maxResults
            this.updateProjectData(projectName, dto, projectConfig)
        }
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

        /**
         * Default constructor in the case that we have no change log information
         */
        ChangelogHistoryItemDto() {}

        /**
         * In the event that we have changelog infomation.
         * @param i - The json array from the result list.
         */
        ChangelogHistoryItemDto(def i, def taskStatusMap) {

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
