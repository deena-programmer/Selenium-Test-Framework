pipeline {
	agent any
	
	tools{
		maven 'maven-3.9.16'
	}
	
	environment{
		COMPOSE_PATH = "${WORKSPACE}/docker" //Adjust if compose file is elsewhere
		SELENIUM_GRID = "true"
	}
	stages{
		stage('Start Selenium Grid via Docker Compose'){
			steps{
				script{
					echo"Starting Selenium Grid with Docker Compose..."
					bat "docker compose -f ${COMPOSE_PATH}\\docker-compose.yml up -d"
					echo "Waiting for Selenium Grid to be ready..."
					sleep 30 //add a wait if needed
			}
		  }
		}
	
	stages{
		stage('Checkout'){
			steps{
				git branch: 'main', url: 'https://github.com/deena-programmer/Selenium-Test-Framework.git'
			}
		}
		stage('Build'){
			steps{
				bat 'mvn clean install -DseleniumGrid=true'
			}
		}
		stage('Test'){
			steps{
				bat 'mvn clean test -DseleniumGrid=true'
			}
		}
		
		stage('Stop Selenium Grid'){
			steps{
				script{
					echo "Stopping Selenium Grid..."
					bat "docker compose -f ${COMPOSE_PATH}\\docker-compose.yml down"
				}
			}
		}
		stage('Reports'){
			steps{
				publishHTML(target: [
					reportDir: 'src/test/resources/ExtentReport',
					reportFiles: 'ExtentReport.html',
					reportName: 'Extent Spark Report'
				])
			}
		}
	}
	post{
		always{
			archiveArtifacts artifacts: '**src/test/resources/ExtentReport/*.html', fingerprint: true
			junit 'target/surefire-reports/*.xml'
		}
		success{
			emailext(
				to: 'deenak2896@gmail.com',
				subject: "Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
				body: """
				<html>
				<body>
				<p>Hello Team,</p>
				
				<p> The latest Jenkins build has completed. </p>
				
				<p><b>Project Name:</b> ${env.JOB_NAME}</p>
				<p><b>Build Number:</b> ${env.BUILD_NUMBER}</p>
				<p><b>Build Status:</b> <span style="color: green;"> <b>SUCCESS</b></span></p>
				<p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
				
				<p><b>Last Commit:</b></p>
				<p>${env.GIT_COMMIT}</p>
				<p><b>Branch:</b> ${env.GIT_BRANCH}</p>
				
				<p><b>Build log is attached.</b></p>
				
				<p><b>Extent Report: </b><a href="http://localhost:8080/job/OrangeHRM_Build/HTML_
				 20Extent_20Report/"> Click here </a><p>
				 
				 <p>Best regards,</p>
				 <p><b>Deena K</b></p>
				 </body>
				 </html>
				 """,
				 mimeType: 'text/html',
				 attachLog: true
			)
		}
		failure{
			emailext(
				to: 'deenak2896@gmail.com',
				subject: "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
				body: """
				<html>
				<body>
				<p>Hello Team,</p>
				
				<p> The latest Jenkins build has <b style="color: red;">FAILED</b>.</p>
				
				<p><b>Project Name:</b> ${env.JOB_NAME}</p>
				<p><b>Build Number:</b> ${env.BUILD_NUMBER}</p>
				<p><b>Build Status:</b>  <b style="color: red;">FAILED</b></span></p>
				<p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
				<p><b>Please check the logs and take necessary actions.</b></p>
				
				<p><b>Extent Report (if available): </b><a href="http://localhost:8080/job/OrangeHRM_Build/HTML_
				 20Extent_20Report/"> Click here </a><p>
				 
				 <p>Best regards,</p>
				 <p><b>Deena K</b></p>
				 </body>
				 </html>
				 """,
				 mimeType: 'text/html',
				 attachLog: true
				)
		}
	}
}