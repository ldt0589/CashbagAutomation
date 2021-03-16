package feature.Selly;

import org.testng.annotations.Test;
import resource.api.Selly.UserAPI;
import resource.api.Selly.CartAPI;
import resource.api.userAPI;
import resource.common.GlobalVariables;
import resource.common.TestBase;

import java.io.IOException;
import java.util.Hashtable;

import static resource.common.GlobalVariables.CB_URL;
import static resource.common.GlobalVariables.referral_code;

public class Add_to_Cart extends TestBase {

    private resource.api.Selly.CartAPI CartAPI = new CartAPI();
    private String sellerToken = null;
    private UserAPI userAPI = new UserAPI();

    @Test(dataProvider = "getDataForTest", priority = 1, description = "Add multi items into Cart")
    public void TC01(Hashtable<String, String> data) throws IOException {
        if (isTestCaseExecutable && isTestDataExecutable(data, logMethod)) {
            try {

                logStep = logStepInfo(logMethod, "Step #1: Get seller's token from Phone Number");
                sellerToken = userAPI.getSellerToken(logStep, GlobalVariables.SellerPhone);

                logStep = logStepInfo(logMethod, "Step #2: Add 1 item of Selly Inventory into Cart");
                CartAPI.getSkuID(logStep, sellerToken, "60420a65e08f6691a78f29e7");

            } catch (Exception e) {
                log4j.error(getStackTrade(e.getStackTrace()));
                logException(logMethod, testCaseName, e);
            }
        }

    }

}
