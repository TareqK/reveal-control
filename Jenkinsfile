node('master'){
 def scmVars = checkout scm
 def imageTag = env.TAG_NAME
 if(imageTag != null){
   imageTag = "latest"
 }
 withDockerRegistry(credentialsId: 'dc94d276-bbcb-490a-8586-8322dbf729dd') {
  withMaven(jdk: 'JDK-11', maven: 'default-maven') {
   sh "make clean && make publish DOCKER_TAG=${imageTag}"
  }
 }
 if(imageTag == "latest"){
   build wait: false, job: 'deploy-reveal-control-demo'
 }
}
