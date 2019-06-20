pipeline {
  agent any
  parameters {
    string(description: 'GCP Project ID', name: 'googleProjectId', defaultValue: '')
    string(description: 'Project image name', name: 'imageName', defaultValue: 'telemetry-frontend')
    string(description: 'Project image tag', name: 'imageTag', defaultValue: 'v1')
  }
    
  stages {
    stage('init') {
      steps{
        checkout scm
      }
    }
    
    stage('build') {
      steps{
        sh "mvn -Dmaven.test.skip=true package && docker build -t gcr.io/${params.googleProjectId}/${params.imageName}:${params.imageTag} ."
      }
    }
        
    stage('push') {
      steps{
        sh "docker push gcr.io/${params.googleProjectId}/${params.imageName}:${params.imageTag}"
      }
    }

    stage('deploy'){
      steps{
        sh "INGRESS_HOST=\$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.status.loadBalancer.ingress[0].ip}') && helm template kubernetes/ --set google.project.id=${params.googleProjectId} --set project.imageName=${params.imageName} --set project.imageTag=${params.imageTag} --set google.project.ingressgatewayIp=\$INGRESS_HOST | kubectl apply -f -"
      }
    }
  }
}
