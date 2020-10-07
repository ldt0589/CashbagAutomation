package resource.pages;

import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import resource.utility.Utility;
import java.io.IOException;

import static resource.common.GlobalVariables.mainWindow;

public class GoogleSignInPage extends Utility {

//    //Switch to handled window after clicking action links
//    public GoogleSignInPage() throws IOException {
//        switchToWindowHandle();
//    }

    @FindBy(xpath = "//div[contains(text(),'Google')]")
    private WebElement btn_registerGoogle;

    @FindBy(id = "identifierId")
    private WebElement tbx_googleUserName;

    @FindBy(name = "password")
    private WebElement tbx_googlePassWord;

    @FindBy(id = "identifierNext")
    private WebElement btn_Next;

    @FindBy(id = "passwordNext")
    private WebElement btn_passWordNext;

    public void SignInAccount(String userName, String passWord, ExtentTest logTest) throws IOException  {
        try{

            // Switch to new window opened
            for(String winHandle : Utility.getDriver().getWindowHandles()){
                Utility.getDriver().switchTo().window(winHandle);
            }

            // Perform the actions on new window
            waitForControl(tbx_googleUserName);
            logInfo(logTest,  "----->Enter Google username: " + userName);
            tbx_googleUserName.sendKeys(userName);
            logInfo(logTest,  "----->Click on Next button");
            btn_Next.click();

            waitForControl(tbx_googlePassWord);
            logInfo(logTest,  "----->Enter Google password: " + passWord);
            tbx_googlePassWord.sendKeys(passWord);
            logInfo(logTest,  "----->Click on Submit button");
            btn_passWordNext.click();

            // Switch back to main browser
            Utility.getDriver().switchTo().window(mainWindow);

        } catch (Exception e) {
            log4j.error("SignInAccount method - ERROR - " + e);
            logException(logTest, "SignInAccount method - ERROR", e);
        }
    }

}