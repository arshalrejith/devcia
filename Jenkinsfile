pipeline {
    agent any

    options {
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                bat '"C:\\Users\\snuc\\Downloads\\apache-maven-3.10.0-bin\\apache-maven-3.10.0\\bin\\mvn.cmd" clean test'
            }
        }

        stage('Package') {
            steps {
                bat '"C:\\Users\\snuc\\Downloads\\apache-maven-3.10.0-bin\\apache-maven-3.10.0\\bin\\mvn.cmd" package'
            }
        }
    }

    post {
        always {
            junit testResults: 'course-registration-app/target/surefire-reports/TEST-*.xml',
                  allowEmptyResults: true

            archiveArtifacts artifacts: 'course-registration-app/target/*.jar',
                             fingerprint: true,
                             allowEmptyArchive: true
        }
    }
}
