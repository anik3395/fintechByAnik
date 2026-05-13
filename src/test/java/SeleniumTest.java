import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumTest {
    public static void main(String[] args) throws InterruptedException {
//        System.out.println("Hello World");

        WebDriver driver = new ChromeDriver();

        Thread.sleep(2000);

        driver.navigate().to("http://eaapp.somee.com/");

        Thread.sleep(2000);

        driver.manage().window().maximize();

        Thread.sleep(2000);
//        By locator = By.linkText("Login");
        By locator = By.cssSelector("a[href='/Account/Login']");

        Thread.sleep(2000);
        WebElement element = driver.findElement(locator);

        Thread.sleep(2000);
        element.click();
//
//        Thread.sleep(2000);
//
//        System.out.println("Current URL: " + driver.getCurrentUrl());
//
//        String source = driver.getPageSource();
//        if(source.contains("Search with Microsoft Bing and use the power of AI to find information")){
//            System.out.println("Found Microsoft and Bing");
//        }else {
//            System.out.println("Not Found Microsoft and Bing");
//        }
//
//        for(var handler : driver.getWindowHandles()){
//            System.out.println("My browser current handle : " + handler);
//        }
//
////        driver.manage().window().minimize();
////
////        Thread.sleep(2000);
////
////        driver.close();
    }
}
