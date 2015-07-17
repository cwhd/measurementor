package com.nike.mm.business.plugin.data

/**
 * Created by rparr2 on 6/23/15.
 */
interface IJiraDataForTests {

    Object JIRA_DATA =
            [
                    startAt: 0,
                    maxResults: 1,
                    total: 1485,
                    issues: [
                            [
                                    id: "606335",
                                    key: "DSDB-1780",
                                    fields: [
                                            assignee: [
                                                    name: "bditto",
                                                    emailAddress: "brandon.ditto@nike.com"
                                            ],
                                            issuetype: [
                                                    name: "Service"
                                            ],
                                            status: [
                                                    name: "In Definition",
                                                    id: "10005",
                                                    statusCategory: [
                                                            id: 2,
                                                            key: "new",
                                                            name: "To Do"
                                                    ]
                                            ],
                                            creator: [
                                                    emailAddress: "Syed.Mahmud@nike.com"
                                            ],
                                            customfield_10013: 5,
                                            customfield_12040: null,
                                            resolutiondate:"2015-06-11T15:25:25.000-0700",
                                            labels: [],
                                            components: [],
                                            comment: [
                                                    total: 0
                                            ],
                                            created: "2015-05-11T15:25:25.000-0700"
                                    ],
                                    changelog: [
                                            startAt: 0,
                                            maxResults: 0,
                                            total: 0,
                                            histories: [
                                                    [
                                                            id: "5191976",
                                                            created: "2014-12-03T17:37:26.000-0800",
                                                            author: [
                                                                emailAddress: "made.up@email.com",
                                                            ],
                                                            items: [
                                                                    [
                                                                            field: "Rank",
                                                                            fieldtype: "custom",
                                                                            from: null,
                                                                            fromString: null,
                                                                            to: null,
                                                                            toString: "Ranked higher"
                                                                    ]
                                                            ]
                                                    ]
                                            ]
                                    ]
                            ]
                    ]
            ]

        Object JIRA_DATA_AUTHOR_NULL =
                [
                        startAt: 0,
                        maxResults: 1,
                        total: 1485,
                        issues: [
                                [
                                        id: "606335",
                                        key: "DSDB-1780",
                                        fields: [
                                                assignee: [
                                                        name: "bditto",
                                                        emailAddress: "brandon.ditto@nike.com"
                                                ],
                                                issuetype: [
                                                        name: "Service"
                                                ],
                                                status: [
                                                        name: "In Definition",
                                                        id: "10005",
                                                        statusCategory: [
                                                                id: 2,
                                                                key: "new",
                                                                name: "To Do"
                                                        ]
                                                ],
                                                creator: [
                                                        emailAddress: "Syed.Mahmud@nike.com"
                                                ],
                                                customfield_10013: 5,
                                                customfield_12040: null,
                                                resolutiondate:"2015-06-11T15:25:25.000-0700",
                                                labels: [],
                                                components: [],
                                                comment: [
                                                        total: 0
                                                ],
                                                created: "2015-05-11T15:25:25.000-0700"
                                        ],
                                        changelog: [
                                                startAt: 0,
                                                maxResults: 0,
                                                total: 0,
                                                histories: [
                                                        [
                                                                id: "5191976",
                                                                created: "2014-12-03T17:37:26.000-0800",
                                                                items: [
                                                                        [
                                                                                field: "Rank",
                                                                                fieldtype: "custom",
                                                                                from: null,
                                                                                fromString: null,
                                                                                to: null,
                                                                                toString: "Ranked higher"
                                                                        ]
                                                                ]
                                                        ]
                                                ]
                                        ]
                                ]
                        ]
                ]

        Object JIRA_DATA_EMAIL_NULL =
                [
                        startAt: 0,
                        maxResults: 1,
                        total: 1485,
                        issues: [
                                [
                                        id: "606335",
                                        key: "DSDB-1780",
                                        fields: [
                                                assignee: [
                                                        name: "bditto",
                                                        emailAddress: "brandon.ditto@nike.com"
                                                ],
                                                issuetype: [
                                                        name: "Service"
                                                ],
                                                status: [
                                                        name: "In Definition",
                                                        id: "10005",
                                                        statusCategory: [
                                                                id: 2,
                                                                key: "new",
                                                                name: "To Do"
                                                        ]
                                                ],
                                                creator: [
                                                        emailAddress: "Syed.Mahmud@nike.com"
                                                ],
                                                customfield_10013: 5,
                                                customfield_12040: null,
                                                resolutiondate:"2015-06-11T15:25:25.000-0700",
                                                labels: [],
                                                components: [],
                                                comment: [
                                                        total: 0
                                                ],
                                                created: "2015-05-11T15:25:25.000-0700"
                                        ],
                                        changelog: [
                                                startAt: 0,
                                                maxResults: 0,
                                                total: 0,
                                                histories: [
                                                        [
                                                                id: "5191976",
                                                                created: "2014-12-03T17:37:26.000-0800",
                                                                author: [],
                                                                items: [
                                                                        [
                                                                                field: "Rank",
                                                                                fieldtype: "custom",
                                                                                from: null,
                                                                                fromString: null,
                                                                                to: null,
                                                                                toString: "Ranked higher"
                                                                        ]
                                                                ]
                                                        ]
                                                ]
                                        ]
                                ]
                        ]
                ]

        Object JIRA_DATA_NO_ISSUES =
                [
                        startAt: 0,
                        maxResults: 1,
                        total: 1485
                ]
}