package feature.SELLY;

import org.testng.annotations.Test;
import resource.api.Selly.CartAPI;
import resource.api.Selly.UserAPI;
import resource.common.GlobalVariables;
import resource.common.TestBase;

import java.io.IOException;
import java.util.Hashtable;

public class Selly_02_Add_to_Cart_Copy01 extends TestBase {

    private CartAPI CartAPI = new CartAPI();
    private String sellerToken = null;
    private UserAPI userAPI = new UserAPI();

    @Test(dataProvider = "getDataForTest", priority = 1, description = "Add multi items into Cart")
    public void TC01(Hashtable<String, String> data) throws IOException {
        try {

            logStep = logStepInfo(logMethod, "Step #1: Get seller's token from Phone Number");
            sellerToken = userAPI.getSellerToken(logStep, GlobalVariables.SellerPhone);

            logStep = logStepInfo(logMethod, "Step #2: Clear all items in Cart");
            CartAPI.clearItemsInCart(logStep, sellerToken);

            logStep = logStepInfo(logMethod, "Step #3: Add multi items into Cart");
            CartAPI.addMultiItemsIntoCart(logStep, sellerToken, data);

        } catch (Exception e) {
            log4j.error(getStackTrade(e.getStackTrace()));
            logException(logMethod, testCaseName, e);
        }

    }

}
