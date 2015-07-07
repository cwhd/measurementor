package com.nike.mm.business.plugin.data


class StashDataForTests {

    static def COMMIT_HISTORY =
            [
                    values: [
                            [
                                    id: "a2b35698f1975b3f5b70331352f3d588f853a36d",
                                    displayId: "a2b35698f19",
                                    author: [
                                            emailAddress: "Air.Jordan@nike.com"
                                    ],
                                    authorTimestamp: 1431558359000
                            ]
                    ],
                    isLastPage: true,
                    start: 0,
                    limit: 1,
                    nextPageStart: 1
            ]

    static def COMMIT_HISTORY_LAST_PAGE_FALSE =
            [
                    values: [
                            [
                                    id: "a2b35698f1975b3f5b70331352f3d588f853a36d",
                                    displayId: "a2b35698f19",
                                    author: [
                                            emailAddress: "Air.Jordan@nike.com"
                                    ],
                                    authorTimestamp: 1431558359000
                            ]
                    ],
                    isLastPage: false,
                    start: 0,
                    limit: 1,
                    nextPageStart: 1
            ]

    static def FILE_DIFFS =
            [
                    diffs: [
                            hunks: [
                                    [
                                            segments: [
                                                    [
                                                            type: "ADDED",
                                                            lines: [
                                                                    [
                                                                            source: 0,
                                                                            destination: 1,
                                                                            line: "package com.nike.tone.foundation.servlets.authentication;",
                                                                            truncated: false
                                                                    ],
                                                                    [
                                                                            source: 0,
                                                                            destination: 1,
                                                                            line: "package com.nike.tone.foundation.servlets.authentication;",
                                                                            truncated: false
                                                                    ]
                                                            ]
                                                    ],
                                                    [
                                                            type: "REMOVED",
                                                            lines: [
                                                                    [
                                                                            source: 0,
                                                                            destination: 1,
                                                                            line: "package com.nike.tone.foundation.servlets.authentication;",
                                                                            truncated: false
                                                                    ]
                                                            ]
                                                    ]
                                            ]
                                    ]
                            ]
                    ]
            ]

    static def OLD_COMMIT_HISTORY =
            [
                    values: [
                            [
                                    authorTimestamp: 915166800000,
                            ]
                    ],
                    isLastPage: true
            ]


    static def PULL_REQUESTS =
            [
                    values: [
                            [
                                    id:66,
                                    state:"MERGED",
                                    open:false,
                                    closed:true,
                                    createdDate:1427499183841,
                                    updatedDate:1427818939305,
                                    locked:false,
                                    author:[
                                            user:[
                                                    emailAddress:"air.jordan@nike.com",
                                                    id:1860,
                                            ],
                                    ],
                                    reviewers:[
                                            [
                                                    user:[
                                                            emailAddress:"mr.pippen@nike.com",
                                                            id:3063,
                                                    ],
                                            ]
                                    ],
                                    attributes:[
                                            resolvedTaskCount:[
                                                    "0"
                                            ],
                                            openTaskCount:[
                                                    "0"
                                            ],
                                            commentCount:[
                                                    "1"
                                            ]
                                    ]
                            ]
                    ],
                    start:0,
                    size:1,
                    limit:1,
                    isLastPage:true,
                    nextPageStart:1
            ]

     static def COMMIT_COUNT =
             [
                     values: [
                             [
                                     id: "087e9fead465b57414d9e138e2eca305ac9802d7",
                                     displayId: "087e9fead46",
                                     author: [
                                             name: "JLE114",
                                             emailAddress: "James.Lee3@nike.com",
                                             id: 1860,
                                             displayName: "Lee, James (WHQ)",
                                             active: true,
                                             slug: "jle114",
                                             type: "NORMAL"
                                     ],
                                     authorTimestamp: 1427499094000,
                                     message: "Change mockito to mockit-core so it doesn't pull in hamcrest that breaks the reporting of failed tests.",
                                     parents: [
                                             [
                                                     id: "5d025732567d0bd2ea0c3175b206d42d13a7f82a",
                                                     displayId: "5d025732567"
                                             ]
                                     ]
                             ]
                     ],
                     size: 1,
                     isLastPage: true,
                     start: 0,
                     limit: 1,
                     nextPageStart: null
             ]

        static def PULL_REQUESTS_PAGE_FALSE =
                [
                        values: [
                                [
                                        id:66,
                                        state:"MERGED",
                                        open:false,
                                        closed:true,
                                        createdDate:1427499183841,
                                        updatedDate:1427818939305,
                                        locked:false,
                                        author:[
                                                user:[
                                                        emailAddress:"air.jordan@nike.com",
                                                        id:1860,
                                                ],
                                        ],
                                        reviewers:[
                                                [
                                                        user:[
                                                                emailAddress:"mr.pippen@nike.com",
                                                                id:3063,
                                                        ],
                                                ]
                                        ],
                                        attributes:[
                                                resolvedTaskCount:[
                                                        "0"
                                                ],
                                                openTaskCount:[
                                                        "0"
                                                ],
                                                commentCount:[
                                                        "1"
                                                ]
                                        ]
                                ]
                        ],
                        start:0,
                        size:1,
                        limit:1,
                        isLastPage:false,
                        nextPageStart:1
                ]

}
