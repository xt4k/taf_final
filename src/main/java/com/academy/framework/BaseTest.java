package com.academy.framework;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.fail;

public class BaseTest {
    private static final Logger LOG = LogManager.getLogger(BaseTest.class);
    protected WebDriver driver;
    protected StringBuffer verificationErrors = new StringBuffer();

    @Parameters("browser")
    @BeforeClass(alwaysRun = true)
    public void setUp(@Optional("firefox") String browser) throws Exception {
        initDrivers(browser);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    private void initDrivers(String browser) {
        // Здесь читаю пусть к конфигурационному файлу
        String commonProperties = System.getProperty("common.cfg");
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(commonProperties));
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (browser) {
            case "chrome":
                System.setProperty("webdriver.chrome.driver", properties.getProperty("chrome.driver"));
                // disable notifications
                Map<String, Object> prefs = new HashMap<String, Object>();
                prefs.put("profile.default_content_setting_values.notifications", 2);
//                prefs.put("intl.accept_languages", "en,en_US");
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("prefs", prefs);
                driver = new ChromeDriver(options);
                break;

            case "firefox":
                System.setProperty("webdriver.gecko.driver", properties.getProperty("gecko.driver"));
                driver = new FirefoxDriver();
                break;

            default:
                throw new IllegalArgumentException("Unknown browser " + browser);
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    @BeforeMethod
    public void logTestStart(Method method, Object[] params) {
        LOG.info("Start test {} with parameters {}",
                method.getName(), Arrays.toString(params));
    }

    @AfterMethod(alwaysRun = true)
    public void logTestStop(Method method) {
        LOG.info("Stop test {}", method.getName());
    }
}
