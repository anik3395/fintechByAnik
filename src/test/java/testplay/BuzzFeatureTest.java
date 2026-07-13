package testplay;

import base.BaseTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Slf4j
public class BuzzFeatureTest extends BaseTest {
    private static final String LOGIN_URL = "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";

    @Test
    public void testPositiveCaseForBuzz(){

        test.info("Testing starting for Buzz Feature");
        page.navigate(LOGIN_URL);
        loginAsAdmin();
        test.info("Login Successful");
        page.locator("a[href='/web/index.php/buzz/viewBuzz']").click();
        test.info("Navigated to Buzz Module");

        Locator buzzTextArea = page.getByRole(AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("What's on your mind?"));

        buzzTextArea.click();
        test.info("Clicking on the text area to post a new buzz");

        String postText = "Team collaboration is going well this week.";
        buzzTextArea.fill(postText);
        test.info("Filled up the text area with: " + postText);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Post").setExact(true)).click();
        test.info("Clicked on the button to post a new buzz");
        page.waitForTimeout(2000);

        assertThat(page.getByText(postText).first())
                .isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000));

        test.pass("Positive Test Passed: Buzz post created successfully");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Share Photos")).click();
        test.info("Clicked on the button to share photos");
        page.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("Add Photos"))).nth(3).click();
        test.info("Clicked on the Add Photos button");
        page.waitForTimeout(5000);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Choose File"))
                .setInputFiles(Paths.get("/home/anik/Pictures/Screenshots/Screenshot from 2026-07-03 18-04-58.png"));
        page.waitForTimeout(2000);
        test.info("Selected a photo to upload");

    }

    private void loginAsAdmin() {
        page.locator("input[name='username']").fill("Admin");
        page.locator("input[name='password']").fill("admin123");
        page.locator("button:has-text('Login')").click();
        page.waitForSelector(".oxd-topbar-header-breadcrumb");
    }
}
