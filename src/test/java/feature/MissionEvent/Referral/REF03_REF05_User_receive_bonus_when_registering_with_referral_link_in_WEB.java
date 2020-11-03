package feature.MissionEvent.Referral;

import io.restassured.response.Response;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;
import resource.api.IssueApi;
import resource.api.userAPI;
import resource.common.GlobalVariables;
import resource.common.TestBase;
import resource.pages.*;
import resource.pages.ReferralPage;
import resource.utility.Utility;
import java.io.IOException;
import java.util.Hashtable;
import static resource.common.GlobalVariables.*;

public class REF03_REF05_User_receive_bonus_when_registering_with_referral_link_in_WEB extends TestBase {

    private Response response;
    private userAPI userAPI = new userAPI();
    private ReferralPage ReferralPage = null;
    private DowloadPage DowloadPage = null;
    private GoogleSignInPage GoogleSignInPage = null;
    private String userID = null;
    private String userToken = null;
    private String referralBonus = null;

    @Test(dataProvider = "getDataForTest", priority = 1, description = "In WEB, User receives referral bonus when registering account with referral link")
    public void Test(Hashtable<String, String> data) throws IOException {
        if (isTestCaseExecutable && isTestDataExecutable(data, logMethod)) {
            try {

                logStep = logStepInfo(logMethod, "PRE-CONDITION: ");
                logInfo(logStep, "Clean user to make sure that USER account is new");
                userAPI.deleteUserAccount(logStep, GlobalVariables.gg_username);

                logStep = logStepInfo(logMethod, "Step #1: Access referral link: " + CB_URL + data.get("referral_path") + referral_code);
                navigateToTestSite(logStep, CB_URL + data.get("referral_path") + referral_code);

                logStep = logStepInfo(logMethod, "Step #2: Open Sign In Google window");
                ReferralPage = PageFactory.initElements(Utility.getDriver(), ReferralPage.class);
                ReferralPage.openGoogleSignInWindow(logStep);
//
                logStep = logStepInfo(logMethod, "Step #3: Sign In Google Account");
                GoogleSignInPage = PageFactory.initElements(Utility.getDriver(), GoogleSignInPage.class);
                GoogleSignInPage.SignInAccount(gg_username, gg_password, logStep);

                logStep = logStepInfo(logMethod, "Step #4: Get User Info");
                userID = userAPI.getUserId(logStep, GlobalVariables.gg_username);
                userToken = userAPI.getUserToken(logStep, GlobalVariables.gg_username);

                logStep = logStepInfo(logMethod, "Step #5: Verify that the dowload page displays");
                DowloadPage = PageFactory.initElements(Utility.getDriver(), DowloadPage.class);
                DowloadPage.checkButtonDowloadExists(logStep);

                logStep = logStepInfo(logMethod, "Step #6: Verify that user receives referral bonus");
                referralBonus = userAPI.getUserMeStatistic(logStep, userToken, "totalSuccessBonusCash");
                verifyUserStatistic(logStep, referralBonus, GlobalVariables.CB_referral_bonus);


            } catch (Exception e) {
                log4j.error(getStackTrade(e.getStackTrace()));
                logException(logMethod, testCaseName, e);
            }
        }

    }
}
