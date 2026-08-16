package testplay;

import base.BaseTest;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ProfileAbout extends BaseTest {
    private static final String LOGIN_URL = "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";

    @Test
    public void clickOnProfile() {
        test.info("Clicking on profile");
        page.navigate(LOGIN_URL);
        test.info("Navigating the login page");
        loginAsAdmin();
        test.info("Logged in successfully");

        assertThat(page.locator("a[href='/web/index.php/dashboard/index']")).isVisible();
        test.info("Positive Test Passed: User logged in and navigated successfully to dashboard");

// --- Go to About ---
        page.locator(".oxd-userdropdown-tab").click();
        test.info("Clicking on profile button");
        page.getByRole(AriaRole.MENUITEM,
                new Page.GetByRoleOptions().setName("About")).click();
        test.info("Navigate to about profile page");

// --- Go back to dashboard before reopening dropdown ---
        page.navigate(LOGIN_URL.replace("/auth/login", "/dashboard/index"));


// --- Go to Support ---
        page.locator(".oxd-userdropdown-tab").click();
        test.info("Clicking on support button");
        page.getByRole(AriaRole.MENUITEM,
                new Page.GetByRoleOptions().setName("Support")).click();
        test.info("Navigate to support page");
    }

    private void loginAsAdmin() {
        page.locator("input[name='username']").fill("Admin");
        page.locator("input[name='password']").fill("admin123");
        page.locator("button:has-text('Login')").click();
        page.waitForSelector(".oxd-topbar-header-breadcrumb");
    }
}