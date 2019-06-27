pipeline {
  
  agent any
  
  parameters {
    string(description: 'GCP Project ID', name: 'googleProjectId', defaultValue: env.GOOGLE_PROJECT_ID )
    string(description: 'Project image name', name: 'imageName', defaultValue: 'telemetry-frontend')
    string(description: 'Project image tag', name: 'imageTag', defaultValue: '' )
  }
    
  environment {
    IMG_TAG = "${params.imageTag.trim() == '' ? env.BUILD_NUMBER : params.imageTag }"
  }

  stages {
    stage('init') {
      steps{
        checkout scm
      }
    }

    stage('build') {
      steps{
        sh "mvn -Dmaven.test.skip=true package && docker build -t gcr.io/${params.googleProjectId}/${params.imageName}:${env.IMG_TAG} ."
      }
    }

    stage('push') {
      steps{
        sh "docker push gcr.io/${params.googleProjectId}/${params.imageName}:${env.IMG_TAG}"
      }
    }

    stage('deploy'){
      steps{
        sh "INGRESS_HOST=\$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.status.loadBalancer.ingress[0].ip}') && helm template kubernetes/ --set google.project.id=${params.googleProjectId} --set project.imageName=${params.imageName} --set project.imageTag=${env.IMG_TAG} --set google.project.ingressgatewayIp=\$INGRESS_HOST | kubectl apply -f - "
      }
    }
  }
}
