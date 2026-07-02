package page;

import com.microsoft.playwright.Page;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public class LoginPage {
    private final Page page;
    private final String usernameTextbox = "input[name='username']";
    private final String passwordTextbox = "input[name='password']";
    private final String loginButtonText = "button[type='submit']";

    public void login(String username, String password) {
        page.fill(usernameTextbox, username);
        page.fill(passwordTextbox, password);
        page.click(loginButtonText);
    }
}
