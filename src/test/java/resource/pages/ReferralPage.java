package resource.pages;

import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import resource.utility.Utility;
import static resource.common.GlobalVariables.*;
import java.io.IOException;

public class ReferralPage extends Utility {

    //Switch to handled window after clicking action links
    public ReferralPage() throws IOException {
        switchToWindowHandle();
    }

    @FindBy(xpath = "//div[contains(text(),'Google')]")
    private WebElement btn_registerGoogle;

    public void openGoogleSignInWindow(ExtentTest logTest) throws IOException  {
        try{

            // Store the current window handle
            mainWindow = Utility.getDriver().getWindowHandle();

            // Perform the click operation that opens new window
            logInfo(logTest,  "----->Click on button: [Register by Google]");
            waitForControl(btn_registerGoogle);
            ((JavascriptExecutor) Utility.getDriver()).executeScript("arguments[0].click();",btn_registerGoogle);

        } catch (Exception e) {
            log4j.error("openGoogleSignInWindow method - ERROR - " + e);
            logException(logTest, "openGoogleSignInWindow method - ERROR", e);
        }
    }

}