package feature.MobileTest;

import io.restassured.response.Response;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;
import resource.api.IssueApi;
import resource.common.TestBase;
import resource.pages.JiraIssuePage;
import resource.pages.JiraLoginPage;
import resource.utility.Utility;

import java.io.IOException;
import java.util.Hashtable;

import static resource.common.GlobalVariables.JIRA_URL;

public class TC001_Login_application_successfully extends TestBase {

    private JiraLoginPage jiraLoginPage = null;
    private JiraIssuePage jiraIssuePage = null;
    private Response response;
    private IssueApi issueApi = new IssueApi();
    public String issueID;

    @Test(groups = {"Regression","Smoke"}, dataProvider = "getDataForTest", priority = 1, description = "Create an Issue on Jira")
    public void TC01(Hashtable<String, String> data) throws IOException {
        if (isTestCaseExecutable && isTestDataExecutable(data, logMethod)) {
            try {

                logInfo(logMethod, "Start application");
//                navigateToTestSite(logMethod, JIRA_URL);

//                logStep = logStepInfo(logMethod, "Step #1: User login to JIRA site");
//                jiraLoginPage = PageFactory.initElements(Utility.getDriver(), JiraLoginPage.class);
//                jiraLoginPage.loginToJira(logStep);

//                logStep = logStepInfo(logMethod, "Step #2: User create issue as bug");
//                jiraIssuePage = PageFactory.initElements(Utility.getDriver(), JiraIssuePage.class);
//                jiraIssuePage.createIssue(logStep, data.get("IssueSummary"), data.get("IssueType"));
//
//                logStep = logStepInfo(logMethod, "Step #3: Verify that the successful message displays");
//                jiraIssuePage.checkSuccessfulMsgDisplay(logStep);
//
//                logStep = logStepInfo(logMethod, "Step #4: Verify that the issue is created successfully");
//                issueID = jiraIssuePage.getIssueId(logStep);
//                response = issueApi.getIssueDetail(logStep, issueID);
//                issueApi.verifyIssueResponse(issueID, data, response, logStep);

            } catch (Exception e) {
                    log4j.error(getStackTrade(e.getStackTrace()));
                    logException(logMethod, testCaseName, e);
            }
        }

    }
}
