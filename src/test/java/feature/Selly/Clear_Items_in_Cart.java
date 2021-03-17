package feature.Selly;

import org.testng.annotations.Test;
import resource.api.Selly.CartAPI;
import resource.api.Selly.UserAPI;
import resource.common.GlobalVariables;
import resource.common.TestBase;

import java.io.IOException;
import java.util.Hashtable;

public class Clear_Items_in_Cart extends TestBase {

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
                CartAPI.addItemIntoCart(logStep, sellerToken, "6041f4a394191010d111a460");

            } catch (Exception e) {
                log4j.error(getStackTrade(e.getStackTrace()));
                logException(logMethod, testCaseName, e);
            }
        }

    }

}
