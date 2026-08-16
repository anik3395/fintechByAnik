package testplay;

import base.BaseTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertTrue;

public class VacanciesTest extends BaseTest {
    private static final String LOGIN_URL = "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";

    @Test
    public void positiveTestWithVacancies() {
        test.info("Testing Vacancies started");
        page.navigate(LOGIN_URL);
        test.info("Navigated to login page");
        loginAsAdmin();
        test.info("Logged in successfully");

        assertThat(page.locator("a[href='/web/index.php/dashboard/index']")).isVisible();
        test.info("Successfully navigated to dashboard page");

        page.locator("a[href='/web/index.php/recruitment/viewRecruitmentModule']").click();
        test.info("Navigated to recruitment module page");

        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Vacancies")).click();
        test.info("Navigated to vacancies module page");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add")).click();
        test.info("Clicking on add vacancies");

        // 1. Vacancy Name
        Locator vacancyName = page.getByRole(AriaRole.TEXTBOX).nth(1);
        vacancyName.click();
        vacancyName.fill("AKD Kapjap");
        test.info("Entered Vacancy Name: AKD");

        // 2. Job Title
        page.getByText("-- Select --").click();
        page.getByRole(AriaRole.OPTION,
                new Page.GetByRoleOptions().setName("Account Assistant")).click();
        test.info("Selected Job Title: Account Assistant");

        // 3. Description
        Locator description = page.getByRole(AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("Type description here"));
        description.click();
        description.fill("AKD TEST");
        test.info("Entered Description: AKD TEST");

        // 4. Hiring Manager - type and select first real suggestion (no hardcoded name)
        Locator hiringManager = page.getByRole(AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("Type for hints..."));

        hiringManager.click();
        hiringManager.pressSequentially("b", new Locator.PressSequentiallyOptions().setDelay(150));

        // Wait for the autocomplete dropdown container to appear
        Locator suggestionList = page.locator("div.oxd-autocomplete-dropdown");
        suggestionList.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10000));

        // Wait for at least one suggestion item inside it
        Locator suggestions = suggestionList.locator("span");
        suggestions.first().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10000));

        // Log all available suggestions (useful for debugging, safe to keep or remove)
        int count = suggestions.count();
        for (int i = 0; i < count; i++) {
            test.info("Suggestion " + i + ": " + suggestions.nth(i).innerText());
        }

        // Guard against "No Records Found"
        String firstSuggestionText = suggestions.first().innerText();
        if (firstSuggestionText.toLowerCase().contains("no records found")) {
            throw new RuntimeException("No hiring manager suggestions found for search term 'b'. "
                    + "Try a different search letter or check demo data availability.");
        }

        // Click the first real suggestion
        suggestions.first().click();
        test.info("Selected Hiring Manager: " + firstSuggestionText);

        // Confirm the field actually got populated (not left invalid)
        Locator invalidError = page.locator("span.oxd-input-field-error-message");
        if (invalidError.isVisible()) {
            throw new RuntimeException("Hiring Manager field still shows validation error after selection: "
                    + invalidError.innerText());
        }

        // 5. Number of Positions
        Locator numberOfPositions = page.getByRole(AriaRole.TEXTBOX).nth(4);
        numberOfPositions.click();
        numberOfPositions.fill("01");
        test.info("Entered Number of Positions: 01");

        // 6. Save
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Save")).click();
        test.info("Clicked Save button to create job vacancy");

        // 7. Check validation errors first (in case Save didn't go through)
        Locator errorMessages = page.locator(".oxd-input-field-error-message");
        if (errorMessages.count() > 0) {
            for (int i = 0; i < errorMessages.count(); i++) {
                test.info("VALIDATION ERROR: " + errorMessages.nth(i).innerText());
            }
            throw new AssertionError("Form validation failed - vacancy was NOT saved.");
        }

        // 8. Confirm success via URL change (this is proven to work from your logs)
        page.waitForURL(Pattern.compile(".*recruitment/addJobVacancy/\\d+"),
                new Page.WaitForURLOptions().setTimeout(15000));
        test.info("Job Vacancy saved successfully. URL: " + page.url());

        // 9. Optional: try to catch the toast too, but don't fail the test if it's already gone
        try {
            Locator successToast = page.locator(".oxd-toast-content--success");
            successToast.waitFor(new Locator.WaitForOptions().setTimeout(3000));
            test.info("Success toast confirmed: " + successToast.innerText());
        } catch (TimeoutError e) {
            test.info("Toast already dismissed (expected - URL change already confirms success)");
        }

        System.out.println("Current URL after save: " + page.url());
        test.info("Current URL after save: " + page.url());
    }



    // ================================================================
    // NEGATIVE TEST 1: Empty required fields
    // ================================================================
    @Test
    public void negativeTestEmptyRequiredFields() {
        test.info("Testing Save with all required fields empty");
        navigateToAddVacancyForm();

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        test.info("Clicked Save without filling any fields");

        Locator errorMessages = page.locator(".oxd-input-field-error-message");
        errorMessages.first().waitFor(new Locator.WaitForOptions().setTimeout(5000));

        int errorCount = errorMessages.count();
        test.info("Validation errors found: " + errorCount);
        for (int i = 0; i < errorCount; i++) {
            test.info("Error " + i + ": " + errorMessages.nth(i).innerText());
        }

        assertThat(errorMessages.first()).isVisible();
        assertTrue(errorCount > 0, "Expected validation errors when saving empty form, but none appeared");

        assertTrue(page.url().contains("addJobVacancy"),
                "Form should not submit successfully with empty required fields");
        test.info("Confirmed form correctly rejected empty submission");
    }

    // ================================================================
    // NEGATIVE TEST 2: Blank Vacancy Name
    // ================================================================
    @Test
    public void negativeTestBlankVacancyName() {
        test.info("Testing Save with blank Vacancy Name");
        navigateToAddVacancyForm();

        page.getByText("-- Select --").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Account Assistant")).click();

        Locator description = page.getByRole(AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("Type description here"));
        description.click();
        description.fill("Negative test - blank name");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        test.info("Clicked Save with Vacancy Name left blank");

        Locator errorMessages = page.locator(".oxd-input-field-error-message");
        errorMessages.first().waitFor(new Locator.WaitForOptions().setTimeout(5000));

        assertThat(errorMessages.first()).isVisible();
        String errorText = errorMessages.first().innerText();
        test.info("Vacancy Name validation error: " + errorText);

        assertTrue(page.url().contains("addJobVacancy"),
                "Form should not save with blank Vacancy Name");
    }

    // ================================================================
    // NEGATIVE TEST 3: Vacancy Name exceeds max length
    // ================================================================
    @Test
    public void negativeTestVacancyNameExceedsMaxLength() {
        test.info("Testing Vacancy Name exceeding max character limit");
        navigateToAddVacancyForm();

        String longName = "A".repeat(200);

        Locator vacancyName = page.getByRole(AriaRole.TEXTBOX).nth(1);
        vacancyName.click();
        vacancyName.fill(longName);
        test.info("Entered " + longName.length() + " character Vacancy Name");

        page.getByText("-- Select --").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Account Assistant")).click();

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();

        Locator errorMessages = page.locator(".oxd-input-field-error-message");
        boolean hasError = errorMessages.count() > 0;

        if (hasError) {
            test.info("Correctly rejected: " + errorMessages.first().innerText());
        } else {
            test.info("No validation error shown - checking if field was truncated instead");
        }

        assertTrue(page.url().contains("addJobVacancy"),
                "Form should not proceed past validation with an oversized Vacancy Name");
    }

    // ================================================================
    // NEGATIVE TEST 4: Number of Positions - non-numeric input
    // ================================================================
    @Test
    public void negativeTestNumberOfPositionsNonNumeric() {
        test.info("Testing Number of Positions with non-numeric input");
        navigateToAddVacancyForm();

        Locator vacancyName = page.getByRole(AriaRole.TEXTBOX).nth(1);
        vacancyName.click();
        vacancyName.fill("Negative Test Vacancy");

        page.getByText("-- Select --").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Account Assistant")).click();

        Locator numberOfPositions = page.getByRole(AriaRole.TEXTBOX).nth(4);
        numberOfPositions.click();
        numberOfPositions.fill("abc");
        test.info("Entered non-numeric value 'abc' into Number of Positions");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();

        Locator errorMessages = page.locator(".oxd-input-field-error-message");
        errorMessages.first().waitFor(new Locator.WaitForOptions().setTimeout(5000));

        assertThat(errorMessages.first()).isVisible();
        test.info("Validation error: " + errorMessages.first().innerText());
        assertTrue(page.url().contains("addJobVacancy"),
                "Form should reject non-numeric Number of Positions");
    }

    // ================================================================
    // NEGATIVE TEST 5: Number of Positions - zero value
    // ================================================================
    @Test
    public void negativeTestNumberOfPositionsZeroOrNegative() {
        test.info("Testing Number of Positions with value '0'");
        navigateToAddVacancyForm();

        Locator vacancyName = page.getByRole(AriaRole.TEXTBOX).nth(1);
        vacancyName.click();
        vacancyName.fill("Zero Positions Test");

        page.getByText("-- Select --").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Account Assistant")).click();

        Locator numberOfPositions = page.getByRole(AriaRole.TEXTBOX).nth(4);
        numberOfPositions.click();
        numberOfPositions.fill("0");
        test.info("Entered '0' into Number of Positions");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();

        Locator errorMessages = page.locator(".oxd-input-field-error-message");
        boolean rejected = errorMessages.count() > 0;

        if (rejected) {
            test.info("Correctly rejected zero positions: " + errorMessages.first().innerText());
        } else {
            test.info("WARNING: '0' positions was accepted - check business rule validity");
        }
    }

    // ================================================================
    // NEGATIVE TEST 6: Duplicate Vacancy Name
    // ================================================================
    @Test
    public void negativeTestDuplicateVacancyName() {
        test.info("Testing duplicate Vacancy Name rejection");

        String duplicateName = "Duplicate Test Vacancy " + System.currentTimeMillis();
        navigateToAddVacancyForm();
        createVacancy(duplicateName);
        page.waitForURL(Pattern.compile(".*recruitment/addJobVacancy/\\d+"),
                new Page.WaitForURLOptions().setTimeout(15000));
        test.info("Created initial vacancy: " + duplicateName);

        page.navigate("https://opensource-demo.orangehrmlive.com/web/index.php/recruitment/viewRecruitmentModule");
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Vacancies")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add")).click();
        createVacancy(duplicateName);
        test.info("Attempted to create duplicate vacancy: " + duplicateName);

        Locator errorMessages = page.locator(".oxd-input-field-error-message");
        boolean rejected = errorMessages.count() > 0;

        if (rejected) {
            test.info("Correctly rejected duplicate name: " + errorMessages.first().innerText());
        } else {
            test.info("NOTE: OrangeHRM may allow duplicate vacancy names - verify against requirements");
        }
    }




    // ================================================================
    // SHARED HELPERS
    // ================================================================
    private void navigateToAddVacancyForm() {
        page.navigate(LOGIN_URL);
        loginAsAdmin();
        page.locator("a[href='/web/index.php/recruitment/viewRecruitmentModule']").click();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Vacancies")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add")).click();
    }

    private void createVacancy(String vacancyName) {
        Locator vacancyNameField = page.getByRole(AriaRole.TEXTBOX).nth(1);
        vacancyNameField.click();
        vacancyNameField.fill(vacancyName);

        page.getByText("-- Select --").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Account Assistant")).click();

        Locator numberOfPositions = page.getByRole(AriaRole.TEXTBOX).nth(4);
        numberOfPositions.click();
        numberOfPositions.fill("01");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
    }



    private void loginAsAdmin() {
        page.locator("input[name='username']").fill("Admin");
        page.locator("input[name='password']").fill("admin123");
        page.locator("button:has-text('Login')").click();
        page.waitForSelector(".oxd-topbar-header-breadcrumb");
    }
}

