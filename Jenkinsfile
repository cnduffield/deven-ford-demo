//NOTE: CF space(s), environment values, and properties are defined in 'pipeline.configuration.groovy' file

timeout(time: 15, unit: 'MINUTES') {
    node {
        stage('Pipeline Setup') {
            deleteDir()
            checkout scm
            readConfigFile()
            configureIntegrations()
            configureEnv()
        }

        stage('Build') {
            sh './gradlew pipelineBuild'
        }

        if (property('integrations.checkmarx.enabled')) {
            stage("Quality Check (Checkmarx)") {
                runCheckmarx()
                if (currentBuild.result == "FAILURE") sh 'exit 1'
            }
        }

        if (property('integrations.sonarQube.enabled')) {
            stage("Quality Check (SonarQube)") {
                sh './gradlew pipelineQualityCheck'
            }
        }

        if (env.BRANCH_NAME == 'master') {

            stageForEachCfSpace('Blue: Stage w/ Tests (%s)') {
                sh '[ ! -f pipeline.cf-services.sh ] || (chmod +x pipeline.cf-services.sh && ./pipeline.cf-services.sh)'
                sh './gradlew cfManifest pipelineCfStage acceptanceTests'
            }

            if (property('integrations.nexus.enabled')) {
                stage("Publish (Nexus)") {
                    sh './gradlew pipelinePublish'
                }
            }

            stageForEachCfSpace('Green: Release (%s)') {
                sh './gradlew pipelineCfRelease'
            }

        }

        cleanUp()
    }
}

/************************ HELPER METHODS ************************/

def _config, _configCf

// read configuration from external file
def readConfigFile() {
    evaluate readFile('pipeline.configuration.groovy')
    _config = CONFIGURATION
    _configCf = _config.cfEnvironments?.getAt('DEV') ?: [:]
}

// configure environment relating to third-party integrations
def configureIntegrations() {
    // configure nexus w/ credentials
    if (property('integrations.nexus.enabled')) {
        _withCredentials(property('integrations.nexus.credentialsId')) { user, password ->
            env.BOOST_PUBLISH_TEAM_REPO_USER = user;
            env.BOOST_PUBLISH_TEAM_REPO_PASS = password;
            env.BOOST_PUBLISH_TEAM_REPO_URL = property('integrations.nexus.repoUrl');
        }
    }

    // configure sonarqube w/ credentials
    if (property('integrations.sonarQube.enabled')) {
        _withCredentials(property('integrations.sonarQube.credentialsId')) { user, password ->
            env.BOOST_SONARQUBE_SONAR_LOGIN = password;
            env.BOOST_SONARQUBE_SONAR_PROJECTKEY = property('integrations.sonarQube.projectKey');
        }
    }
}

// re/configure environment using global, specific cf environment, and specific cf space values
def configureEnv(spaceKey = null) {
    def setEnvs = { envMap -> for (def e in envMap) env."${e.key}" = e.value }
    env.cfManifestFile = 'manifest-generated.yml'
    setEnvs _config.env
    setEnvs _configCf.env
    if (spaceKey) {
        setEnvs _configCf.spaces?.getAt(spaceKey)?.env
        cfLogin spaceKey
    }
}

// perform an action for CF space
def stageForEachCfSpace(caption, closure) {
    for (def spaceKey in _configCf.spaces?.keySet()) {
        stage(String.format(caption, spaceKey)) {
            configureEnv spaceKey
            closure spaceKey
        }
    }
}

// login and cache cf session
def cfLogin(spaceKey) {
    env.CF_HOME = "${env.WORKSPACE}/cf-sessions/cf-${spaceKey}"

    if (!fileExists(env.CF_HOME)) {
        def spaceInfo = _configCf.spaces[spaceKey]
        _withCredentials(spaceInfo.credentialsId) { user, password ->
            sh """ mkdir -p "${env.CF_HOME}"; cf login -a "${spaceInfo.apiEndpoint}" -o "${spaceInfo.org}" -s "${spaceInfo.space}" -u "${user}" -p "${password}" """
        }
    }
}

// retrieve value for single or nested property from configuration (i.e. pipeline.configuration.groovy)
def property(name, defaultValue = null) {
    def value = _config
    name.tokenize('.').each { value = value?.get(it) }
    value ?: defaultValue
}

// credentials wrapper helper
def _withCredentials(credentialsId, closure) {
    if (!credentialsId) return
    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: credentialsId, usernameVariable: '_user_', passwordVariable: '_password_']]) {
        closure _user_, _password_
    }
}

// finalize
def cleanUp() {
    dir("${env.WORKSPACE}/cf-sessions") { deleteDir() }
}

// executes Checkmarx analysis
def runCheckmarx() {
    step([$class: 'CxScanBuilder', comment: 'Scan Triggered by Jenkins Pipeline', credentialsId: property('integrations.checkmarx.credentialsId'), excludeFolders: '', excludeOpenSourceFolders: '',
                  exclusionsSetting: 'global', failBuildOnNewResults: true, failBuildOnNewSeverity: 'HIGH', filterPattern: '''!**/_cvs/**/*,     !**/.svn/**/*,     !**/.hg/**/*,     !**/.git/**/*,
                  !**/.bzr/**/*,    !**/bin/**/*,    !**/obj/**/*,    !**/backup/**/*,     !**/.idea/**/*,    !**/*.DS_Store,    !**/*.ipr,    !**/*.iws,
                  !**/*.bak,    !**/*.tmp,    !**/*.aac,    !**/*.aif,     !**/*.iff,    !**/*.m3u,    !**/*.mid,    !**/*.mp3,
                  !**/*.mpa,    !**/*.ra,    !**/*.wav,    !**/*.wma,     !**/*.3g2,    !**/*.3gp,    !**/*.asf,    !**/*.asx,
                  !**/*.avi,    !**/*.flv,    !**/*.mov,    !**/*.mp4,     !**/*.mpg,    !**/*.rm,    !**/*.swf,    !**/*.vob,
                  !**/*.wmv,    !**/*.bmp,    !**/*.gif,    !**/*.jpg,     !**/*.png,    !**/*.psd,    !**/*.tif,    !**/*.swf,
                  !**/*.jar,    !**/*.zip,    !**/*.rar,    !**/*.exe,     !**/*.dll,    !**/*.pdb,    !**/*.7z,    !**/*.gz,
                  !**/*.tar.gz,    !**/*.tar,    !**/*.gz,    !**/*.ahtm,     !**/*.ahtml,    !**/*.fhtml,    !**/*.hdm,    !**/*.hdml,
                  !**/*.hsql,    !**/*.ht,    !**/*.hta,    !**/*.htc,     !**/*.htd,    !**/*.war,    !**/*.ear,    !**/*.htmls,
                  !**/*.ihtml,    !**/*.mht,    !**/*.mhtm,    !**/*.mhtml,     !**/*.ssi,    !**/*.stm,    !**/*.stml,    !**/*.ttml,
                  !**/*.txn,    !**/*.xhtm,    !**/*.xhtml,    !**/*.class,     !**/*.iml,    !Checkmarx/Reports/*.*''',
                  fullScanCycle: 10, teamPath: property('integrations.checkmarx.teamPath'), includeOpenSourceFolders: '', osaArchiveIncludePatterns: '*.jar', osaEnabled: true,
                  vulnerabilityThresholdEnabled:true, osaHighThreshold:0, osaMediumThreshold:10, osaInstallBeforeScan: false, password: '',
                  preset: '36', projectName: property('integrations.checkmarx.projectName'), sastEnabled: true, serverUrl: property('integrations.checkmarx.serverUrl'),
                  sourceEncoding: '1', username: '', vulnerabilityThresholdResult: 'FAILURE', waitForResultsEnabled: true]
    )
}
