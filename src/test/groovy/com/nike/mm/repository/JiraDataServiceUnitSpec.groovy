package com.nike.mm.repository

import spock.lang.Specification;

class JiraDataServiceUnitSpec extends Specification{
	
	/*
		curl -D- -u rparry:%Pices1971 -X GET -H "Content-Type: application/json" http://jpt5.teksystems.com/rest/api/2/project/TASCEMS
		[
			{
				 "self":"http://jpt5.teksystems.com/rest/api/2/project/11008"
				,"id":"11008"
				,"key":"TASCEMS"
				,"name":"TASC EMS"
			}
		]
	*/
	
	/*
	 	curl -D- -u rparry:%Pices1971 -X GET -H "Content-Type: application/json" http://jpt5.teksystems.com/rest/api/2/project/11008
	 
		{
		    "expand": "projectKeys",
		    "self": "http://jpt5.teksystems.com/rest/api/2/project/11008",
		    "id": "11008",
		    "key": "TASCEMS",
		    "lead": {
		        "key": "dtardif",
		        "name": "dtardif",
		        "displayName": "Tardif, Daniel",
		        "active": true
		    },
		    "components": [
		        {
		            "name": "MyFile V2 R1 UAT",
		            "isAssigneeTypeValid": false
		        }
		    ],
		    "issueTypes": [
		        {
		            "id": "1",
		            "name": "Bug",
		        }
		    ],
		    "assigneeType": "UNASSIGNED",
		    "versions": [
		        {
		            "id": "10624",
		            "name": "Release 1 (TASCEMS)",
		            "startDate": "2014-10-06",
		            "releaseDate": "2015-06-02",
		            "overdue": false,
		            "userStartDate": "06/Oct/14",
		            "userReleaseDate": "02/Jun/15",
		            "projectId": 11008
		        }
		    ],
		    "name": "TASC EMS",
		    "roles": {
		        "Users": "http://jpt5.teksystems.com/rest/api/2/project/11008/role/10000",
		        "Administrators": "http://jpt5.teksystems.com/rest/api/2/project/11008/role/10002",
		        "Developers": "http://jpt5.teksystems.com/rest/api/2/project/11008/role/10001"
		    }
		}					
	*/
	/*
		 curl -D- -u rparry:%Pices1971 -X GET -H "Content-Type: application/json" http://jpt5.teksystems.com/rest/api/2/project/11008/statuses -o 
		 [
		    {
		        "name": "Bug",
		        "statuses": [
		             {"name": "New","statusCategory": {"name": "New"}}
		        ]
		    }
		    ,{
		        "name": "Epic",
		        "statuses": [
		            {"name": "New","statusCategory": {"name": "New"}}
		        ]
		    }
		] 
	 */
	
	
	/*
		curl -D- -u rparry:%Pices1971 -X GET -H "Content-Type: application/json" http://jpt5.teksystems.com/rest/api/2/project/11008/versions	 	
		[
		    {
		        "id": "10624",
		        "name": "Release 1 (TASCEMS)",
		        "archived": false,
		        "released": false,
		        "startDate": "2014-10-06",
		        "releaseDate": "2015-06-02",
		        "overdue": false,
		        "userStartDate": "06/Oct/14",
		        "userReleaseDate": "02/Jun/15",
		        "projectId": 11008
		]
	*/
	/*
	 	Gets the greenhoper id's for the projects.
		curl -u rparry:%Pices1971 -X GET -H "Content-Type: application/json" 'http://jpt5.teksystems.com/rest/greenhopper/1.0/rapidviews/list'
			{
	            "id": 109,
	            "name": "TASC - OneSource Scrum Board",
	            "canEdit": false,
	            "sprintSupportEnabled": true,
	            "filter": {
	                "id": 11175,
	                "name": "Filter for TASC Scrum Board",
	                "query": "project = \"TASC EMS\" ORDER BY Rank ASC",
	                "owner": {
	                    "userName": "jplumpto",
	                    "displayName": "Plumpton, Joanna",
	                    "renderedLink": "        <a class=\"user-hover\" rel=\"jplumpto\" id=\"_jplumpto\" href=\"/secure/ViewProfile.jspa?name=jplumpto\">Plumpton, Joanna</a>"
	                }
	            }
	        }
		*/
	
		/*
			curl -u rparry:%Pices1971 -X GET -H "Content-Type: application/json" 'http://jpt5.teksystems.com/rest/greenhopper/1.0/sprintquery/109?includeFutureSprints=true&includeHistoricSprints=true'
			{
		    	"sprints": [
		        	{
			            "id": 337,
			            "name": "TASC OS Sprint 16",
			            "state": "CLOSED",
			            "linkedPagesCount": 0
			        }
		    	],
		    	"rapidViewId": 135
			}		

		*/
	
		/*
		 	curl -D- -u rparry:%Pices1971 -X POST -H "Content-Type: application/json" --data '{"jql":"project = 11008 and sprint = 337","startAt":0,"maxResults":2}' "http://jpt5.teksystems.com/rest/api/2/search"
			{
			    "startAt": 0,
			    "maxResults": 1,
			    "total": 132,
			    "issues": [
			        {
			            "id": "33720",
			            "key": "TASCEMS-1261",
			            "fields": {
			                "summary": "Unit testing of the search/build/docker",
			                "progress": {
			                    "progress": 14400,
			                    "total": 14400,
			                    "percent": 100
			                },
			                "issuetype": {
			                    "id": "5",
			                    "name": "Sub-task",
			                    "subtask": true
			                },
			                "timespent": 14400,
			                "reporter": {
			                    "name": "mdolmato",
			                    "emailAddress": "mdolmato@teksystems.com",
			                    "displayName": "Dolmatov, Mykola",
			                    "active": true
			                },
			                "updated": "2015-05-19T11:03:45.000-0400",
			                "created": "2015-05-12T16:45:00.000-0400",
			                "status": {
			                    "name": "Done",
			                    "statusCategory": {
			                        "key": "done",
			                        "name": "Complete"
			                    }
			                },
			                "parent": {
			                    "id": "32630",
			                    "key": "TASCEMS-1072",
			                    "fields": {
			                        "status": {
			                            "name": "Done",
			                            "id": "10002",
			                            "statusCategory": {
			                                "key": "done",
			                                "name": "Complete"
			                            }
			                        },
			                        "issuetype": {
			                            "id": "7",
			                            "name": "Story",
			                        }
			                    }
			                },
			                "workratio": 100,
			                "aggregateprogress": {
			                    "progress": 14400,
			                    "total": 14400,
			                    "percent": 100
			                },
			                "timeoriginalestimate": 14400,
			                "fixVersions": [
			                    {
			                        "id": "10624",
			                        "name": "Release 1 (TASCEMS)",
			                    }
			                ],
			                "resolution": {
			                    "id": "1",
			                    "name": "Fixed"
			                },
			                "resolutiondate": "2015-05-19T11:03:45.000-0400",
			                "creator": {
			                    "name": "mdolmato",
			                    "emailAddress": "mdolmato@teksystems.com",
			                    "displayName": "Dolmatov, Mykola",
			                },
			                "aggregatetimeoriginalestimate": 14400,
			                "duedate": null,
			                "assignee": {
			                    "name": "mdolmato",
			                    "emailAddress": "mdolmato@teksystems.com",
			                    "displayName": "Dolmatov, Mykola",
			                },
			                "aggregatetimespent": 14400
			            }
			        }
			    ]
			}
	*/
}
