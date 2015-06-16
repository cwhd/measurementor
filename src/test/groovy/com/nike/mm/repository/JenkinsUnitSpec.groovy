package com.nike.mm.repository

import spock.lang.Specification

class JenkinsUnitSpec extends Specification{

	/*
	 * Get all the jobs in the server.
	 *
	 * http://192.168.37.90:8080/api/json
	 *
		{
			,"mode":"NORMAL"
			,"nodeDescription":"the master Jenkins node"
			,"nodeName":""
			,"numExecutors":2
			,"jobs":[
				{
					 "name":"<repo>"
					,"url":"http://192.168.37.90:8080/job/<repo>/"
					,"color":"blue"
				}
			]
		}
	*/
	
	/*
	 * Get list of builds for an individual repo.
	 * 
	 * http://192.168.37.90:8080/job/<repo>/api/json
	 *
		{
			 "url":"http://192.168.37.90:8080/job/<repo>/"
			,"buildable":true
			,"builds":[
				{
					 "number":1
					,"url":"http://192.168.37.90:8080/job/<repo>/1/"
				}
			]
			,"color":"blue"
			,"healthReport":[
				{
					 "description":"Build stability: No recent builds failed."
					,"iconClassName":"icon-health-80plus"
					,"iconUrl":"health-80plus.png"
					,"score":100
				}
			]
			,"lastBuild":{ "number":1,"url":"http://192.168.37.90:8080/job/Java-coding-excercise-dnsewolo/1/"}
			,"lastCompletedBuild":{ "number":1,"url":"http://192.168.37.90:8080/job/Java-coding-excercise-dnsewolo/1/"}
			,"lastFailedBuild":null
			,"lastStableBuild":{ "number":1,"url":"http://192.168.37.90:8080/job/Java-coding-excercise-dnsewolo/1/"}
			,"lastSuccessfulBuild":{ "number":1,"url":"http://192.168.37.90:8080/job/Java-coding-excercise-dnsewolo/1/"}
			,"lastUnstableBuild":null
			,"lastUnsuccessfulBuild":null
			,"nextBuildNumber":2
			,"modules":[
				{
					 "name":"com.teksystems:candidateName"
					,"url":"http://192.168.37.90:8080/job/<repo>/com.teksystems$candidateName/"
					,"color":"blue"
					,"displayName":"candidateName-SalesTaxes"
				}
			]
		}
	*/
	
	/*
	 * Gets an individual build report.
	 * 
	 * http://192.168.37.90:8080/job/Java-coding-excercise-dnsewolo/1//api/json
	 *
		{
				{
					"buildsByBranchName":{
						"origin/master":{
							 "buildNumber":1
							,"buildResult":null
							,"marked":{
								 "SHA1":"8c489c00f88537483bb84b8cb4fbbc1b3778399f"
								,"branch":[
									{
										 "SHA1":"8c489c00f88537483bb84b8cb4fbbc1b3778399f"
										,"name":"origin/master"
									}
								]
							}
							,"revision":{
								 "SHA1":"8c489c00f88537483bb84b8cb4fbbc1b3778399f"
								,"branch":[
									{
										 "SHA1":"8c489c00f88537483bb84b8cb4fbbc1b3778399f"
										,"name":"origin/master"
									}
								]
							}
						}
					}
					,"lastBuiltRevision":{
						 "SHA1":"8c489c00f88537483bb84b8cb4fbbc1b3778399f"
						,"branch":[
							{
								 "SHA1":"8c489c00f88537483bb84b8cb4fbbc1b3778399f"
								,"name":"origin/master"
							}
						]
					}
					,"remoteUrls":[
						"https://jenkins-user@192.168.37.90/coding-exercise-group/Java-coding-excercise-dnsewolo.git"
					]
				}
			]
			,"duration":55506
			,"fullDisplayName":"Java-coding-excercise-dnsewolo #1"
			,"id":"2015-05-15_09-32-18"
			,"number":1
			,"result":"SUCCESS"
			,"timestamp":1431682338519
			,"url":"http://192.168.37.90:8080/job/Java-coding-excercise-dnsewolo/1/"
			,"builtOn":""
			,"changeSet":{
				 "items":[]
				,"kind":"git"
			}
			,"culprits":[]
			,"mavenArtifacts":{}
			,"mavenVersionUsed":"3.2.2"
		}
	 */
}
