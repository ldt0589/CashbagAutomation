package resource.pages;

import com.aventstack.extentreports.ExtentTest;
import resource.utility.Utility;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.IOException;

public class JiraIssuePage extends Utility {

    //Switch to handled window after clicking action links
    public JiraIssuePage() throws IOException {
        switchToWindowHandle();
    }

    String issueId = null;

    @FindBy(id = "createGlobalItem")
    private WebElement button_CreateIssue;

    @FindBy(id = "project-field")
    private WebElement textbox_Project;

    @FindBy(id ="issuetype-field")
    private WebElement textbox_IssueType;

    @FindBy(id ="summary")
    private WebElement textbox_Summary;

    @FindBy(xpath ="//input[contains(@id,'submit')]")
    private WebElement button_submit;

    @FindBy(xpath ="//div[contains(@class,'aui-message-success')]")
    private WebElement label_success;

    @FindBy(xpath ="//div[contains(@class,'aui-message-success')]/a")
    private WebElement link_issue;

    /*-------------------------------------*/

    public void openIssuePage() {
        button_CreateIssue.click();
    }

    public void inputTextBoxProject(String project) {
        waitForControl(textbox_Project);
        textbox_Project.click();
        textbox_Project.sendKeys(Keys.chord(Keys.CONTROL, "a"), project);
        textbox_Project.sendKeys(Keys.ENTER);
        log4j.debug("enter project name");
        sleep(3);
    }

    public void inputTextBoxIssueType(String issue) {
        textbox_IssueType.click();
        textbox_IssueType.sendKeys(Keys.chord(Keys.CONTROL, "a"), issue);
        textbox_IssueType.sendKeys(Keys.ENTER);
        log4j.debug("enter issue type");
        sleep(3);
    }

    public void inputTextBoxSummary(String summary) {
        textbox_Summary.clear();
        textbox_Summary.sendKeys(summary);
        log4j.debug("enter summary");
        sleep(3);
    }

    public void clickButtonSubmit() {
        button_submit.click();
    }

    public void createIssue(ExtentTest logTest) throws IOException {
        try {
            log4j.debug("create an Issue...start");

            logInfo(logTest, "Open Create Issue page");
            waitForControl(button_CreateIssue);
            openIssuePage();

//            logInfo(logTest,  "Input Project name");
//            waitForControl(textbox_IssueType);
//            inputTextBoxProject("Project_Demo");

//            logInfo(logTest,  "Input Issue type");
//            inputTextBoxIssueType("Bug");

            logInfo(logTest,  "Input summary");
            inputTextBoxSummary("This is a Story");

            logInfo(logTest,  "Submit issue");
            clickButtonSubmit();

            log4j.info("Create an issue...end");
        } catch (Exception e) {
            log4j.error("CreateIssue method - ERROR - " + e);
            logException(logTest, "CreateIssue method - ERROR", e);
        }
    }

    public void checkSuccessfulMsgDisplay(ExtentTest logTest) throws IOException {
        try {
            log4j.debug("Verify that successful message should be displayed...start");

            logInfo(logTest, "Verify that successful message should be displayed:");
            checkControlExist(logTest, label_success, "success label");

            log4j.info("Verify that successful message should be displayed...end");
        } catch (Exception e) {
            log4j.error("checkSuccessfulMsgDisplay method - ERROR - " + e);
            logException(logTest, "checkSuccessfulMsgDisplay method - ERROR", e);
        }
    }

    public String getIssueId(ExtentTest logTest) throws IOException {
        try {
            issueId = link_issue.getAttribute("data-issue-key");
            log4j.info("Issue Id = " + issueId);

        } catch (Exception e) {
            log4j.error("getIssueId method - ERROR - " + e);
            logException(logTest, "getIssueId method - ERROR", e);
        }
        return issueId;
    }


}