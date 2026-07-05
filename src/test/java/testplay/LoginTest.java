package testplay;

import base.BaseTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.Test;
import page.HomePage;
import page.LoginPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginTest extends BaseTest {

    private static final String LOGIN_URL = "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";


    @Test
    void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(page);
        HomePage homePage = new HomePage(page);

        test.info("Test Started");
        page.navigate(LOGIN_URL);
        test.info("Login Page Loaded");

        loginPage.login("Admin", "admin123");
        test.info("Login Successful");

//        homePage.clickTimeLink();
//        test.info("Time Link Clicked");

        assertThat(page.locator("a[href='/web/index.php/dashboard/index']")).isVisible();
        test.pass("Positive Test Passed: User logged in and navigated successfully");
    }

    // ---------------------- NEGATIVE TESTS ----------------------

    @Test
    void testLoginWithInvalidPassword() {
        LoginPage loginPage = new LoginPage(page);

        test.info("Test Started");
        page.navigate(LOGIN_URL);
        test.info("Login Page Loaded");

        loginPage.login("Admin", "wrongPassword");
        test.info("Attempted login with invalid password");

        assertThat(page.locator(".oxd-alert-content-text"))
                .hasText("Invalid credentials");
        test.pass("Negative Test Passed: Invalid password correctly rejected");
    }

    @Test
    void testLoginWithInvalidUsername() {
        LoginPage loginPage = new LoginPage(page);

        test.info("Test Started");
        page.navigate(LOGIN_URL);
        test.info("Login Page Loaded");

        loginPage.login("WrongAdmin", "admin123");
        test.info("Attempted login with invalid username");

        assertThat(page.locator(".oxd-alert-content-text"))
                .hasText("Invalid credentials");
        test.pass("Negative Test Passed: Invalid username correctly rejected");
    }

    @Test
    void testLoginWithInvalidUsernameAndPassword() {
        LoginPage loginPage = new LoginPage(page);

        test.info("Test Started");
        page.navigate(LOGIN_URL);
        test.info("Login Page Loaded");

        loginPage.login("WrongAdmin", "wrongPassword");
        test.info("Attempted login with invalid username and password");

        assertThat(page.locator(".oxd-alert-content-text"))
                .hasText("Invalid credentials");
        test.pass("Negative Test Passed: Invalid credentials correctly rejected");
    }

    @Test
    void testLoginWithEmptyUsername() {
        LoginPage loginPage = new LoginPage(page);

        test.info("Test Started");
        page.navigate(LOGIN_URL);
        test.info("Login Page Loaded");

        loginPage.login("", "admin123");
        test.info("Attempted login with empty username");

        assertThat(page.locator(".oxd-input-group__message"))
                .hasText("Required");
        test.pass("Negative Test Passed: Empty username validation shown");
    }

    @Test
    void testLoginWithEmptyPassword() {
        LoginPage loginPage = new LoginPage(page);

        test.info("Test Started");
        page.navigate(LOGIN_URL);
        test.info("Login Page Loaded");

        loginPage.login("Admin", "");
        test.info("Attempted login with empty password");

        assertThat(page.locator(".oxd-input-group__message"))
                .hasText("Required");
        test.pass("Negative Test Passed: Empty password validation shown");
    }

    @Test
    void testLoginWithBothFieldsEmpty() {
        LoginPage loginPage = new LoginPage(page);

        test.info("Test Started");
        page.navigate(LOGIN_URL);
        test.info("Login Page Loaded");

        loginPage.login("", "");
        test.info("Attempted login with both fields empty");

        assertThat(page.locator(".oxd-input-group__message").first()).isVisible();
        test.pass("Negative Test Passed: Required field validation shown for both fields");
    }

    @Test
    void testLoginWithSqlInjection() {
        LoginPage loginPage = new LoginPage(page);

        test.info("Test Started");
        page.navigate(LOGIN_URL);
        test.info("Login Page Loaded");

        loginPage.login("' OR '1'='1", "' OR '1'='1");
        test.info("Attempted login with SQL injection payload");

        assertThat(page.locator(".oxd-alert-content-text"))
                .hasText("Invalid credentials");
        assertThat(page.locator(".oxd-topbar-header-breadcrumb")).not().isVisible();
        test.pass("Negative Test Passed: SQL injection did not bypass login");
    }

    @Test
    void testLoginWithCaseSensitivePasswordNegative() {
        LoginPage loginPage = new LoginPage(page);

        test.info("Test Started");
        page.navigate(LOGIN_URL);
        test.info("Login Page Loaded");

        loginPage.login("Admin", "ADMIN123"); // wrong case password
        test.info("Attempted login with case-mismatched password");

        assertThat(page.locator(".oxd-alert-content-text"))
                .hasText("Invalid credentials");
        test.pass("Negative Test Passed: Password case sensitivity enforced");
    }

    @Test
    void testLoginWithWhitespaceCredentials() {
        LoginPage loginPage = new LoginPage(page);

        test.info("Test Started");
        page.navigate(LOGIN_URL);
        test.info("Login Page Loaded");

        loginPage.login("   ", "   ");
        test.info("Attempted login with whitespace-only credentials");

        assertThat(page.locator(".oxd-input-group__message").first()).isVisible();
        test.pass("Negative Test Passed: Whitespace-only input rejected");
    }

    @Test
    void testLoginWithSpecialCharactersInUsername() {
        LoginPage loginPage = new LoginPage(page);

        test.info("Test Started");
        page.navigate(LOGIN_URL);
        test.info("Login Page Loaded");

        loginPage.login("Admin!@#$%", "admin123");
        test.info("Attempted login with special characters in username");

        assertThat(page.locator(".oxd-alert-content-text"))
                .hasText("Invalid credentials");
        test.pass("Negative Test Passed: Special character username rejected");
    }

    @Test
    void testLoginWithExcessivelyLongUsername() {
        LoginPage loginPage = new LoginPage(page);

        String longUsername = "A".repeat(300);

        test.info("Test Started");
        page.navigate(LOGIN_URL);
        test.info("Login Page Loaded");

        loginPage.login(longUsername, "admin123");
        test.info("Attempted login with excessively long username");

        assertThat(page.locator(".oxd-alert-content-text"))
                .hasText("Invalid credentials");
        test.pass("Negative Test Passed: Long username input handled gracefully");
    }

//    @Test
//    public void test(){
//        LoginPage loginPage = new LoginPage(page);
//        HomePage homePage = new HomePage(page);
//
//        test.info("Test Started");
//        page.navigate("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
//        test.info("Login Page");
//        loginPage.login("Admin", "admin123");
//        test.info("Login Successful");
//        homePage.clickTimeLink();
//        test.info("Time Link Clicked");
//
//
////        page.navigate("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
////        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username")).click();//Find the locator for the username textbox and click on it
////        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username")).fill("Admin"); //Fill the locator with the username "Admin"
////        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password")).click();
////        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password")).fill("admin123");
////        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
////        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Time")).click();
////        page.locator("form").getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("View")).dblclick();
////        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Leave")).click();
//    }

//    @Test
//    public void test1(){
//        LoginPage loginPage = new LoginPage(page);
//        HomePage homePage = new HomePage(page);
//
//        test.skip("Skipped Test");
////        throw new RuntimeException("Test skipped");
//    }

//    @Test
//    public void test2(){
//        LoginPage loginPage = new LoginPage(page);
//        HomePage homePage = new HomePage(page);
//
//        test.info("Test Started");
//        page.navigate("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
//        test.info("Login Page");
//        loginPage.login("Admin", "admin123");
//        test.info("Login Successful");
////        homePage.clickTimeLink();
////        test.info("Time Link Clicked");
//
//
//
//
//    }
}
