import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CSAutomationTest {

    public static void main(String[] args) throws InterruptedException {

        WebDriver driver = new ChromeDriver();

        driver.navigate().to("https://dev-panel.cardselling.cash/");
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // ---------------- NEGATIVE TEST DATA ----------------
        String[][] negativeTests = {
                {"aniknewroztech.com", "Anik100@"},
                {"anik+1@newroztech.com", "WrongPass123"},
                {"", ""},
                {"anik+1@newroztech.com", "Anik100@"}
        };

        for (String[] testData : negativeTests) {

            String email = testData[0];
            String password = testData[1];

            // ---------------- EMAIL ----------------
            WebElement emailField = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.cssSelector("input[name='email']")
                    )
            );
            emailField.clear();
            Thread.sleep(3000);
            emailField.sendKeys(email);

            // ---------------- PASSWORD ----------------
            WebElement passwordField = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.name("password")
                    )
            );
            passwordField.clear();
            Thread.sleep(3000);
            passwordField.sendKeys(password);

            // ---------------- LOGIN CLICK ----------------
            wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.cssSelector("button[type='submit']")
                    )
            ).click();

            // ---------------- ERROR VALIDATION ----------------
            try {

                WebElement error = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                By.xpath(
                                        "//*[contains(text(),'Login Failed') " +
                                                "or contains(text(),'Invalid') " +
                                                "or contains(text(),'failed') " +
                                                "or contains(text(),'Error')]"
                                )
                        )
                );

                System.out.println("❌ Expected failure:");
                System.out.println("Email: " + email + " | Password: " + password);
                System.out.println("Message: " + error.getText());

                // Click OK if exists
                driver.findElements(By.xpath("//button[contains(text(),'OK')]"))
                        .stream()
                        .findFirst()
                        .ifPresent(WebElement::click);

            } catch (Exception e) {
                System.out.println("⚠ No error message found - UI behavior different");
            }

            // ---------------- RESET ----------------
            driver.navigate().refresh();
        }

        // ===============================
        // OPTIONAL: NEXT SCENARIO (ENABLE BUTTON)
        // ===============================

        Thread.sleep(3000);
        try {
            WebElement enableBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[contains(.,'Enable')]")
                    )
            );

            enableBtn.click();
            System.out.println("✅ Enable button clicked successfully");

        } catch (Exception e) {
            System.out.println("⚠ Enable button not found");
        }

    }
}