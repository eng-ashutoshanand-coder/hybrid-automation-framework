package com.automation.tests;

import java.net.MalformedURLException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.automation.core.DriverFactory;
import com.automation.pages.DashboardPage;
import com.automation.pages.LoginPage;
import com.automation.utils.ExcelUtil;
public class MasterSuiteTests extends BaseTest {

    // --- WEB TEST ---
   /* @Test(priority = 1)
    public void runWebTest() throws MalformedURLException {
        if (!Boolean.parseBoolean(prop.getProperty("run_web"))) throw new org.testng.SkipException("Web disabled");
        
        driver = DriverFactory.initializeDriver("web", "", "");
        driver.get(prop.getProperty("base_url"));
        
        HomePage home = new HomePage(driver);
        home.enterSearch("Selenium Framework");
        
        test.info("Searched for Selenium Framework on Desktop Web");
    }

    // --- MOBILE VIEW TEST ---
    @Test(priority = 2)
    public void runMobileViewTest() throws MalformedURLException {
        if (!Boolean.parseBoolean(prop.getProperty("run_mobile_view"))) throw new org.testng.SkipException("Mobile View disabled");

        driver = DriverFactory.initializeDriver("mobile_view", "", "");
        driver.get(prop.getProperty("base_url"));
        
        test.info("Opened Google in iPhone 12 Pro Emulation Mode");
    }

    // --- API TEST ---
    @Test(priority = 3)
    public void runApiTest() {
        // Toggle check handled in BaseTest or here
        if (!Boolean.parseBoolean(prop.getProperty("run_api"))) throw new org.testng.SkipException("API disabled");

        RestAssured.baseURI = prop.getProperty("api_base_url");
        Response response = RestAssured.given().get("/users?page=2");
        
        Assert.assertEquals(response.getStatusCode(), 200);
        test.info("API Response Time: " + response.getTime() + "ms");
        test.pass("API Status Code verified as 200");
    }

    // --- ANDROID TEST ---
    @Test(priority = 4)
    public void runAndroidTest() throws MalformedURLException {
        // Config toggles this off by default so your build doesn't fail without an Emulator
        if (!Boolean.parseBoolean(prop.getProperty("run_android"))) throw new org.testng.SkipException("Android disabled");

        driver = DriverFactory.initializeDriver("android", prop.getProperty("android_app_path"), prop.getProperty("appium_url"));
        test.info("Android App Launched");
    }*/
	
	/*@Test(priority = 1)
    public void verifyOrangeHrmQuickLaunch() throws MalformedURLException {
        // 1. Check Execution Toggle
        if (!Boolean.parseBoolean(prop.getProperty("run_web"))) {
            throw new org.testng.SkipException("Web testing is disabled in config.");
        }

        // 2. Initialize Driver & Navigate
        log.info("Initializing Web Driver...");
        driver = DriverFactory.initializeDriver("web", "", "");
        log.info("Navigating to URL: " + prop.getProperty("base_url"));
        driver.get(prop.getProperty("base_url"));
        test.info("Navigated to OrangeHRM Login Page");

        // 3. Perform Login
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
        test.info("Entered credentials and clicked login");

        // 4. Verify Dashboard Loading
        DashboardPage dashboardPage = new DashboardPage(driver);
        Assert.assertTrue(dashboardPage.isDashboardLoaded(), "Dashboard failed to load!");
        test.pass("Dashboard loaded successfully");

        // 5. Verify Quick Launch Options
        int quickLaunchCount = dashboardPage.getQuickLaunchItemCount();
        test.info("Found " + quickLaunchCount + " Quick Launch options on the dashboard.");
        
        // OrangeHRM usually has 6 default quick launch items
        Assert.assertTrue(quickLaunchCount > 0, "No Quick Launch items found on the dashboard!");
        test.pass("Quick Launch options verified successfully");
        }*/
        
     // 1. Define the DataProvider
        @DataProvider(name = "loginCredentials")
        public Object[][] getLoginData() {
            String excelPath = System.getProperty("user.dir") + "/src/test/resources/testdata/LoginData.xlsx";
            // Fetches data from "Sheet1" of the Excel file
            return ExcelUtil.getTestData(excelPath, "Sheet1"); 
        }

        // 2. Link the DataProvider to the Test and pass the parameters
        @Test(priority = 1, dataProvider = "loginCredentials")
        public void verifyOrangeHrmLoginDDT(String username, String password) throws MalformedURLException {
            if (!Boolean.parseBoolean(prop.getProperty("run_web"))) {
                throw new org.testng.SkipException("Web testing is disabled in config.");
            }

            log.info("Testing Login with Username: " + username + " | Password: " + password);
            
            driver = DriverFactory.initializeDriver("web", "", "");
            driver.get(prop.getProperty("base_url"));
            
            // Use the parameters from Excel instead of config.properties
            LoginPage loginPage = new LoginPage(driver);
            loginPage.login(username, password);

            DashboardPage dashboardPage = new DashboardPage(driver);
            
            // If it's the valid Admin user, expect success
            if (username.equals("Admin") && password.equals("admin123")) {
                Assert.assertTrue(dashboardPage.isDashboardLoaded(), "Dashboard should load for valid user!");
                test.pass("Valid Login Successful for: " + username);
            } else {
                // For invalid users, expect the dashboard NOT to load
                Assert.assertFalse(dashboardPage.isDashboardLoaded(), "Dashboard should NOT load for invalid user!");
                test.pass("Invalid Login correctly rejected for: " + username);
            }
        }
    
	@Test(priority = 2)
    public void verifyOrangeHrmMobileView() throws MalformedURLException {
        // 1. Check Execution Toggle
        if (!Boolean.parseBoolean(prop.getProperty("run_mobile_view"))) {
            throw new org.testng.SkipException("Mobile View testing is disabled in config.");
        }

        // 2. Initialize Driver & Navigate (Using 'mobile_view' this time)
        driver = DriverFactory.initializeDriver("mobile_view", "", "");
        driver.get(prop.getProperty("base_url"));
        test.info("Navigated to OrangeHRM Login Page in Mobile View (iPhone 12 Pro Emulation)");

        // 3. Perform Login
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
        test.info("Entered credentials and clicked login on mobile view");

        // 4. Verify Dashboard Loading
        DashboardPage dashboardPage = new DashboardPage(driver);
        Assert.assertTrue(dashboardPage.isDashboardLoaded(), "Dashboard failed to load in mobile view!");
        test.pass("Dashboard loaded successfully in mobile view");

        // 5. Verify Quick Launch Options
        int quickLaunchCount = dashboardPage.getQuickLaunchItemCount();
        test.info("Found " + quickLaunchCount + " Quick Launch options on the mobile dashboard.");
        
        Assert.assertTrue(quickLaunchCount > 0, "No Quick Launch items found on the mobile dashboard!");
        test.pass("Quick Launch options verified successfully in mobile view");
    }
}