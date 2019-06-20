pipeline {
  agent any
  parameters {
    string(description: 'GCP Project ID', name: 'googleProjectId', defaultValue: '')
    string(description: 'Project image name', name: 'imageName', defaultValue: 'telemetry-frontend')
    string(description: 'Project image tag', name: 'imageVersion', defaultValue: 'v1')
  }
    
  stages {
    stage('init') {
      steps{
        checkout scm
      }
    }
    
    stage('build') {
      steps{
        sh "mvn -Dmaven.test.skip=true package && docker build -t gcr.io/${params.googleProjectId}/${params.imageName}:${params.imageVersion} ."
      }
    }
        
    stage('push') {
      steps{
        sh "docker push gcr.io/${params.googleProjectId}/${params.imageName}:${params.imageVersion}"
      }
    }

    stage('deploy'){
      steps{
        sh "helm template kubernetes/ --set iac.google.project=${params.googleProjectId} | kubectl apply -f -"
      }
    }
  }
}
