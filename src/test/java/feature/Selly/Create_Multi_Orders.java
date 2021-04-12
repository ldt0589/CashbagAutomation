package feature.Selly;

import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import resource.api.Selly.CartAPI;
import resource.api.Selly.OrderAPI;
import resource.api.Selly.UserAPI;
import resource.common.GlobalVariables;
import resource.common.TestBase;

import java.io.IOException;
import java.util.Hashtable;

public class Create_Multi_Orders extends TestBase {

    private String sellerToken = null;
    private UserAPI userAPI = new UserAPI();
    private CartAPI CartAPI = new CartAPI();
    private OrderAPI OrderAPI = new OrderAPI();
    private JSONObject customer = null;

    @Test(dataProvider = "getDataForTest", priority = 1, description = "Add multi items into Cart")
    public void TC01(Hashtable<String, String> data) throws IOException {
        try {

            logStep = logStepInfo(logMethod, "Step #1: Get seller's token from Phone Number");
            sellerToken = userAPI.getSellerToken(logStep, GlobalVariables.SellerPhone);

            logStep = logStepInfo(logMethod, "Step #2: Clear all items in Cart");
            CartAPI.clearItemsInCart(logStep, sellerToken);

            logStep = logStepInfo(logMethod, "Step #3: Add multi items into Cart");
            CartAPI.addMultiItemsIntoCart(logStep, sellerToken, data);

            logStep = logStepInfo(logMethod, "Step #4: Create new Customer");
            customer = OrderAPI.createCustomer(logStep, sellerToken);

            logStep = logStepInfo(logMethod, "Step #5: Create Multiple Order");
            OrderAPI.createMultiOrder(logStep, sellerToken, customer);

        } catch (Exception e) {
            log4j.error(getStackTrade(e.getStackTrace())) ;
            logException(logMethod, testCaseName, e);
        }

    }

}
