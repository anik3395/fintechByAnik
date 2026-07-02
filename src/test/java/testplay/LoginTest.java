package testplay;

import base.BaseTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.Test;
import page.HomePage;
import page.LoginPage;

public class LoginTest extends BaseTest {

    @Test
    public void test(){
        LoginPage loginPage = new LoginPage(page);
        HomePage homePage = new HomePage(page);

        test.info("Test Started");
        page.navigate("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        test.info("Login Page");
        loginPage.login("Admin", "admin123");
        test.info("Login Successful");
        homePage.clickTimeLink();
        test.info("Time Link Clicked");


//        page.navigate("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
//        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username")).click();//Find the locator for the username textbox and click on it
//        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username")).fill("Admin"); //Fill the locator with the username "Admin"
//        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password")).click();
//        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password")).fill("admin123");
//        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
//        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Time")).click();
//        page.locator("form").getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("View")).dblclick();
//        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Leave")).click();
    }

}
