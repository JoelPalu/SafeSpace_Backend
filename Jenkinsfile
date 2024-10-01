
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/JoelPalu/SafeSpace_Backend.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Code Coverage') {
            steps {
                jacoco execPattern: '**/target/jacoco.exec'
            }
        }
    }

    post {
        always {
            junit '**/target/surefire-reports/*.xml'
            jacoco execPattern: '**/target/jacoco.exec'
        }
    }

    environment {
            // Define Docker Hub credentials ID
            DOCKERHUB_CREDENTIALS_ID = 'docker_credentials'
            // Define Docker Hub repository name
            DOCKERHUB_REPO = 'kirillsaveliev/safespacebacked'
            // Define Docker image tag
            DOCKER_IMAGE_TAG = 'latest'
        }
        stages {
            stage('Checkout') {
                steps {
                    // Checkout code from Git repository
                    git 'https://github.com/JoelPalu/SafeSpace_Backend.git'
                }
            }
            stage('Build Docker Image') {
                steps {
                    // Build Docker image
                    script {
                        docker.build("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}")
                    }
                }
            }
            stage('Push Docker Image to Docker Hub') {
                steps {
                    // Push Docker image to Docker Hub
                    script {
                        docker.withRegistry('https://index.docker.io/v1/', DOCKERHUB_CREDENTIALS_ID) {
                            docker.image("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}").push()
                        }
                    }
                }
            }
        }
}