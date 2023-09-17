package Utilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

//import com.Compass.WebUI.MobileEngine;
//
//import io.appium.java_client.android.AndroidDriver;
//import io.appium.java_client.remote.MobileCapabilityType;

public class BrowserConfig extends Global_Utilities {

	@SuppressWarnings("deprecation")
	public static WebDriver getBrowser(String BrowserType) throws MalformedURLException {
		WebDriver BrowserDriver = null;
		String dir = System.getProperty("user.dir");
	
		System.out.println("Browser selected: " + System.getProperty("Browser"));
		System.out.println("Browser Type selected: " + BrowserType);
		System.out.println("Infrastructure selected: " + System.getProperty("Infrastructure"));

		if (System.getProperty("Infrastructure").equalsIgnoreCase("VM")) {
			System.out.println("--Starting tests on VM--");

			if (System.getProperty("Browser").equalsIgnoreCase("Chrome")) 
			{
				
				WebDriverManager.chromedriver().setup();

				if (BrowserType.equalsIgnoreCase("Incognito Mode")) 
				{
					
					ChromeOptions options = new ChromeOptions();
					
					options.addArguments("--remote-allow-origins=*");
					options.addArguments("--incognito");
					options.addArguments("start-maximized");
					options.setCapability(ChromeOptions.CAPABILITY, options);
					options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
					WebDriverManager.chromedriver().setup();
					BrowserDriver = new ChromeDriver(options);
				}

				else if (BrowserType.equalsIgnoreCase("Download Preference")) 
				{
					String downloadFilepath = System.getProperty("user.dir") +FolderLocation.get();
					System.out.println("downloadFilepath is "+downloadFilepath);

					HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
					chromePrefs.put("profile.default_content_settings.popups", 0);
					chromePrefs.put("download.default_directory", downloadFilepath);

					ChromeOptions options1 = new ChromeOptions();
					options1.addArguments("--remote-allow-origins=*");
					options1.setExperimentalOption("prefs", chromePrefs);
					options1.addArguments("--incognito");
					options1.addArguments("start-maximized");
					options1.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
					options1.setCapability(ChromeOptions.CAPABILITY, options1);
					WebDriverManager.chromedriver().setup();
					BrowserDriver = new ChromeDriver(options1);
				}

				else if (BrowserType.equalsIgnoreCase("Normal Mode")) 
				{
			
					ChromeOptions options2 = new ChromeOptions();
					options2.addArguments("--remote-allow-origins=*");
					options2.addArguments("start-maximized");
					options2.setCapability(ChromeOptions.CAPABILITY, options2);
					options2.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
							UnexpectedAlertBehaviour.ACCEPT);
					WebDriverManager.chromedriver().setup();
					BrowserDriver = new ChromeDriver(options2);
				}
				else 
				{
					throw new InvalidParameterException("You must choose one of the defined Browser Types");
				}
			}
			
			else if (System.getProperty("Browser").equalsIgnoreCase("Firefox")) 
			{
				if (BrowserType.equalsIgnoreCase("Incognito Mode")) 
				{
//				System.setProperty("webdriver.gecko.driver",
//						"C:\\NewFramework_ISD-SIT_Project\\Browser\\GeckoDriver\\geckodriver.exe");
				FirefoxOptions firefox = new FirefoxOptions();
				firefox.setCapability("marionette", false);
				firefox.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
				firefox.setCapability("browser.privatebrowsing.autostart", true);
				firefox.addArguments("-private");
				firefox.setCapability("acceptInsecureCerts",true);
				// Options specific for Firefox to avoid prompting a dialog for downloads. They might
			    // change in the future, so please refer to the Firefox documentation for up to date details.
				firefox.addPreference("browser.download.manager.showWhenStarting", false);
				firefox.addPreference("browser.helperApps.neverAsk.saveToDisk",
			        "images/jpeg, application/pdf, application/octet-stream");
				firefox.addPreference("pdfjs.disabled", true);
				// This capability tells the Grid, that this test should be routed ONLY to a node that can
			    // auto manage downloads.
				firefox.setCapability("se:downloadsEnabled", true);
				WebDriverManager.firefoxdriver().setup();
				BrowserDriver = new FirefoxDriver(firefox);

				System.out.println("Browser type not enabled in test framework");
			}
			}

			else if (BrowserType.equalsIgnoreCase("Android Browser")) {
//			DesiredCapabilities mobilecapabilities = new DesiredCapabilities();
//			mobilecapabilities.setCapability(MobileCapabilityType.BROWSER_NAME, BrowserType.CHROME);
//			mobilecapabilities.setCapability(MobileCapabilityType.PLATFORM, Platform.ANDROID);
//			mobilecapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "OnePlus5T");
//			mobilecapabilities.setCapability(MobileCapabilityType.VERSION, MobileEngine.version);
//			mobilecapabilities.setCapability(MobileCapabilityType.UDID, MobileEngine.deviceid);
//			mobilecapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
//			
//		 BrowserDriver = new AndroidDriver(new URL("http://127.0.0.1:"+MobileEngine.port+"/wd/hub"), mobilecapabilities);
//		 return driver;
				
				System.out.println("Browser type not enabled in test framework");
			}

			else {
				throw new InvalidParameterException("You must choose one of the defined Browser");
			}

		}

		if (System.getProperty("Infrastructure").equalsIgnoreCase("Docker")) {
			System.out.println("--Starting tests in docker container--");

			if (System.getProperty("Browser").equalsIgnoreCase("Chrome")) {
		
				ChromeOptions options = new ChromeOptions();

				if (BrowserType.equalsIgnoreCase("Incognito Mode")) {
					options.addArguments("--incognito");
					options.addArguments("start-maximized");
					options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
					options.setCapability(ChromeOptions.CAPABILITY, options);
					options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
							UnexpectedAlertBehaviour.ACCEPT);
				} else if (BrowserType.equalsIgnoreCase("Download Preference")) {
					String downloadFilepath = System.getProperty("user.dir") + "\\Comparison\\" + FolderLocation.get();
					HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
					chromePrefs.put("profile.default_content_settings.popups", 0);
					chromePrefs.put("download.default_directory", downloadFilepath);
					options.setExperimentalOption("prefs", chromePrefs);
					options.addArguments("--incognito");
					options.addArguments("start-maximized");
					options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
					options.setCapability(ChromeOptions.CAPABILITY, options);
				} else if (BrowserType.equalsIgnoreCase("Normal Mode")) {
					options.addArguments("start-maximized");
					options.setCapability(ChromeOptions.CAPABILITY, options);
					options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
							UnexpectedAlertBehaviour.ACCEPT);
				}
				else {
					throw new InvalidParameterException("You must choose one of the defined Browser Types");
				}

				BrowserDriver = new RemoteWebDriver(new URL("http://10.153.199.26:4444/wd/hub"), options);
				
			}
			else {
				throw new InvalidParameterException("You must choose one of the defined Browser");
			}

		}

		return BrowserDriver;
	}

}
