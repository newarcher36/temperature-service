pipeline {

    agent any

    tools {
        maven 'maven 3.8.5'
    }

    environment {
        DOCKER_HUB_PASS = credentials('docker-hub-password')
    }

    stages {
        stage('Initialize') {
            steps {
                sh '''
                echo "PATH = ${PATH}"
                echo "M2_HOME = ${M2_HOME}"
                '''
            }
        }
        stage("build-test_meteo-station") {
            steps {
                sh 'echo "Compiling and launching unit (and in future also integration test)"'
                sh 'mvn clean install'
            }
            post {
                success {
                    junit 'target/surefire-reports/**/*.xml'
                    junit 'target/failsafe-reports/**/*.xml'
                }
            }
        }
        stage("build-docker-image") {
            steps {
                sh 'pwd'
                sh 'echo "Building docker image of temperature-service"'
                sh 'docker build -t newarcher/temperature-service:latest .'
            }
        }
        stage("publish-docker-image") {
            steps {
                sh 'echo "Pushing image newarcher/temperature-service:latest to docker hub"'
                sh 'docker login -u newarcher -p $DOCKER_HUB_PASS'
                sh 'docker push newarcher/temperature-service:latest'
                sh 'docker logout'
                sh 'echo "done!"'
            }
        }
    }
}