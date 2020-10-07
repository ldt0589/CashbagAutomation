package resource.pages;

import com.aventstack.extentreports.ExtentTest;
import resource.utility.Utility;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import resource.common.GlobalVariables;

import java.io.IOException;

public class JiraLoginPage extends Utility {

    //Switch to handled window after clicking action links
    public JiraLoginPage() throws IOException {
        switchToWindowHandle();
    }

    private String pageName = "Jira > Login Page > ";


    @FindBy(id = "username")
    private WebElement textbox_Username;

    @FindBy(id = "password")
    private WebElement textbox_Password;

    @FindBy(id ="login-submit")
    private WebElement button_Login;

    public void inputTextBoxDealerCode(String username) {
        textbox_Username.clear();
        textbox_Username.sendKeys(username);
    }

    public void inputTextBoxPassword(String password) {
        textbox_Password.clear();
        textbox_Password.sendKeys(password);
    }

    public void clickButtonLogin() {
        forceClick(button_Login);
//        button_Login.click();
    }


    public void loginToJira(ExtentTest logTest) throws IOException {
        try {
            log4j.debug("Login to JIRA...start");

            logInfo(logTest, pageName + "Input username: " + GlobalVariables.JIRA_USERNAME);
            waitForControl(textbox_Username);
            inputTextBoxDealerCode(GlobalVariables.JIRA_USERNAME);

            waitForControl(button_Login);
            clickButtonLogin();

            logInfo(logTest, pageName + "Input password ");
            waitForControl(textbox_Password);
            inputTextBoxPassword(GlobalVariables.JIRA_PASSWORD);

            clickButtonLogin();

            log4j.info("Login to JIRA portal...end");
        } catch (Exception e) {
            log4j.error("loginToJira method - ERROR - " + e);
            logException(logTest, "loginToJira method - ERROR", e);
        }
    }

}