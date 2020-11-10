package resource.pages;

import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import resource.utility.Utility;

import java.io.IOException;

import static resource.common.GlobalVariables.mainWindow;

public class DowloadPage extends Utility {

//    Switch to handled window after clicking action links
//    public DowloadPage() throws IOException {
//        switchToWindowHandle();
//    }

    @FindBy(xpath = "//a[contains(@href,'https://play.google.com/store/apps/details?id=vn.cashbag')]")
    private WebElement btn_AndroidDowload;

    @FindBy(xpath = "//a[contains(@href,'https://apps.apple.com/us/app/cashbag')]")
    private WebElement btn_AppleDowload;

    public void checkButtonDowloadExists(ExtentTest logTest) throws IOException {
        try {

            checkControlExist(logTest, btn_AndroidDowload, "button Android dowload");
            checkControlExist(logTest, btn_AppleDowload, "button Apple dowload");

        } catch (Exception e) {
            log4j.error("checkButtonDowloadExists method - ERROR - " + e);
            logException(logTest, "checkButtonDowloadExists method - ERROR", e);
        }
    }

//    public void openGoogleSignInWindow(ExtentTest logTest) throws IOException  {
//        try{
//
//            // Store the current window handle
//            mainWindow = Utility.getDriver().getWindowHandle();
//
//            // Perform the click operation that opens new window
//            logInfo(logTest,  "----->Click on button: [Register by Google]");
//            waitForControl(btn_registerGoogle);
//            ((JavascriptExecutor) Utility.getDriver()).executeScript("arguments[0].click();",btn_registerGoogle);
//
//        } catch (Exception e) {
//            log4j.error("openGoogleSignInWindow method - ERROR - " + e);
//            logException(logTest, "openGoogleSignInWindow method - ERROR", e);
//        }
//    }

}