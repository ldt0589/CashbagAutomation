package feature.MobileTest;

import io.restassured.response.Response;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;
import resource.api.IssueApi;
import resource.common.GlobalVariables;
import resource.common.TestBase;
import resource.pages.MobileLoginPage;
import resource.utility.Utility;

import java.io.IOException;
import java.util.Hashtable;

public class TC001_Login_application_successfully extends TestBase {

    @Test(groups = {"Regression","Smoke"}, dataProvider = "getDataForTest", priority = 1, description = "Create an Issue on Jira")
    public void TC01(Hashtable<String, String> data) throws IOException {
        if (isTestCaseExecutable && isTestDataExecutable(data, logMethod)) {
            try {

                MobileLoginPage loginpage = new MobileLoginPage();

                logStep = logStepInfo(logMethod, "Step #1: Open Account Screen");
//                sleep(3);
                loginpage.open_Login_screen(logStep);

                logStep = logStepInfo(logMethod, "Step #2: User logins application");
//                MobileLoginPage.login_application(GlobalVariables.phoneNumber, GlobalVariables.otp, logStep);

            } catch (Exception e) {
                    log4j.error(getStackTrade(e.getStackTrace()));
                    logException(logMethod, testCaseName, e);
            }
        }

    }
}
