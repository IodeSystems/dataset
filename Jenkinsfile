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
                          description: 'Skip Ui Tests',
                          name: 'SKIP_UI_TESTS')])])
        }
      }
    }

    stage('Build Project') {
      when {
        not {
          branch 'main'
        }
        expression {
          return params.SKIP_UI_TESTS == false
        }
      }
      steps {
        echo 'Building..'
        sh 'bin/build all'
      }
    }

    stage('Publish') {
      when {
        branch 'main'
      }
      steps {
        sh 'bin/dev release'
      }
    }
    stage('Archive') {
      steps {
        archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
      }
    }
  }
}
