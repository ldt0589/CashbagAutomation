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

public class IMS_02_Cancelled_Orders_Then_Change_Delivery_Service extends TestBase {

    private String sellerToken = null;
    private String adminToken = null;
    private UserAPI userAPI = new UserAPI();
    private CartAPI CartAPI = new CartAPI();
    private OrderAPI OrderAPI = new OrderAPI();
    private JSONObject customer = null;
    private ArrayList SellyOrderIDList = null;
    private JSONArray IMSArrayListOld = null;
    private JSONArray IMSArrayListNew = null;

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
            SellyOrderIDList = OrderAPI.createMultiOrder(logStep, sellerToken, customer, data.get("Courier_name"));
            OrderAPI.verifySellyOrderStatus(logStep, SellyOrderIDList,"waiting_approved","waiting_approved");

            logStep = logStepInfo(logMethod, "Step #6: Selly Admin APPROVE orders");
            OrderAPI.SellyConfirmOrder(logStep, SellyOrderIDList, "approve");
            IMSArrayListOld = OrderAPI.getIMSOrderIdArray(logStep, SellyOrderIDList);
            OrderAPI.verifySellyOrderStatus(logStep, SellyOrderIDList,"pending","pending");
            OrderAPI.verifyIMSOrderStatus(logStep, IMSArrayListOld, "waiting_approved");

            logStep = logStepInfo(logMethod, "Step #7: IMS APPROVE orders");
            OrderAPI.IMSApproveOrder(logStep, IMSArrayListOld);
            OrderAPI.verifyIMSOrderStatus(logStep, IMSArrayListOld, "confirmed");
            OrderAPI.verifySellyOrderStatus(logStep, SellyOrderIDList, "pending", "pending");

            logStep = logStepInfo(logMethod, "Step #8: Get IMS IDs Array");
            IMSArrayListOld = OrderAPI.getIMSOrderIdArray(logStep, SellyOrderIDList);

            logStep = logStepInfo(logMethod, "Step #9: IMS PICKING orders");
            OrderAPI.IMSConfirmOrder(logStep, IMSArrayListOld, "picking");
            OrderAPI.verifyIMSOrderStatus(logStep, IMSArrayListOld, "picking");
            OrderAPI.verifySellyOrderStatus(logStep, SellyOrderIDList, "pending", "pending");

            logStep = logStepInfo(logMethod, "Step #10: IMS PICKED orders");
            OrderAPI.IMSConfirmOrder(logStep, IMSArrayListOld, "picked");
            OrderAPI.verifyIMSOrderStatus(logStep, IMSArrayListOld, "picked");
            OrderAPI.verifySellyOrderStatus(logStep, SellyOrderIDList, "pending", "pending");

            logStep = logStepInfo(logMethod, "Step #11 IMS DELIVERING orders");
            OrderAPI.IMSConfirmOrder(logStep, IMSArrayListOld, "delivering");
            OrderAPI.verifyIMSOrderStatus(logStep, IMSArrayListOld, "delivering");
            OrderAPI.verifySellyOrderStatus(logStep, SellyOrderIDList, "delivering", "delivering");

            logStep = logStepInfo(logMethod, "Step #12: IMS CANCELLED orders");
            OrderAPI.IMSConfirmOrder(logStep, IMSArrayListOld, "cancelled");
            OrderAPI.verifyIMSOrderStatus(logStep, IMSArrayListOld, "cancelled");
            OrderAPI.verifySellyOrderStatus(logStep, SellyOrderIDList, "waiting_cancelled", "delivering");

            logStep = logStepInfo(logMethod, "Step #13: SELLY Admin updates another delivery service for Order");
            OrderAPI.SellyUpdateDeliveryService(logStep, SellyOrderIDList, data.get("Courier_name_new"));
            OrderAPI.verifySellyOrderStatus(logStep, SellyOrderIDList, "waiting_approved", "delivering");
            OrderAPI.verifySellyOrderHistory(logStep, SellyOrderIDList, data.get("Courier_name"), "cancelled", data.get("Courier_name_new"), "waiting_approved");

            logStep = logStepInfo(logMethod, "Step #14: Selly Admin APPROVE orders again");
            OrderAPI.SellyConfirmOrder(logStep, SellyOrderIDList, "approve");
            IMSArrayListNew = OrderAPI.getIMSOrderIdArray(logStep, SellyOrderIDList);
            OrderAPI.verifySellyOrderStatus(logStep, SellyOrderIDList,"pending","delivering");
            OrderAPI.verifyIMSOrderStatus(logStep, IMSArrayListNew, "waiting_approved");
            OrderAPI.verifyNewIMSOrderCreated(logStep, SellyOrderIDList, IMSArrayListOld, IMSArrayListNew);

            logStep = logStepInfo(logMethod, "Step #15: IMS APPROVE orders");
            OrderAPI.IMSApproveOrder(logStep, IMSArrayListNew);
            OrderAPI.verifyIMSOrderStatus(logStep, IMSArrayListNew, "confirmed");
            OrderAPI.verifySellyOrderStatus(logStep, SellyOrderIDList, "pending", "delivering");

            logStep = logStepInfo(logMethod, "Step #16: Get IMS IDs Array");
            IMSArrayListNew = OrderAPI.getIMSOrderIdArray(logStep, SellyOrderIDList);

            logStep = logStepInfo(logMethod, "Step #17: IMS PICKING orders");
            OrderAPI.IMSConfirmOrder(logStep, IMSArrayListNew, "picking");
            OrderAPI.verifyIMSOrderStatus(logStep, IMSArrayListNew, "picking");
            OrderAPI.verifySellyOrderStatus(logStep, SellyOrderIDList, "pending", "delivering");

            logStep = logStepInfo(logMethod, "Step #18: IMS PICKED orders");
            OrderAPI.IMSConfirmOrder(logStep, IMSArrayListNew, "picked");
            OrderAPI.verifyIMSOrderStatus(logStep, IMSArrayListNew, "picked");
            OrderAPI.verifySellyOrderStatus(logStep, SellyOrderIDList, "pending", "delivering");

            logStep = logStepInfo(logMethod, "Step #19: IMS DELIVERING orders");
            OrderAPI.IMSConfirmOrder(logStep, IMSArrayListNew, "delivering");
            OrderAPI.verifyIMSOrderStatus(logStep, IMSArrayListNew, "delivering");
            OrderAPI.verifySellyOrderStatus(logStep, SellyOrderIDList, "delivering", "delivering");

            logStep = logStepInfo(logMethod, "Step #20: IMS DELIVERED orders");
            OrderAPI.IMSConfirmOrder(logStep, IMSArrayListNew, "delivered");
            OrderAPI.verifyIMSOrderStatus(logStep, IMSArrayListNew, "completed");
            OrderAPI.verifySellyOrderStatus(logStep, SellyOrderIDList, "delivered", "delivered");

        } catch (Exception e) {
            log4j.error(getStackTrade(e.getStackTrace())) ;
            logException(logMethod, testCaseName, e);
        }

    }

}
