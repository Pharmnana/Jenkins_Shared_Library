def call(String stageID){

  if("${stageID}" == "Test"){
    echo "Testing with SonarQube"
    sh "mvn sonar:sonar"
    echo "Testing done"
  }
  
    else if("${stageID}" == "Approval"){
        echo 'Seeking approval'
        echo 'Sending email to team lead'
        emailext body: 'Hello Team Lead, I have just concluded the jenkins pipeline script and awaiting your authorization to deploy the project titled ${JOB_NAME} located in ${JOB_URL}' ,
                  subject: 'Approval request to deploy.' ,
                  to: 'study@gmail.com, devopsnancy@gmail.com'
        timeout(time: 2, unit: "DAYS"){
        input message: 'Approve to deploy to production'
        }
      }

    else if ("${stageID}" == "Staging"){
          echo 'Deploying to production tomcat_1'
            deploy adapters: [tomcat9(credentialsId: 'tomcat_cred', path: '', url: ${env.STAGING_URL})], contextPath: null, war: 'target/*war'
          }
    
    else if ("${stageID}" == "Production"){
            echo 'Deploying to production tomcat_2'
            deploy adapters: [tomcat9(credentialsId: 'tomcat_cred', path: '', url: ${PRODUCTION_URL})], contextPath: null, war: 'target/*war'
          }
    
    else if ("${stageID}" == "Success"){
            echo "Deploying to ${env.DEPLOY_ENV} tomcat successful" 
            emailext  body: 'Hello Team Lead, the project titled ${JOB_NAME} located in ${JOB_URL} has been successfully deployed.' ,
                    subject: 'Deploy Success' ,
                    to: ${TEAM_LEAD_EMAILS}
         }
    else if ("${stageID}" == "Failure"){
      echo "Deploying to ${env.DEPLOY_ENV} failed"
      emailext body: 'Hello Team Lead, unfortunately, the project titled ${JOB_NAME} located in ${JOB_URL} failed to deploy. Could you kindly give the team a few days to troubleshoot?' ,
              subject: 'Deploy failure' ,
              to: ${TEAM_LEAD_EMAILS}
    }
  }
      
