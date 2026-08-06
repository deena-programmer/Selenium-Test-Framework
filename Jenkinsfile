pipeline {
	agent any
	
	tools{
		maven 'maven-3.9.16'
	}
	stages{
		stage('Checkout'){
			steps{
				git branch: 'main', url: 'https://github.com/deena-programmer/Selenium-Test-Framework.git'
			}
		}
		stage('Build'){
			steps{
				bat 'mvn clean install'
			}
		}
		stage('Test'){
			steps{
				bat 'mvn test'
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
		Success{
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
	}
}