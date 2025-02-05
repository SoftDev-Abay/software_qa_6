// File: src/test/java/tests/SearchTests.java
package tests;

import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ReportsRegistryPage;
import utils.ExcelUtils;
import java.util.List;

public class SearchTests extends BaseTest {

    @DataProvider(name = "searchData")
    public Object[][] searchData() {
        Object[][] rawData = ExcelUtils.getExcelData("testdata.xlsx", "searchData");
        // Each row in rawData contains three columns:
        //   0: searchValue (String)
        //   1: columnIndex (String or numeric, will be converted to int)
        //   2: expectedTitles (String with delimited expected rows)
        Object[][] convertedData = new Object[rawData.length][3];
        for (int i = 0; i < rawData.length; i++) {
            String searchValue = (String) rawData[i][0];
            int columnIndex = Integer.parseInt(rawData[i][1].toString());
            String expectedTitlesString = (String) rawData[i][2];
            String[][] expectedTitles = parseExpectedTitles(expectedTitlesString);
            convertedData[i][0] = searchValue;
            convertedData[i][1] = columnIndex;
            convertedData[i][2] = expectedTitles;
        }
        return convertedData;
    }

    private String[][] parseExpectedTitles(String data) {
        String[] rows = data.split(";");
        String[][] result = new String[rows.length][2];
        for (int i = 0; i < rows.length; i++) {
            String[] parts = rows[i].split(",", 2);
            if (parts.length < 2) {
                throw new RuntimeException("Expected two parts per row in expectedTitles, got: " + rows[i]);
            }
            result[i][0] = parts[0].trim();
            result[i][1] = parts[1].trim();
        }
        return result;
    }

    @Test(dataProvider = "searchData")
    public void testSearchFunctionality(String searchValue, int columnIndex, String[][] expectedTitles) {
        extentTest = extentReports.createTest("testSearchFunctionality: " + searchValue);

        try {
            LoginPage loginPage = new LoginPage(driver);
            loginPage.login("edugov_admin", "CuShF33o", "https://esep.govtec.kz/admin");
            extentTest.info("Logged in as edugov_admin");

            ReportsRegistryPage registryPage = new ReportsRegistryPage(driver);
            List<List<String>> rowTexts = registryPage.searchInRegistry(searchValue, columnIndex);
            extentTest.info("Search completed for: " + searchValue);

            Thread.sleep(2000);

            for (int i = 0; i < rowTexts.size(); i++) {
                logger.info("Row " + i + " => " + rowTexts.get(i));
            }

            for (String[] expected : expectedTitles) {
                boolean found = false;
                for (List<String> row : rowTexts) {
                    if (row.size() >= 2 && row.get(0).equals(expected[0]) && row.get(1).equals(expected[1])) {
                        found = true;
                        break;
                    }
                }
                Assert.assertTrue(found,
                        "Expected row " + expected[0] + ", " + expected[1] + " not found in results!");
            }

            extentTest.pass("Search test passed with value: " + searchValue);

        } catch (Exception e) {
            extentTest.fail("Search test failed for: " + searchValue + " - " + e.getMessage());
            logger.error("Exception during search test: ", e);
            Assert.fail(e.getMessage());
        }
    }
}
