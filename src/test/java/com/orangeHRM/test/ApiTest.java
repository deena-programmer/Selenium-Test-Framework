package com.orangeHRM.test;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangeHRM.utilities.ApiUtility;
import com.orangeHRM.utilities.ExtentManager;
import com.orangeHRM.utilities.RetryAnalyzer;

import io.restassured.response.Response;

public class ApiTest {

	@Test(retryAnalyzer = RetryAnalyzer.class)
	public void verifyGetUserAPI() {
		
		SoftAssert softAssert = new SoftAssert();

		// Step1: Define API Endpoint
		String endPoint = "https://jsonplaceholder.typicode.com/users/1";
		ExtentManager.logStep("API Endpoint: " +endPoint);

		// Step2: Send Get Request
		ExtentManager.logStep("Sending GET Request to the API");
		Response response = ApiUtility.sendGetRequest(endPoint);

		// Step3: Validate status code
		ExtentManager.logStep("Validating API Response status code");
		boolean isStatusCodeValid = ApiUtility.validateStatusCode(response, 200);
		softAssert.assertTrue(isStatusCodeValid, "Status code is not as Expected");
		if (isStatusCodeValid) {
			ExtentManager.logStepValidationForAPI("Status code Validation Passed!");
		} else {
			ExtentManager.logFailureAPI("Status code Validation Failed!");
		}

		// Step4: Validate user name
		ExtentManager.logStep("Validating response body for username");
		String userName = ApiUtility.getJsonValue(response, "username");
		boolean isUserNameValid = "Bret".equals(userName);
		softAssert.assertTrue(isUserNameValid, "Username is not valid");
		if (isUserNameValid) {
			ExtentManager.logStepValidationForAPI("Username Validation Passed!");
		} else {
			ExtentManager.logFailureAPI("Username Validation Failed!");
		}

		// Step5: Validate email
		ExtentManager.logStep("Validating response body for email");
		String userEmail = ApiUtility.getJsonValue(response, "email");
		boolean isEmailValid = "Sincere@april.biz".equals(userEmail);
		softAssert.assertTrue(isEmailValid, "Email is not valid");
		if (isEmailValid) {
			ExtentManager.logStepValidationForAPI("Email Validation Passed!");
		} else {
			ExtentManager.logFailureAPI("Email Validation Failed!");
		}
		
		softAssert.assertAll();
	}
}
