package feature.JiRa.Issue;

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

public class Create_Issue2 extends TestBase {

    private JiraLoginPage jiraLoginPage = null;
    private JiraIssuePage jiraIssuePage = null;
    private Response response;
    private IssueApi issueApi = new IssueApi();

    @Test(dataProvider = "getDataForTest", priority = 1, description = "Create an Issue on Jira")
    public void TC01(Hashtable<String, String> data) throws IOException {
        if (isTestCaseExecutable && isTestDataExecutable(data, logMethod)) {
            try {

                logInfo(logMethod, "Navigated to " + JIRA_URL);
                navigateToTestSite(logMethod, JIRA_URL);

                logStep = logStepInfo(logMethod, "Step #1: User login to JIRA site");
                jiraLoginPage = PageFactory.initElements(Utility.getDriver(), JiraLoginPage.class);
                jiraLoginPage.loginToJira(logStep);

//                logStep = logStepInfo(logMethod, "Step #2: User create issue as bug");
//                jiraIssuePage = PageFactory.initElements(Utility.getDriver(), JiraIssuePage.class);
//                jiraIssuePage.createIssue(logStep);
//
//                logStep = logStepInfo(logMethod, "Step #3: Verify that the successful message displays");
//                jiraIssuePage.checkSuccessfulMsgDisplay(logStep);
//
//                logStep = logStepInfo(logMethod, "Step #4: Verify that the issue is created successfully");
//                response = issueApi.getIssueDetail(logStep, jiraIssuePage.getIssueId(logStep));
//                issueApi.verifyGetIssueResponse(jiraIssuePage.getIssueId(logStep), data, response, logStep);

            } catch (Exception e) {
                    log4j.error(getStackTrade(e.getStackTrace()));
                    logException(logMethod, testCaseName, e);
            }
        }

    }
}
