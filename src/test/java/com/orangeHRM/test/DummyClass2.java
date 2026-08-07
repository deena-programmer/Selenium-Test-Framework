package com.orangeHRM.test;

import org.testng.annotations.Test;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.utilities.ExtentManager;

public class DummyClass2 extends BaseClass{

	@Test
	public void dummyTest2() {
		String title = getDriver().getTitle();
		ExtentManager.logStep("Verifying the title");
		assert title.equals("OrangeHRM") : "Test Failed - Title is not matching";
		
		System.out.println("Test Passed - Title is matching");
		ExtentManager.logStep("Validation successful");
	}
}
