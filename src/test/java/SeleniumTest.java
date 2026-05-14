import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SeleniumTest {

    static WebDriver driver;

    public static void main(String[] args) throws InterruptedException {

        startBrowser();

        loginProcess();

        employeeNavigation();

        searchEmployee();

        clearSearch();

    }

    // ===========================
    // Browser Start
    // ===========================

    public static void startBrowser() throws InterruptedException {

        driver = new ChromeDriver();

        Thread.sleep(2000);

        System.out.println("Navigating to application...");

        driver.navigate().to("http://eaapp.somee.com/");

        Thread.sleep(2000);

        driver.manage().window().maximize();

        System.out.println("Browser maximized.");

        Thread.sleep(2000);
    }

    // ===========================
    // Login Process
    // ===========================

    public static void loginProcess() throws InterruptedException {

        System.out.println("Opening Login Page...");

        driver.findElement(By.cssSelector("a[href='/Account/Login']")).click();

        Thread.sleep(2000);

        System.out.println("Entering Username...");

        driver.findElement(By.name("UserName")).sendKeys("anikkk");

        String[] passwords = {"111111", "123456"};

        for (String password : passwords) {

            WebElement passwordField = driver.findElement(By.name("Password"));

            passwordField.clear();

            Thread.sleep(1000);

            passwordField.sendKeys(password);

            Thread.sleep(1000);

            driver.findElement(By.cssSelector("button[type='submit']")).click();

            Thread.sleep(3000);

            System.out.println("Attempted password: " + password);
        }

        System.out.println("Login process completed.");

        Thread.sleep(2000);
    }

    // ===========================
    // Navigate Employee Page
    // ===========================

    public static void employeeNavigation() throws InterruptedException {

        System.out.println("Navigating to Employee page...");

        driver.findElement(By.cssSelector("a[href='/Employee']")).click();

        Thread.sleep(3000);

        System.out.println("Employee page opened successfully.");
    }

    // ===========================
    // Employee Search
    // ===========================

    public static void searchEmployee() {

        System.out.println("Starting employee search...");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("searchTerm")));

        String[] searchNames = {
                "John Anderson",
                "John Smith",
                "Michael Johnson",
                "WrongUser",
                "NothingFound"
        };

        for (String name : searchNames) {

            WebElement searchBox = driver.findElement(By.name("searchTerm"));

            searchBox.clear();

            searchBox.sendKeys(name);

            driver.findElement(By.cssSelector("button.btn-search")).click();

            System.out.println("Searched for: " + name);

            String pageSource = driver.getPageSource();

            if (pageSource.contains(name)) {
                System.out.println("Found : " + name);
            } else {
                System.out.println("Not Found : " + name);
            }

            System.out.println("----------------------------");
        }
    }

    // ===========================
    // Clear Search
    // ===========================

    public static void clearSearch() throws InterruptedException {

        System.out.println("Clearing search...");

        driver.findElement(By.linkText("✕ Clear")).click();

        Thread.sleep(2000);

        System.out.println("Search cleared successfully.");
    }

    // ===========================
    // Close Browser
    // ===========================

}
