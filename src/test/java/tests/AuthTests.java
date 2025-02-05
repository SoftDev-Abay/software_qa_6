// File: src/test/java/tests/AuthTests.java
package tests;

import com.aventstack.extentreports.ExtentTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.ExcelUtils;  // Import the new ExcelUtils

public class AuthTests extends BaseTest {

    // DataProvider now reads test data from testdata.xlsx, sheet "loginData"
    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return ExcelUtils.getExcelData("testdata.xlsx", "loginData");
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password, String expectedUrl) {
        extentTest = extentReports.createTest("testLogin: " + username);
        logger.info("Starting login test for user: " + username);

        try {
            LoginPage loginPage = new LoginPage(driver);
            loginPage.login(username, password, expectedUrl);

            String currentUrl = driver.getCurrentUrl();
            extentTest.info("Current URL after login: " + currentUrl);

            Assert.assertTrue(
                    currentUrl.contains(expectedUrl),
                    "Expected URL to contain: " + expectedUrl + " but got: " + currentUrl
            );
            extentTest.pass("Login test passed for user: " + username);

        } catch (Exception e) {
            extentTest.fail("Login test failed for user: " + username + "\n" + e.getMessage());
            logger.error("Exception during login test: ", e);
            Assert.fail(e.getMessage());
        }
    }
}
