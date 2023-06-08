pipeline {
  agent any
  stages {
    stage('Build Parameters') {
      when {
        branch "main"
      }
      steps {
        script {
          properties([parameters([
                  booleanParam(defaultValue: false,
                          description: 'Deploy',
                          name: 'DEPLOY'),
                  booleanParam(defaultValue: false,
                          description: 'Release',
                          name: 'RELEASE'),
                  stringParam(defaultValue: 'auto',
                          description: 'Version, "auto" for automatic versioning, or specify a version number (e.g. 1.0.0)',
                          name: 'VERSION'),
          ])])
        }
      }
    }

    stage('Build and Publish') {
      when {
        expression {
          return params.DEPLOY != true
        }
      }
      steps {
        sh 'bin/gen parser'
        sh 'bin/dev ensure-no-changes'
        sh 'bin/mvn clean deploy --update-snapshots --no-transfer-progress'
      }
    }

    stage('Release') {
      when {
        branch "main"
        expression {
          return params.RELEASE == true
        }
      }
      steps {
        sh "bin/dev release '$VERSION'"
      }
    }

    stage('Archive') {
      steps {
        archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
      }
    }
  }
}
