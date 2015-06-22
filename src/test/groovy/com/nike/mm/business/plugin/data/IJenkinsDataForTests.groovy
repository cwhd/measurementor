package com.nike.mm.business.plugin.data

/**
 * Created by rparr2 on 6/22/15.
 */
interface IJenkinsDataForTests {

    Object API_JSON =
            [
                    jobs: [
                            [
                                    name: "Achievements",
                                    url: "https://someurl.com/job/Achievements/"
                            ]
                    ]
            ]

    Object JOBS_JOBS_API =
            [
                    "jobs": [
                            [
                                    "name": "AchievementsAdminService",
                                    "url": "https://someurl.com/job/Achievements/job/AchievementsAdminService/"
                            ]
                    ]
            ]
    Object JOBS_JOBS_JOBS_API =
            [
                    "jobs": [
                            [
                                    "name": "achievements-admin-ami",
                                    "url": "https://someurl.com/job/Achievements/job/AchievementsAdminService/job/achievements-admin-ami/",
                            ]
                    ]
            ]

    Object BUILDS_API =
            [
                    "builds": [
                            [
                                    "number": 34,
                                    "url": "https://someurl.com/job/Achievements/job/AchievementsAdminService/job/achievements-admin-ami/34/"
                            ]
                    ]
            ]

    Object BUILD_API =
            [
                    "actions": [
                            [
                                    "causes": [
                                            [
                                                    "shortDescription": "Started by upstream project \"Achievements/AchievementsAdminService/achievements-admin-war\" build number 38",
                                                    "upstreamBuild": 38,
                                                    "upstreamProject": "Achievements/AchievementsAdminService/achievements-admin-war",
                                                    "upstreamUrl": "job/Achievements/job/AchievementsAdminService/job/achievements-admin-war/"
                                            ]
                                    ]
                            ],
                            [
                                    "lastBuiltRevision": [
                                            "SHA1": "374fc75507754e3ae45695b93be4b47ef994d416"
                                    ],
                                    "remoteUrls": [
                                            "http://stash.nikedev.com/scm/pt/jenkins-templates.git"
                                    ],
                                    "scmName": ""
                            ]
                    ],
                    "duration": 301676,
                    "fullDisplayName": "Achievements » AchievementsAdminService » achievements-admin-ami #34",
                    "id": "34",
                    "number": 34,
                    "queueId": 16584,
                    "result": "SUCCESS",
                    "timestamp": 1434086249281,
                    "url": "https://jenkins.tools.nikecloud.com/job/Achievements/job/AchievementsAdminService/job/achievements-admin-ami/34/"
            ]
}