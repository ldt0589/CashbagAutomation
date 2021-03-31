package feature.Selly;

import org.testng.annotations.Test;
import resource.api.Selly.UserAPI;
import resource.api.Selly.CartAPI;
import resource.common.GlobalVariables;
import resource.common.TestBase;

import java.io.IOException;
import java.util.Hashtable;

public class Add_to_Cart extends TestBase {

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
            CartAPI.addItemIntoCart(logStep, sellerToken, data.get("ProductID_Selly"));
            CartAPI.addItemIntoCart(logStep, sellerToken, data.get("ProductID_Zody"));
//            CartAPI.addItemIntoCart(logStep, sellerToken, data.get("ProductID_Zody2"));
            CartAPI.addItemIntoCart(logStep, sellerToken, data.get("ProductID_Unibag"));
//            CartAPI.addItemIntoCart(logStep, sellerToken, data.get("ProductID_DaNang"));
//            CartAPI.addItemIntoCart(logStep, sellerToken, data.get("ProductID_Cashbag   "));

        } catch (Exception e) {
            log4j.error(getStackTrade(e.getStackTrace()));
            logException(logMethod, testCaseName, e);
        }

    }

}
