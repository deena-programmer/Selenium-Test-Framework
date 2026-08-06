package com.orangeHRM.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.pages.HomePage;
import com.orangeHRM.pages.LoginPage;
import com.orangeHRM.utilities.DataProviders;
import com.orangeHRM.utilities.ExtentManager;

public class LoginPageTest extends BaseClass{
	
	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}
	
	@Test(dataProvider="validLoginData", dataProviderClass = DataProviders.class)
	public void verifyValidLoginTest(String username, String password) {
		
		//ExtentManager.startTest("Vaild Login Test");  --This has been implemented in TestListener
		System.out.println("Running testMethod1 on thread: "+ Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to login page entering username and password");
		loginPage.login(username, password);
		ExtentManager.logStep("Verifing Admin Tab is visible or not");
		Assert.assertTrue(homePage.isAdminTabVisible(),"Admin tab should be visible after successfull login");
		ExtentManager.logStep("Validation successful");
		homePage.logout();
		ExtentManager.logStep("Logged out successful");
		staticWait(2);
	}
	
	@Test(dataProvider="inValidLoginData", dataProviderClass = DataProviders.class)
	public void inValidLoginTest(String username, String password) {
		
		//ExtentManager.startTest("In-Vaild Login Test");  --This has been implemented in TestListener
		System.out.println("Running testMethod2 on thread: "+ Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to login page entering username and password");
		loginPage.login(username, password);
		String expectedErrorMessage = "Invalid credentials1";
		Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage), "Test Failed: Invalid error message");
		ExtentManager.logStep("Validation successful");
		ExtentManager.logStep("Logged out successful");
	}
}
