package com.orangeHRM.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.orangeHRM.actiondriver.ActionDriver;
import com.orangeHRM.utilities.ExtentManager;
import com.orangeHRM.utilities.LoggerManager;

public class BaseClass {

	protected static Properties prop;
	// protected static WebDriver driver;
	// private static ActionDriver actionDriver;

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);

	// Getter Method for SoftAssert
	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}

	@BeforeSuite
	public void loadConfig() throws IOException {
		// Load the configuration file
		prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "/src/main/resources/config.properties");
		prop.load(fis);
		logger.info("properties file loaded");

		// Start the Extent report
		// ExtentManager.getReporter(); --This has been implemented in TestListener
	}

	@BeforeMethod
	@Parameters("browser")
	public synchronized void setup(String browser) throws IOException {
		System.out.println("Setting up WebDriver for:" + this.getClass().getSimpleName());
		launchBrowser(browser);
		configureBrowser();
		staticWait(2);
		// sample logger message
		logger.info("WebDriver instance and browser maximized");
		logger.trace("This is a Trace message");
		logger.error("This is a Error message");
		logger.debug("This is a debug message");
		logger.fatal("This is a fatal message");
		logger.warn("This is a warn message");

		// Initialize the actionDriver only once
		/*
		 * if (actionDriver == null) { actionDriver = new ActionDriver(driver);
		 * logger.info("ActionDriver instance is created." +
		 * Thread.currentThread().getId()); }
		 */

		// Initialize the actionDriver for the currend THread
		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver initialized for thread: " + Thread.currentThread().getId());

	}

	// Initialize the WebDiver based on browser defined in config.properties file
	private synchronized void launchBrowser(String browser) {

		// String browser = prop.getProperty("browser");
		boolean seleniumGrid = Boolean.parseBoolean(prop.getProperty("seleniumGrid"));
		String gridURL = prop.getProperty("gridURL");

		if (seleniumGrid) {
			try {
				if (browser.equalsIgnoreCase("chrome")) {
					ChromeOptions options = new ChromeOptions();
					options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080");
					driver.set(new RemoteWebDriver(new URL(gridURL), options));
					
				} else if (browser.equalsIgnoreCase("firefox")) {
					FirefoxOptions options = new FirefoxOptions();
					options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080");
					driver.set(new RemoteWebDriver(new URL(gridURL), options));
					
				} else if (browser.equalsIgnoreCase("edge")) {
					EdgeOptions options = new EdgeOptions();
					options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080");
					driver.set(new RemoteWebDriver(new URL(gridURL), options));
					
				} else {
					throw new IllegalArgumentException("Browser Not Supported: " + browser);
				}
				
				logger.info("RemoteWebDriver instance created for Grid in headless mode");
			} catch (MalformedURLException e) {
				throw new RuntimeException("Invaild Grid URL", e);
			}
		} else {

			if (browser.equalsIgnoreCase("chrome")) {

				// create ChromeOptions
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--headless"); // Run Chrome in headless mode
				options.addArguments("--disable-gpu"); // Disable GPU for headless mode
				options.addArguments("--disable-notifications"); // Disable browser notification
				options.addArguments("--no-sandbox"); // Required for some CI enviroments like
				options.addArguments("--disble-dev-shm-usage"); // Resolve issues in resource
				// driver = new ChromeDriver();
				driver.set(new ChromeDriver()); // New changes as per threadLocal
				logger.info("ChromeDriver Instance is created");
				ExtentManager.registerDriver(getDriver());
			} else if (browser.equalsIgnoreCase("firefox")) {
				FirefoxOptions options = new FirefoxOptions();
				options.addArguments("--headless"); // Run Chrome in headless mode
				options.addArguments("--disable-gpu"); // Disable GPU for headless mode
				options.addArguments("--disable-notifications"); // Disable browser notification
				options.addArguments("--no-sandbox"); // Required for some CI enviroments like
				options.addArguments("--disble-dev-shm-usage"); // Resolve issues in resource

				// driver = new FirefoxDriver();
				driver.set(new FirefoxDriver());// New changes as per threadLocal
				logger.info("FirefoxDriver Instance is created");
				ExtentManager.registerDriver(getDriver());
			} else if (browser.equalsIgnoreCase("edge")) {
				EdgeOptions options = new EdgeOptions();
				options.addArguments("--headless"); // Run Chrome in headless mode
				options.addArguments("--disable-gpu"); // Disable GPU for headless mode
				options.addArguments("--disable-notifications"); // Disable browser notification
				options.addArguments("--no-sandbox"); // Required for some CI enviroments like
				options.addArguments("--disble-dev-shm-usage"); // Resolve issues in resource
				// driver = new EdgeDriver();
				driver.set(new EdgeDriver());// New changes as per threadLocal
				logger.info("EdgeDriver Instance is created");
				ExtentManager.registerDriver(getDriver());
			} else {
				throw new IllegalArgumentException("Browser Not Supported: " + browser);
			}
		}
	}

	// Configure browser setting such as implicit wait, maximize the browser and
	// navigate to the URL
	private void configureBrowser() {
		// Implicit Wait
		int implicitlyWait = Integer.parseInt(prop.getProperty("implicitwait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitlyWait));

		// maximize the browser
		getDriver().manage().window().maximize();

		// Navigate to URL
		try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			System.out.println("Failed to navigate to the URL: " + e.getMessage());
		}
	}

	@AfterMethod
	public synchronized void tearDown() {
		if (getDriver() != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				System.out.println("Unable to quit the dirver:" + e.getMessage());
			}
		}
		logger.info("WebDriver instance is closed.");
		driver.remove();
		actionDriver.remove();
		// driver = null;
		// actionDriver = null;
		// ExtentManager.endTest(); --This has been implemented in TestListener
	}

	// Getter method for prop
	public static Properties getProp() {
		return prop;
	}

	/*
	 * //Driver getter method public WebDriver getDriver() { return driver; }
	 */
	// Getter Method for WebDriver
	public static WebDriver getDriver() {
		if (driver.get() == null) {
			System.out.println("WebDriver is not initialized");
			throw new IllegalStateException("WebDriver is not initialized");
		}

		return driver.get();
	}

	// Getter Method for ActionDriver
	public static ActionDriver getActionDriver() {
		if (actionDriver.get() == null) {
			System.out.println("ActionDriver is not initialized");
			throw new IllegalStateException("ActionDriver is not initialized");
		}

		return actionDriver.get();
	}

	// Driver setter method
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver = driver;
	}

	// Static wait for pause
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}
}
