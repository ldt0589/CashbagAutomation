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

public class PerfectoDemo extends TestBase {

    private JiraLoginPage jiraLoginPage = null;
    private JiraIssuePage jiraIssuePage = null;
    private Response response;
    private IssueApi issueApi = new IssueApi();
    public String issueID;

    @Test(dataProvider = "getDataForTest", priority = 1, description = "Create an Issue on Jira")
    public void TC01(Hashtable<String, String> data) throws IOException {
        if (isTestCaseExecutable && isTestDataExecutable(data, logMethod)) {
            try {

                logInfo(logMethod, "Navigated to " + JIRA_URL);
                navigateToTestSite(logMethod, JIRA_URL);


            } catch (Exception e) {
                    log4j.error(getStackTrade(e.getStackTrace()));
                    logException(logMethod, testCaseName, e);
            }
        }

    }
}
