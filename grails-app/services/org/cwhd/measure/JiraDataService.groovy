package org.cwhd.measure

import grails.transaction.Transactional
import java.util.concurrent.TimeUnit
import org.apache.commons.logging.LogFactory

/**
 * This service gets a bunch of data out of JIRA and scrubs it a bit
 */
@Transactional
class JiraDataService {
    private static final logger = LogFactory.getLog(this)
    def grailsApplication
    def httpRequestService
    def elasticSearchService
    def couchConnectorService

    /**
     * this gets a list of JIRA projects that are available. I don't think i really need this anymore...
     * @return
     */
    def getProjects() {
        def url = grailsApplication.config.jira.url
        def path = "/rest/api/2/project"
        def json = httpRequestService.callRestfulUrl(url, path, null, true)
        def projects = []


        for(def i : json) {
            projects.add(i.key)
            logger.info("$i.key")
        }

        //projects.add("ACOE")
        return projects
    }

    /**
     * get data from JIRA from a project
     * @param startAt
     * @param maxResults
     * @param project the JIRA project key you want data from
     * @param fromDate the earliest date you want data from.  Use YYYY-MM-dd format
     * @return
     */
    def getData(startAt, maxResults, project, fromDate) {
        if(!startAt) {
            startAt = 0
        }
        if(!maxResults) {
            maxResults = 100
        }
        def fromQuery = ""
        if(fromDate) {
            fromQuery = " AND updatedDate>$fromDate"
        }

        def url = grailsApplication.config.jira.url
        def path = "/rest/api/2/search"
        def jiraQuery = "project=$project$fromQuery"
        def query = [jql: jiraQuery, expand:"changelog",startAt: startAt, maxResults: maxResults, fields:"*all"]

        def json = httpRequestService.callRestfulUrl(url, path, query, true)
        def keepGoing = false
        if(json.total) {
            if(startAt == 0) {
                logger.info("-----")
                logger.info("$json.total records in $project")
            }
        } else {
            logger.info("-----")
            logger.info("no results for $project!")
        }

        //NOTE we need to set the map so we know what direction things are moving in; this relates to the moveForward & moveBackward stuff
        //TODO this needs to be a parameter that gets passed in based on the project
        def taskStatusMap = ["In Definition": 1, "Dev Ready":2, "Dev":3, "QA Ready":4, "QA":5, "Deploy Ready":6, "Done":7]

        if(json.issues?.size() > 0) { //if we have issues and there are more than 0 of them
            keepGoing = true
            for (def i : json.issues) {

                //NOTE need to define these before we try to get them
                def moveForward = 0
                def moveBackward = 0
                def assignees = []
                def tags = []
                def movedToDev
                def commentCount = 0
                def movedToDevList = []

                //NOTE the changelog gets us great historical information
                if (i.changelog) {
                    for (def h : i.changelog.histories) {
                        for (def t : h.items) {
                            //NOTE the following conditionals flatten history into stuff we can work with easier
                            if (t.field == "status") { //NOTE get the progression for churn
                                if (taskStatusMap[t.fromString] > taskStatusMap[t.toString]) {
                                    moveBackward++
                                } else {
                                    moveForward++
                                    movedToDevList.add(UtilitiesService.cleanJiraDate(h.created))
                                    //when it was moved to dev
                                }
                            } else if (t.field == "assignee") {
                                //NOTE get everyone that worked on this issue, or at least was assigned to it
                                if (t.toString) {
                                    assignees.add(UtilitiesService.makeNonTokenFriendly(t.toString))
                                }
                            }
                        }
                    }
                    //at this point we should know when this moved to dev
                    movedToDev = movedToDevList.min()
                } else {
                    logger.debug("changelog is null!")
                }

                commentCount = i.fields.comment?.total

                tags = i.fields.labels //note this gets tags

                //NOTE this block gets the story points; for some reason this needs to be converted to an int?
                def storyPoints = 0
                if (i.fields.customfield_10013) {
                    storyPoints = i.fields.customfield_10013.toInteger()
                }

                def issueType = i.fields.issuetype?.name
                if (issueType) {
                    issueType = i.fields.issuetype.name.replace(" ", "_")
                }

                def createdDate = UtilitiesService.cleanJiraDate(i.fields.created)
                def fin = UtilitiesService.cleanJiraDate(i.fields.resolutiondate)

                //figure out how many days this took to get done
                def leadTime = 0
                def devTime = 0
                if (createdDate && fin) {
                    long duration = fin.getTime() - createdDate.getTime()
                    leadTime = TimeUnit.MILLISECONDS.toDays(duration)
                }

                if (movedToDev && fin) {
                    long duration = fin.getTime() - movedToDev.getTime()
                    devTime = TimeUnit.MILLISECONDS.toDays(duration)
                } else if (movedToDev && !fin) {
                    long duration = new Date().getTime() - movedToDev.getTime()
                    devTime = TimeUnit.MILLISECONDS.toDays(duration)
                }

                def estimateHealth = UtilitiesService.estimateHealth(storyPoints, devTime, 13, 9, [1, 2, 3, 5, 8, 13])

                def jiraData = JiraData.findByKey(i.key)
                if (jiraData) {
                    jiraData.createdBy = UtilitiesService.cleanEmail(i.fields.creator?.emailAddress)
                    jiraData.issuetype = issueType
                    jiraData.movedForward = moveForward
                    jiraData.movedBackward = moveBackward
                    jiraData.storyPoints = storyPoints
                    jiraData.finished = fin
                    jiraData.assignees = assignees
                    jiraData.tags = tags
                    jiraData.dataType = "PTS"
                    jiraData.leadTime = leadTime
                    jiraData.devTime = devTime
                    jiraData.commentCount = commentCount
                    jiraData.jiraProject = project
                    jiraData.rawEstimateHealth = estimateHealth.raw
                    jiraData.estimateHealth = estimateHealth.result
                } else {
                    jiraData = new JiraData(key: i.key,
                            created: createdDate,
                            createdBy: UtilitiesService.cleanEmail(i.fields.creator?.emailAddress),
                            issuetype: issueType,
                            movedForward: moveForward,
                            movedBackward: moveBackward,
                            storyPoints: storyPoints,
                            finished: fin,
                            assignees: assignees,
                            tags: tags,
                            dataType: "PTS",
                            leadTime: leadTime,
                            devTime: devTime,
                            commentCount: commentCount,
                            jiraProject: project,
                            estimateHealth: estimateHealth.result,
                            rawEstimateHealth: estimateHealth.raw)
                }

                def couchReturn = couchConnectorService.saveToCouch(jiraData)
                logger.debug("RETURNED FROM COUCH: $couchReturn")
                jiraData.couchId = couchReturn
                jiraData.save(flush: true, failOnError: true)
            }
        }

        if(keepGoing) {
            logger.debug("-----")
            logger.debug("NEXT PAGE starting at $startAt")
            logger.info(".")
            getData(startAt + maxResults, maxResults, project, fromDate)
        }
    }

    /**
     * this will reindex all the JiraData we have in EC if we need to.  This is only needed
     * when something blows up.
     * @return
     */
    def reIndexEC() {
        elasticSearchService.index(JiraData)
    }
}