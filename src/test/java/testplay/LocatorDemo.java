package testplay;

import base.BaseTest;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.Test;

public class LocatorDemo extends BaseTest {

    @Test
    public void testAllLocators(){

        page.navigate("https://trytestingthis.netlify.app/");
        //Name
        page.locator("input[name='fname']").fill("John");
        page.locator("input[name='lname']").fill("Doe");

        //Radio button
        page.locator("input[value='male']").check();
        page.locator("input[type='radio']").nth(1).check();

        //DropDown
        page.locator("select#option").selectOption("option 2");

        //CheckBox
        page.locator("input[type='checkbox'][value='Option 3']").check();
        page.locator("input[value='Option 1']").check();

        //Date
        page.locator("input[type='date']").fill("2026-07-03");
        page.locator("#day").fill("2026-07-02");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
    }

}
