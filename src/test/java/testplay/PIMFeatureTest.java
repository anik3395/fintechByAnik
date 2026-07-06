package testplay;

import base.BaseTest;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PIMFeatureTest  extends BaseTest {
    private static final String LOGIN_URL = "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";

    @Test
    public void testAddEmployeeWithValidData(){
        test.info("Testing Started");
        page.navigate(LOGIN_URL);
        loginAsAdmin();
        test.info("Login Successful");
        page.locator("a[href=\"/web/index.php/pim/viewPimModule\"]").click();
        test.info("Navigated to PIM Module");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add")).click();
        test.info("Clicked Add Employee button");
        page.locator("input[name='firstName']").fill("FirstName");
        page.locator("input[name='middleName']").fill("MiddleName");
        page.locator("input[name='lastName']").fill("LastName");
        test.info("Filled employee basic info");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        test.info("Clicked Save");

        // After save, redirects to Personal Details tab with employee name visible
        assertThat(page.locator(".oxd-toast-content")).containsText("Successfully Saved");
        test.pass("Positive Test Passed: Employee added successfully");


    }


    private void loginAsAdmin() {
        page.locator("input[name='username']").fill("Admin");
        page.locator("input[name='password']").fill("admin123");
        page.locator("button:has-text('Login')").click();
        page.waitForSelector(".oxd-topbar-header-breadcrumb");
    }
}
