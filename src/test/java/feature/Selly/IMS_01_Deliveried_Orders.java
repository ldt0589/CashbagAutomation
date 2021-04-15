package feature.Selly;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import resource.api.Selly.CartAPI;
import resource.api.Selly.OrderAPI;
import resource.api.Selly.UserAPI;
import resource.common.GlobalVariables;
import resource.common.TestBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class IMS_01_Deliveried_Orders extends TestBase {

    private String sellerToken = null;
    private String adminToken = null;
    private UserAPI userAPI = new UserAPI();
    private CartAPI CartAPI = new CartAPI();
    private OrderAPI OrderAPI = new OrderAPI();
    private JSONObject customer = null;
    private ArrayList SellyOrderIDList = null;
    private JSONArray IMSArrayList = null;

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
            SellyOrderIDList = OrderAPI.createMultiOrder(logStep, sellerToken, customer);
            OrderAPI.verifySellyOrderStatus(logStep, SellyOrderIDList,"waiting_approved","waiting_approved");

            logStep = logStepInfo(logMethod, "Step #6: Selly Admin APPROVE orders");
            OrderAPI.SellyApproveOrder(logStep, SellyOrderIDList);
            IMSArrayList = OrderAPI.getIMSOrderIdArray(logStep, SellyOrderIDList);
            OrderAPI.verifySellyOrderStatus(logStep, SellyOrderIDList,"pending","pending");
            OrderAPI.verifyIMSOrderStatus(logStep, IMSArrayList, "waiting_approved");

            logStep = logStepInfo(logMethod, "Step #7: IMS APPROVE orders");
            OrderAPI.IMSApproveOrder(logStep, IMSArrayList);
            OrderAPI.verifyIMSOrderStatus(logStep, IMSArrayList, "confirmed");
            OrderAPI.verifySellyOrderStatus(logStep, SellyOrderIDList, "confirmed", "pending");

            logStep = logStepInfo(logMethod, "Step #8: Get IMS IDs Array");
            IMSArrayList = OrderAPI.getIMSOrderIdArray(logStep, SellyOrderIDList);

            logStep = logStepInfo(logMethod, "Step #9: IMS PICKING orders");
            OrderAPI.IMSConfirmOrder(logStep, IMSArrayList, "picking");
            OrderAPI.verifyIMSOrderStatus(logStep, IMSArrayList, "picking");
            OrderAPI.verifySellyOrderStatus(logStep, SellyOrderIDList, "picking", "pending");

            logStep = logStepInfo(logMethod, "Step #10: IMS PICKED orders");
            OrderAPI.IMSConfirmOrder(logStep, IMSArrayList, "picked");
            OrderAPI.verifyIMSOrderStatus(logStep, IMSArrayList, "picked");
            OrderAPI.verifySellyOrderStatus(logStep, SellyOrderIDList, "picked", "pending");

            logStep = logStepInfo(logMethod, "Step #11: IMS DELIVERING orders");
            OrderAPI.IMSConfirmOrder(logStep, IMSArrayList, "delivering");
            OrderAPI.verifyIMSOrderStatus(logStep, IMSArrayList, "delivering");
            OrderAPI.verifySellyOrderStatus(logStep, SellyOrderIDList, "delivering", "delivering");

            logStep = logStepInfo(logMethod, "Step #12: IMS DELIVERED orders");
            OrderAPI.IMSConfirmOrder(logStep, IMSArrayList, "delivered");
            OrderAPI.verifyIMSOrderStatus(logStep, IMSArrayList, "delivered");
            OrderAPI.verifySellyOrderStatus(logStep, SellyOrderIDList, "delivered", "delivered");

        } catch (Exception e) {
            log4j.error(getStackTrade(e.getStackTrace())) ;
            logException(logMethod, testCaseName, e);
        }

    }

}
