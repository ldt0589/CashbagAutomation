package feature.MissionEvent.Referral;

import io.restassured.response.Response;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;
import resource.api.IssueApi;
import resource.api.userAPI;
import resource.common.GlobalVariables;
import resource.common.TestBase;
import resource.pages.ReferralPage;
import resource.pages.GoogleSignInPage;
import resource.pages.JiraLoginPage;
import resource.pages.ReferralPage;
import resource.utility.Utility;
import java.io.IOException;
import java.util.Hashtable;
import static resource.common.GlobalVariables.*;

public class REF03_REF05_User_receive_bonus_when_registering_with_referral_link_in_WEB extends TestBase {

    private Response response;
    private userAPI userAPI = new userAPI();
    private ReferralPage ReferralPage = null;
    private GoogleSignInPage GoogleSignInPage = null;
    private String userID = null;
    private String userToken = null;

    @Test(dataProvider = "getDataForTest", priority = 1, description = "In WEB, User receives referral bonus when registering account with referral link")
    public void Test(Hashtable<String, String> data) throws IOException {
        if (isTestCaseExecutable && isTestDataExecutable(data, logMethod)) {
            try {

//                logStep = logStepInfo(logMethod, "PRE-CONDITION: Make sure account doesn't exist in CASHBAG DB");
//                userAPI.deleteUserAccount(logStep, GlobalVariables.gg_username);


                userID = userAPI.getUserInfo(logStep, GlobalVariables.gg_username).get("data.user._id").toString();
                logStep = logStepInfo(logMethod, "UserID: " + userID);

                userToken = userAPI.getUserInfo(logStep, GlobalVariables.gg_username).get("data.token").toString();
                logStep = logStepInfo(logMethod, "UserToken: " + userToken);


//                logStep = logStepInfo(logMethod, "Step #1: Access referral link: " + CB_URL + data.get("referral_path") + referral_code);
//                navigateToTestSite(logStep, CB_URL + data.get("referral_path") + referral_code);
//
//                logStep = logStepInfo(logMethod, "Step #2: Open Sign In Google window");
//                ReferralPage = PageFactory.initElements(Utility.getDriver(), ReferralPage.class);
//                ReferralPage.openGoogleSignInWindow(logStep);
//
//                logStep = logStepInfo(logMethod, "Step #3: Sign In Google Account");
//                GoogleSignInPage = PageFactory.initElements(Utility.getDriver(), GoogleSignInPage.class);
//                GoogleSignInPage.SignInAccount(gg_username, gg_password, logStep);

//                logStep = logStepInfo(logMethod, "Step #1: User login to JIRA site");
//                jiraLoginPage = PageFactory.initElements(Utility.getDriver(), JiraLoginPage.class);
//                jiraLoginPage.loginToJira(logStep);
//
//                logStep = logStepInfo(logMethod, "Step #2: User create issue as bug");
//                jiraIssuePage = PageFactory.initElements(Utility.getDriver(), JiraIssuePage.class);
//                jiraIssuePage.createIssue(logStep);
//
//                logStep = logStepInfo(logMethod, "Step #3: Verify that the successful message displays");
//                jiraIssuePage.checkSuccessfulMsgDisplay(logStep);
//
//                logStep = logStepInfo(logMethod, "Step #4: Verify that the issue is created successfully");
//                response = issueApi.getIssueAPI(logStep, jiraIssuePage.getIssueId(logStep));
//                issueApi.verifyGetIssueResponse(jiraIssuePage.getIssueId(logStep), data, response, logStep);

            } catch (Exception e) {
                log4j.error(getStackTrade(e.getStackTrace()));
                logException(logMethod, testCaseName, e);
            }
        }

    }
}
