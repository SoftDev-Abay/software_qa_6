// File: src/main/java/utils/DriverFactory.java
package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.URL;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class DriverFactory {
    public static WebDriver initDriver() {
        // Load environment variables from the .env file.
        Dotenv dotenv = Dotenv.load();

        // Determine remote execution flag from a system property or fallback to the .env variable.
        String remoteExecutionStr = System.getProperty("remoteExecution", dotenv.get("REMOTE_EXECUTION", "false"));
        boolean remoteExecution = Boolean.parseBoolean(remoteExecutionStr);

        if (remoteExecution) {
            try {
                String username =  System.getProperty("browserstack.user", dotenv.get("BROWSERSTACK_USER", ""));
                String accessKey = System.getProperty("browserstack.key", dotenv.get("BROWSERSTACK_ACCESS_KEY", ""));
                if (username == null || accessKey == null) {
                    throw new RuntimeException("BrowserStack credentials are not set. Please provide them.");
                }
                String remoteUrl = "https://" + username + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub";

                ChromeOptions options = new ChromeOptions();
                // Standard W3C capabilities:
                options.setCapability("browserName", System.getProperty("browser", "Chrome"));
                options.setCapability("browserVersion", System.getProperty("browser_version", "latest"));

                // BrowserStack specific options under bstack:options:
                Map<String, Object> bstackOptions = new HashMap<>();
                bstackOptions.put("os", System.getProperty("os", "Windows"));
                bstackOptions.put("osVersion", System.getProperty("os_version", "10"));
                bstackOptions.put("sessionName", "EsepTests on BrowserStack");
                // Additional options like build, local testing, etc., can be added here.
                options.setCapability("bstack:options", bstackOptions);

                return new RemoteWebDriver(new URL(remoteUrl), options);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to create remote WebDriver: " + e.getMessage());
            }
        } else {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            return new ChromeDriver(options);
        }
    }
}
