package testplay;

import base.BaseTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PIMFeatureTest  extends BaseTest {
    private static final String LOGIN_URL = "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";

    // ---------------------- POSITIVE TEST CASES ----------------------

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


    @Test
    void testAddEmployeeWithCustomEmployeeId() {
        test.info("Test Started");
        page.navigate(LOGIN_URL);
        loginAsAdmin();

        page.locator("a[href*='pim/viewPimModule']").click();
        page.locator("button:has-text('Add')").click();

        page.locator("input[name='firstName']").fill("Jane");
        page.locator("input[name='lastName']").fill("Smith");

        // Employee ID field is auto-filled; clear and set a custom unique ID
        Locator empIdField = page.getByRole(AriaRole.TEXTBOX).nth(4);
        empIdField.fill("");
        String uniqueEmpId = String.valueOf(System.currentTimeMillis()).substring(8, 13);
        empIdField.fill(uniqueEmpId);
        test.info("Entered custom Employee ID: " + uniqueEmpId);

        page.locator("button[type='submit']").click();

        assertThat(page.locator(".oxd-toast-content")).containsText("Successfully Saved");
        test.pass("Positive Test Passed: Employee added with custom ID");
    }


    @Test
    void testSearchExistingEmployeeByName() {
        test.info("Test Started");
        page.navigate(LOGIN_URL);
        loginAsAdmin();

        page.locator("a[href*='pim/viewPimModule']").click();
        test.info("Navigated to PIM Module");

        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Employee List")).click();
        test.info("Navigated to Employee List page");

        Locator nameField = page.getByRole(AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("Type for hints...")).first();
        nameField.click();
        nameField.fill("Jane");
        test.info("Entered employee name: Jane");

        page.waitForTimeout(1000);
        page.keyboard().press("ArrowDown");
        page.keyboard().press("Enter");
        test.info("Selected suggestion from autocomplete");

        page.locator("button[type='submit']").click();
        test.info("Clicked Search button");

        assertThat(page.locator(".oxd-table-card").first()).isVisible();
        test.pass("Positive Test Passed: Employee found in search results");
    }


    private void loginAsAdmin() {
        page.locator("input[name='username']").fill("Admin");
        page.locator("input[name='password']").fill("admin123");
        page.locator("button:has-text('Login')").click();
        page.waitForSelector(".oxd-topbar-header-breadcrumb");
    }
}
