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

}