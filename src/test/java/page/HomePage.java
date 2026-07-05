package page;

import com.microsoft.playwright.Page;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HomePage {
    private final Page page;
    private final String timeLink = "a[href='/web/index.php/time/viewTimeModule1']";

    public void clickTimeLink(){
        page.locator(timeLink).click();
    }
}
