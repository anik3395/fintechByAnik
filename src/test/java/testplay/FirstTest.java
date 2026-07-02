package testplay;

import base.BaseTest;

import org.testng.annotations.Test;

public class FirstTest extends BaseTest {
    @Test
    public void VerifyTitle(){

        page.navigate("http://www.google.com/ncr");

        if(page.isVisible("button:has-text('Accept All')")){
            page.click("button:has-text('Accept All')");
        }
        System.out.println("Google NCR opened successfully");
        System.err.println("Page title: " + page.title());

    }



//    public static void main(String[] args) {
//        try(Playwright playwright = Playwright.create()) {
//            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
//            Page page = browser.newPage();
//            page.navigate("http://www.google.com");
//            System.err.println("Page title: " + page.title());
////            browser.close();
//        }
//    }
}
