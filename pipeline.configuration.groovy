CONFIGURATION = [

    // third-party integrations
    integrations: [
        nexus: [
            enabled: false,
            credentialsId: 'nexus-private',   // Jenkins credentials ID
            repoUrl: 'https://www.nexus.ford.com/repository/[TEAM-REPO-NAME]',
        ],

        sonarQube: [
            enabled: false,
            credentialsId: 'sonar-private',   // Jenkins credentials ID
            projectKey: '[SONARQUBE-PROJECT-KEY]'
        ],
        checkmarx: [
                enabled: false,
                credentialsId: 'checkmarx-private',   // Jenkins credentials ID
                serverUrl: 'https://www.checkmarx.ford.com',
                projectName: '[CHECKMARX-PROJECT-NAME]',
                teamPath: '[CHECKMARX-TEAM-PATH]',
        ],
    ],

    // cli environment variables (optional)
    env: [
        //if your Jenkins supports multiple JDK's (8 & 11) you may need to set JAVA_HOME
        //below is example where Jenkins defaults to 8, but we need 11 for this project; this is only an example your path will likely differ
        //JAVA_HOME: '/proj/dvnops/jenkins/jdk-11.0.2+9',
        //ACCEPTANCE_DEFAULT_BASIC_AUTH_USER: 'user',
        //ACCEPTANCE_DEFAULT_BASIC_AUTH_PASS: 'password',
    ],

    // deploy environments (e.g. DEV, QA) and their space(s)
    /***** REMOVE COMMENT BLOCK LINES (THIS LINE AND BELOW) AND FILL IN THIS SECTION:
    cfEnvironments: [
        DEV: [
            spaces: [
                EDC1: [
                    apiEndpoint: 'https://api.sys.pp01.edc1.cf.ford.com',
                    org: 'TEAM-ORG',
                    space: 'TEAM-DEV',
                    credentialsId: 'pcf-pre-prod', // Jenkins credentials ID
                    env: [ // cli environment values for this space only
                        cfDomains: 'apps.pp01i.edc1.cf.ford.com',  // comma delimited list of CF route domains
                        cfManifestTarget: 'dev-edc1',     // key tied to the set of values to use in manifest yml file (see manifest-template-settings.json)
                    ]
                ],
                ECC: [
                    apiEndpoint: 'https://api.sys-pcf02v2.cf.ford.com',
                    org: 'TEAM-ORG',
                    space: 'TEAM-DEV',
                    credentialsId: 'pcf-pre-prod',
                    env: [
                        cfDomains: 'apps-pcf02v2i.cf.ford.com',
                        cfManifestTarget: 'dev-ecc',
                    ]
                ]
            ],
            env: [ // cli environment values for all DEV spaces
                // sample oauth2 credentials used by acceptance tests
                ACCEPTANCE_DEFAULT_OAUTH2_CLIENT_ACCESS_TOKEN_URI: 'https://fd-usr-sso.login.sys.pp01.edc1.cf.ford.com/oauth/token',
                ACCEPTANCE_DEFAULT_OAUTH2_CLIENT_ID: 'cb9fdd82-f8b1-44f1-927b-e892c3bff5b1',
                ACCEPTANCE_DEFAULT_OAUTH2_CLIENT_SECRET: 'f3d29b84-3328-47c8-8a09-b11e9147af3c',
            ]
        ],
    ]
    ***** COMMENT BLOCK LINE *****/

]
