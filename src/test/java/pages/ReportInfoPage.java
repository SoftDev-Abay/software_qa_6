
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.DropdownUtils;

public class ReportInfoPage extends BasePage {

    private final String DROPDOWN_1_XPATH = "/html/body/div[1]/div/div[1]/div[3]/div/div/div[2]/div/div[4]/div[2]/div";
    private final String DROPDOWN_2_XPATH = "//*[@id=\"root\"]/div/div[1]/div[3]/div/div/div[2]/div/div[5]/div[2]/div";
    private final String DROPDOWN_3_XPATH = "//*[@id=\"root\"]/div/div[1]/div[3]/div/div/div[2]/div/div[6]/div[2]/div";

    private final String PARENT_XPATH = "/html/body/div[3]/div";

    private final By TEMPLATE_ID_INPUT = By.xpath(
            "//*[@id='root']/div/div[1]/div[3]/div/div/div[2]/div/div[1]/div[2]/input"
    );

    private final By SUBMISSION_BUTTON = By.xpath(
            "//*[@id='root']/div/div[1]/div[3]/div/div/div[2]/div/button"
    );

    public ReportInfoPage(WebDriver driver) {
        super(driver);
    }


    public void openReportInfo(String reportBaseId) {
        String url = "https://esep.govtec.kz/admin/reports/info/" + reportBaseId;
        openUrl(url);
    }


    public void fillBaseInfo(String templateId) {
        DropdownUtils.selectDropdownOption(driver, DROPDOWN_1_XPATH, PARENT_XPATH,
                "Организации государственной и квазигосударственной сфер деятельности");
        DropdownUtils.selectDropdownOption(driver, DROPDOWN_2_XPATH, PARENT_XPATH, "Ежегодно");
        DropdownUtils.selectDropdownOption(driver, DROPDOWN_3_XPATH, PARENT_XPATH, "Да");

        enterText(TEMPLATE_ID_INPUT, templateId);

        clickElement(SUBMISSION_BUTTON);
    }
}
