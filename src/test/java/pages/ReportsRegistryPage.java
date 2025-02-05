// File: src/test/java/pages/ReportsRegistryPage.java
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ReportsRegistryPage extends BasePage {

    private final By SEARCH_INPUT  = By.xpath("/html/body/div[1]/div/div[1]/div[3]/div/div[2]/input[1]");
    private final By RESULTS_TABLE = By.xpath("/html/body/div[1]/div/div[1]/div[3]/div/div[3]/div[1]/table");
    private final By TABLE_ROWS    = By.tagName("tr");
    private final By CREATE_REPORT_BUTTON = By.xpath("//*[@id=\"root\"]/div/div[1]/div[3]/div/button");

    private final String COLUMN_XPATH_TEMPLATE = "/html/body/div[1]/div/div[1]/div[3]/div/div[2]/input[%d]";

    public ReportsRegistryPage(WebDriver driver) {
        super(driver);
    }

    public void openReportsRegistry() {
        openUrl("https://esep.govtec.kz/admin/reports/registry");
    }

    public List<List<String>> searchInRegistry(String searchValue, int columnIndex) {
        openReportsRegistry();

        // Enter the search value and send the RETURN key to trigger the search
        enterText(SEARCH_INPUT, searchValue);
        driver.findElement(SEARCH_INPUT).sendKeys(Keys.RETURN);

        // Locate and click the appropriate checkbox using the column index.
        String columnXpath = String.format(COLUMN_XPATH_TEMPLATE, columnIndex);
        By checkboxLocator = By.xpath(columnXpath);
        WebElement checkbox = waitForElementVisible(checkboxLocator);
        if (!checkbox.isSelected()) {
            checkbox.click();
        }

        // Wait for the results table to be visible.
        waitForElementVisible(RESULTS_TABLE);

        // Wait until there is more than one row in the table.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(drv -> drv.findElements(TABLE_ROWS).size() > 1);

        // Re-locate table rows to ensure we have fresh elements.
        List<WebElement> tableRows = driver.findElements(TABLE_ROWS);
        List<List<String>> rowTexts = new ArrayList<>();

        // Iterate over each row (starting at index 1 to skip header) and capture the text immediately.
        for (int i = 1; i < tableRows.size(); i++) {
            List<WebElement> cells = tableRows.get(i).findElements(By.tagName("td"));
            List<String> rowData = new ArrayList<>();
            for (WebElement cell : cells) {
                rowData.add(cell.getText().trim());
            }
            rowTexts.add(rowData);
        }
        return rowTexts;
    }

    public String createReportBase(String reportBaseId, String reportBaseNameRus, String reportBaseNameKaz) {
        clickElement(CREATE_REPORT_BUTTON);

        By REPORT_BASE_ID_INPUT       = By.xpath("//*[@id=\"modal-root\"]/div/div/div[2]/div[1]/div[2]/input");
        By REPORT_BASE_NAME_RUS_INPUT = By.xpath("//*[@id=\"modal-root\"]/div/div/div[2]/div[2]/div[2]/input");
        By REPORT_BASE_NAME_KAZ_INPUT = By.xpath("//*[@id=\"modal-root\"]/div/div/div[2]/div[3]/div[2]/input");
        By SUBMIT_BUTTON              = By.xpath("//*[@id=\"modal-root\"]/div/div/div[3]/button[1]");

        enterText(REPORT_BASE_ID_INPUT, reportBaseId);
        enterText(REPORT_BASE_NAME_RUS_INPUT, reportBaseNameRus);
        enterText(REPORT_BASE_NAME_KAZ_INPUT, reportBaseNameKaz);

        clickElement(SUBMIT_BUTTON);
        return getCurrentUrl();
    }
}
