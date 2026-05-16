import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

public class CSAutomationTest {

    static WebDriver driver;
    static WebDriverWait wait;

    public static void main(String[] args) throws InterruptedException {

        startBrowser();

        String[][] negativeTests = {
//                {"aniknewroztech.com", "Anik100@"},
//                {"anik+1@newroztech.com", "WrongPass123"},
//                {"", ""},
                {"anik+1@newroztech.com", "Anik100@"}
        };

        runNegativeTests(negativeTests);

        clickEnableButton();

        goToProductManagement();
        goToCategories();
        clickAddCategory();
        inputFieldForAddCategory();
        negativeCategoryTests();

    }

    // ===============================
    // START BROWSER
    // ===============================
    public static void startBrowser() {

        driver = new ChromeDriver();

        driver.get("https://dev-panel.cardselling.cash/");

        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ===============================
    // RUN NEGATIVE TESTS
    // ===============================
    public static void runNegativeTests(String[][] tests) throws InterruptedException {

        for (String[] data : tests) {

            String email = data[0];
            String password = data[1];

            enterEmail(email);

            enterPassword(password);

            clickSignInButton();

            validateLoginResult(email, password);

            refreshPage();
        }
    }

    // ===============================
    // ENTER EMAIL
    // ===============================
    public static void enterEmail(String email) throws InterruptedException {

        Thread.sleep(2000);

        WebElement emailField = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("input[name='email']")
                )
        );

        emailField.clear();

        emailField.sendKeys(email);
    }

    // ===============================
    // ENTER PASSWORD
    // ===============================
    public static void enterPassword(String password) throws InterruptedException {

        Thread.sleep(2000);

        WebElement passwordField = driver.findElement(
                By.name("password")
        );

        passwordField.clear();

        passwordField.sendKeys(password);
    }

    // ===============================
    // CLICK SIGN IN
    // ===============================
    public static void clickSignInButton() throws InterruptedException {

        Thread.sleep(1000);

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();
    }

    // ===============================
    // VALIDATE LOGIN RESULT
    // ===============================
    public static void validateLoginResult(String email, String password)
            throws InterruptedException {

        try {

            WebElement errorMessage = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath(
                                    "//*[contains(text(),'Login Failed') " +
                                            "or contains(text(),'Invalid') " +
                                            "or contains(text(),'Error')]"
                            )
                    )
            );

            System.out.println("Login Failed");
            System.out.println("Email: " + email);
            System.out.println("Password: " + password);
            System.out.println("Message: " + errorMessage.getText());

            Thread.sleep(2000);

            driver.findElement(
                    By.xpath("//button[contains(text(),'OK')]")
            ).click();

        } catch (Exception e) {

            System.out.println("Login Success");
        }
    }

    // ===============================
    // REFRESH PAGE
    // ===============================
    public static void refreshPage() {

        driver.navigate().refresh();
    }

    // ===============================
    // CLICK ENABLE BUTTON
    // ===============================
    public static void clickEnableButton() throws InterruptedException {

        try {

            Thread.sleep(2000);

            WebElement enableBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[contains(text(),'Enable')]")
                    )
            );

            enableBtn.click();

            System.out.println("Enable button clicked");

        } catch (Exception e) {

            System.out.println("Enable button not found");
        }
    }

    //Go to PM
    public static void goToProductManagement() throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("Navigating to Product Management page...");
        WebElement product = new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[contains(text(),'Product Management')]")
                ));

        product.click();
        System.out.println("Product Management page opened successfully.");
    }

    public static void goToCategories() throws InterruptedException {
        System.out.println("Navigating to Categories page...");
        Thread.sleep(2000);
        driver.findElement(By.cssSelector("a[href='/category']")).click();
        System.out.println("Categories page opened successfully.");
    }

    public static void clickAddCategory() throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("Clicking Add Category button...");
        WebElement addCategoryBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("button[class*='bg-primary']")
                )
        );

        addCategoryBtn.click();
        System.out.println("Add Category button clicked successfully.");
    }

    public static void inputFieldForAddCategory() throws InterruptedException {

        Thread.sleep(2000);

        driver.findElement(By.cssSelector("input[name='name']")).sendKeys("AKDGHGH");

        Thread.sleep(1000);

        driver.findElement(By.cssSelector("textarea[name='description']")).sendKeys("This is electronics category");

        Thread.sleep(1000);

        driver.findElement(By.cssSelector("input[name='position']")).sendKeys("1");

        // ---------------- FILE UPLOAD FIX ----------------
        Thread.sleep(1000);
        String filePath = System.getProperty("user.home") + "/imagesGift Card_7dc0990d-84be-4214-8ebc-97659a9d104c.svg";

        driver.findElement(By.cssSelector("input[type='file']")).sendKeys(filePath);

        System.out.println("File uploaded: " + filePath);
        // ---------------- STATUS: INACTIVE ----------------
        Thread.sleep(5000);

        driver.findElement(By.xpath("//button[contains(text(),'Inactive')]")).click();

        System.out.println("Status set to Inactive");

        // ---------------- CREATE BUTTON ----------------
        Thread.sleep(5000);

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        System.out.println("Category form submitted successfully.");
    }

    // ==================================================
    // NEGATIVE CATEGORY TESTS
    // ==================================================
    public static void negativeCategoryTests()
            throws InterruptedException {

        String[][] negativeData = {

                {"", "This is electronics category", "1"},
                {"Electronics", "", "1"},
                {"Electronics", "Test Description", ""},
                {"@#$%^", "Invalid Name", "1"},
                {"Electronics", "Test Description", "-1"},
                {"Electronics", "Test Description", "99999"}
        };

        for (String[] data : negativeData) {

            // ================= REOPEN MODAL =================
            Thread.sleep(3000);

            clickAddCategory();

            String name = data[0];
            String description = data[1];
            String position = data[2];

            Thread.sleep(2000);

            // ================= NAME =================
            WebElement nameField = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.cssSelector("input[name='name']")
                    )
            );

            nameField.clear();
            nameField.sendKeys(name);

            // ================= DESCRIPTION =================
            WebElement descriptionField = driver.findElement(
                    By.cssSelector("textarea[name='description']")
            );

            descriptionField.clear();
            descriptionField.sendKeys(description);

            // ================= POSITION =================
            WebElement positionField = driver.findElement(
                    By.cssSelector("input[name='position']")
            );

            positionField.clear();
            positionField.sendKeys(position);

            // ================= FILE =================
            String filePath = System.getProperty("user.home") + "/imagesGift Card_7dc0990d-84be-4214-8ebc-97659a9d104c.svg";


            WebElement uploadInput = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector("input[type='file']")
                    )
            );

            uploadInput.sendKeys(filePath);

            // ================= STATUS =================
            driver.findElement(
                    By.xpath("//button[contains(text(),'Inactive')]")
            ).click();

            // ================= CREATE =================
            driver.findElement(
                    By.cssSelector("button[type='submit']")
            ).click();

            System.out.println("Negative Test Executed");
            System.out.println("Name: " + name);
            System.out.println("Description: " + description);
            System.out.println("Position: " + position);

            Thread.sleep(3000);

            // ================= VALIDATION =================
            try {

                WebElement error = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                By.xpath(
                                        "//*[contains(text(),'required') " +
                                                "or contains(text(),'invalid') " +
                                                "or contains(text(),'error')]"
                                )
                        )
                );

                System.out.println("Validation Message: "
                        + error.getText());

            } catch (Exception e) {

                System.out.println("No Validation Message Found");
            }

            // ================= CLOSE MODAL =================
            driver.navigate().refresh();

            Thread.sleep(3000);

            goToCategories();
        }
    }

}
