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
        def path = "/rest/api/2/project"
        def json = makeJiraRequest(path, null)
        def projects = []

        logger.info("GETTING JIRA PROJECTS...")
        for(def i : json) {
            projects.add(i.key)
            logger.debug("$i.key")
        }

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

        def path = "/rest/api/2/search"
        def jiraQuery = "project=$project$fromQuery"
        def query = [jql: jiraQuery, expand:"changelog",startAt: startAt, maxResults: maxResults, fields:"*all"]
        def json = makeJiraRequest(path, query)
        def keepGoing = false
        if(json.total) {
            if(startAt > 0) {
                logger.info("Paging $maxResults records...")
            } else {
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
                def product
                def components = []

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
                            def history = History.findBySourceId(h.id)
                            if(!history) {
                                //TODO not totally sure if i care about updates...
                            } else {
                                history = new History(
                                        dataType: "PTS",
                                        sourceId: h.id,
                                        timestamp: h.created,
                                        changeField: t.field,
                                        newValue: t.toString,
                                        changedBy: h.author.emailAddress,
                                        key: i.key
                                )
                                history.save(flush: true, failOnError: true)
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

                //NOTE this gets what we call "product"
                if(i.fields.customfield_12040) {
                    product = i.fields.customfield_12040.value
                    if(i.fields.customfield_12040.child) {
                        product += " " + i.fields.customfield_12040.child.value
                    }
                    logger.debug("PRODUCT: $product")
                }

                for(def c : i.fields.components) {
                    logger.debug("COMPONENT: $c.name")
                    components.add(c.name)
                }

                def issueType = i.fields.issuetype?.name
                if (issueType) {
                    issueType = i.fields.issuetype.name.replace(" ", "_")
                }

                def createdDate = UtilitiesService.cleanJiraDate(i.fields.created)
                def fin = UtilitiesService.cleanJiraDate(i.fields.resolutiondate)

                //figure out how many days this took to get done.
                //0 means the task hasn't started yet.
                //Round anything less than a day to 1 day.
                def leadTime = 0
                def devTime = 0
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

                //TODO - need a way to figure out estimates based on input
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
                    jiraData.components = components
                    jiraData.product = product
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
                            rawEstimateHealth: estimateHealth.raw,
                            components: components,
                            product: product)
                }

                if(grailsApplication.config.sendDataToCouch == "true") {
                    def couchReturn = couchConnectorService.saveToCouch(jiraData)
                    logger.debug("RETURNED FROM COUCH: $couchReturn")
                    jiraData.couchId = couchReturn
                }
                jiraData.save(flush: true, failOnError: true)
            }
        }

        if(keepGoing) {
            logger.debug("-----")
            logger.debug("NEXT PAGE starting at $startAt")
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

    def makeJiraRequest(path, query) {
        def credentials = grailsApplication.config.jira.credentials
        def url = grailsApplication.config.jira.url //this is defined in application.properties
        try {
            return httpRequestService.callRestfulUrl(url, path, query, true, null, credentials)
            logger.debug("GOT DATA BACK FROM JIRA")
        } catch (Exception ex) {
            //TODO handle this!!!
            logger.error("JIRA FAIL: $ex.message")
        }
    }
}