package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.internal.TestResult;
import utils.ExtentManager;

import java.lang.reflect.Method;

public class BaseTest {

    protected Playwright playwright;
    protected Browser browser;
    protected Page page;
    protected ExtentReports extent;
    protected ExtentTest test;

    @BeforeMethod
    public void SetUp(Method method){

        //Reporting
        extent= ExtentManager.getInstance();
        test= extent.createTest(method.getName());

        //PlayWright setup
         playwright = Playwright.create();
         browser= playwright.chromium().
                launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(1000));

         page = browser.newPage();
        page.setDefaultTimeout(30000);           // 30s for actions like click/fill
        page.setDefaultNavigationTimeout(30000); // 30s specifically for navigate()
    }

    @AfterMethod
    public void tearDown( ITestResult result){

        //Reporting logic
        if(result.getStatus() == ITestResult.FAILURE){
            test.fail("Test Failed: " + result.getThrowable());
        }else if(result.getStatus() == ITestResult.SUCCESS){
            test.info("Test Passed: " + result.getTestName());
        }else {
            test.skip("Test Skipped: " + result.getThrowable());
        }
        extent.flush();

        //Browser Cleanup
        if(browser != null){
            browser.close();
        }

        if(playwright != null){
            playwright.close();
        }
    }


}
