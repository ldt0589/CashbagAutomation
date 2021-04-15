package resource.api.Selly;

import com.aventstack.extentreports.ExtentTest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import resource.common.GlobalVariables;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class OrderAPI extends CartAPI{

    private JSONArray cartArr = null;
    private JSONArray deliverySessionArr = null;
    private JSONArray deliverySessionArray = null;
    private JSONObject deliverySessionObject = null;
    private JSONObject itemsObject = null;
    private JSONArray sessionArray = null;
    private JSONObject jsonSession = null;
    private JSONObject jsonBody = null;
    private JSONParser jsonParser = new JSONParser();
    private JSONObject responseObject = null;
    private JSONObject customerObject = null;
    private JSONArray OrderIDsArray = new JSONArray();
    private JSONArray IMSArrayList = null;
    private String customerID = null;
    private String locationID = null;
    public static ArrayList<String> sessionIDList = new ArrayList<String>();
    public static ArrayList<String> IMSIdList = null;
    public static ArrayList<String> orderIDList = new ArrayList<String>();
    public static ArrayList<String> sessionOrderIDList = new ArrayList<String>();
    public static ArrayList<String> deliverySessionIDList = new ArrayList<String>();
    public static ArrayList<String> orderIDs = null;
    public static String sessionKey = null;
    private ArrayList<String> IMSIDs = null;
    private String checksum = "SmaAalemakwAskd";
    private String payloadString;

    private RequestSpecification createMultiSessionOrderSpecification(String userToken) {
        return given().
                baseUri("https://" + GlobalVariables.SellyEnvironment).
                header("Authorization", "Bearer " + userToken).
                header("Content-Type","application/json").
                contentType("application/json").
                relaxedHTTPSValidation();
    }

    private RequestSpecification createMultiDeliverySessionSpecification(String userToken) {
        return given().
                baseUri("https://" + GlobalVariables.SellyEnvironment).
                header("Authorization", "Bearer " + userToken).
                header("Content-Type","application/json").
                contentType("application/json").
                relaxedHTTPSValidation();
    }

    private RequestSpecification createMultiOrderSpecification(String userToken) {
        return given().
                baseUri("https://" + GlobalVariables.SellyEnvironment).
                header("Authorization", "Bearer " + userToken).
                header("Content-Type","application/json").
                contentType("application/json").
                relaxedHTTPSValidation();
    }

    private RequestSpecification createCustomer(String userToken) {
        return given().
                baseUri("https://" + GlobalVariables.SellyEnvironment).
                header("Authorization", "Bearer " + userToken).
                header("Content-Type","application/json").
                contentType("application/json").
        relaxedHTTPSValidation();
    }

    private RequestSpecification getOrderDetail() {
        return given().
                baseUri("https://" + GlobalVariables.SellyAdminEnvironment).
                header("Authorization", "Bearer " + GlobalVariables.SellyAdminToken).
                header("Content-Type","application/json").
                contentType("application/json").
                relaxedHTTPSValidation();
    }

    private RequestSpecification getAppOrderDetail(String sellerToken) {
        return given().
                baseUri("https://" + GlobalVariables.SellyEnvironment).
                header("Authorization", "Bearer " + sellerToken).
                header("Content-Type","application/json").
                contentType("application/json").
                relaxedHTTPSValidation();
    }

    private RequestSpecification getIMSOrderList() {
        return given().
                baseUri("https://" + GlobalVariables.ImsEnvironment).
                header("Authorization", "Bearer " + GlobalVariables.IMSToken).
                header("Content-Type","application/json").
                contentType("application/json").
                relaxedHTTPSValidation();
    }

    private RequestSpecification getIMSOrderDetail() {
        return given().
                baseUri("https://" + GlobalVariables.ImsEnvironment).
                header("Authorization", "Bearer " + GlobalVariables.IMSToken).
                header("Content-Type","application/json").
                contentType("application/json").
                relaxedHTTPSValidation();
    }

    private RequestSpecification IMSConfirmOrder() {
        return given().
                baseUri("https://" + GlobalVariables.ImsDeliveryEnvironment).
                header("Content-Type","application/json").
                contentType("application/json").
                relaxedHTTPSValidation();
    }

    private RequestSpecification IMSApproveOrder() {
        return given().
                baseUri("https://" + GlobalVariables.ImsEnvironment).
                header("Authorization", "Bearer " + GlobalVariables.IMSToken).
                header("Content-Type","application/json").
                contentType("application/json").
                relaxedHTTPSValidation();
    }

    private RequestSpecification adminConfirmOrder() {
        return given().
                baseUri("https://" + GlobalVariables.SellyAdminEnvironment).
                header("Authorization", "Bearer " + GlobalVariables.SellyAdminToken).
                header("Content-Type","application/json").
                contentType("application/json").
        relaxedHTTPSValidation();
    }

    public static String readPayloadDataFromJsonFile(String filePath) throws IOException {
        String data = null;
        try {
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(new java.io.FileReader(filePath));
            data = obj.toString();

        } catch (Exception e) {
            log4j.error("readPayloadDataFromJsonFile method - ERROR - " + e);
        }
        return data;
    }

    public ArrayList<String> getIMSIdList(ExtentTest logTest, ArrayList SellyOrderIDList) throws IOException {

        try {
            IMSIdList = new ArrayList<>();

            for (int i = 0; i < SellyOrderIDList.size(); i++) {

                String OrderID = (String) SellyOrderIDList.get(i);
                RequestSpecification getOrderDetail = this.getOrderDetail();
                Response response = getOrderDetail.get("/order/" + OrderID + "/items");

                logInfo(logTest, "-----> getOrderDetail Response Body: " + response.getBody().asString());

                String ImsID = (String) ((JSONObject) ((JSONObject) ((JSONObject) jsonParser.parse(response.body().asString())).get("data")).get("data")).get("codeOsiris");

                IMSIdList.add(ImsID);

                logInfo(logTest, "-----> IMS Id List: " + IMSIdList.toString());
            }

            return IMSIdList;

        } catch (Exception e) {
            log4j.error("getIMSIdList method - ERROR: " + e);
            logException(logTest, "getIMSIdList method - ERROR: ", e);
        }
        return IMSIdList;
    }

    public JSONArray getIMSOrderIdArray(ExtentTest logTest, ArrayList SellyOrderIDList) throws IOException {

        try {
            IMSIDs = new ArrayList<>();
            IMSIDs = getIMSIdList(logTest, SellyOrderIDList);

            OrderIDsArray = new JSONArray();
            RequestSpecification getIMSOrderList = this.getIMSOrderList();
            Response response = getIMSOrderList.get("/admin/order");
            jsonBody = (JSONObject) jsonParser.parse(response.body().asString());
            JSONArray OrderArray_response = (JSONArray) ((JSONObject) jsonBody.get("data")).get("data");

            for (int i = 0; i < IMSIDs.size(); i++) {
                String ImsID_actual= IMSIDs.get(i);
                String ImsID_expected = (String) ((JSONObject) OrderArray_response.get(i)).get("code");

                if(ImsID_actual.equals(ImsID_expected)){
                    String IMSOrderID = (String) ((JSONObject) OrderArray_response.get(i)).get("_id");
                    JSONObject deliveryObject = (JSONObject) ((JSONObject) OrderArray_response.get(i)).get("delivery");

                    JSONObject orderIDObject = new JSONObject();
                    orderIDObject.put("ImsID", ImsID_actual);
                    orderIDObject.put("trackingCode", deliveryObject.get("trackingCode"));
                    orderIDObject.put("IMSOrderID", IMSOrderID);
                    OrderIDsArray.add(orderIDObject);

                }
            }
            logInfo(logTest, "-----> getIMSOrderIdArray Response Body: " + response.getBody().asString());
            logInfo(logTest, "-----> OrderIDsArray: " + OrderIDsArray.toString());

            return OrderIDsArray;

        } catch (Exception e) {
            log4j.error("getIMSOrderIdArray method - ERROR: " + e);
            logException(logTest, "getIMSOrderIdArray method - ERROR: ", e);
        }
        return OrderIDsArray;
    }

    public void IMSApproveOrder(ExtentTest logTest, JSONArray IMSArrayList) throws IOException {

        try {

//            JSONArray IMSArrayList = getIMSOrderIdArray(logTest, IMSIDList);

            for (int i = 0; i < IMSArrayList.size(); i++) {

                String ISMOrderID = (String) ((JSONObject) IMSArrayList.get(i)).get("IMSOrderID");

                RequestSpecification IMSApproveOrder = this.IMSApproveOrder();
                Response response = IMSApproveOrder.patch("admin/order/" + ISMOrderID + "/approved");
                logInfo(logTest, "-----> IMSApproveOrder Request URL: https://" + GlobalVariables.ImsEnvironment + "admin/order/" + ISMOrderID + "/approved");
                logInfo(logTest, "-----> IMSApproveOrder Response: " + response.getBody().asString());

            }

        } catch (Exception e) {
            log4j.error("IMSApproveOrder method - ERROR: " + e);
            logException(logTest, "IMSApproveOrder method - ERROR: ", e);
        }
    }

    public void IMSConfirmOrder(ExtentTest logTest, JSONArray IMSArrayList, String status) throws IOException {

        try {
            int deliveryCode = 0;

            switch (status) {
                case "cancelled":
                    deliveryCode = 701;
                    break;
                case "picking":
                    deliveryCode = 201;
                    break;
                case "picked":
                    deliveryCode = 300;
                    break;
                case "delivering":
                    deliveryCode = 304;
                    break;
                case "delivered":
                    deliveryCode = 800;
                    break;
                default:
                    // nothing
            }

            for (int i = 0; i < IMSArrayList.size(); i++) {

                String IMSID = (String) ((JSONObject) IMSArrayList.get(i)).get("ImsID");
                String trackingCode = (String) ((JSONObject) IMSArrayList.get(i)).get("trackingCode");
                int finalDeliveryCode = deliveryCode;

                RequestSpecification IMSConfirmOrder = this.IMSConfirmOrder();
                IMSConfirmOrder.body(new HashMap<String, Object>() {{
                    put("TrackingCode", trackingCode);
                    put("StatusCode", finalDeliveryCode);
                    put("OrderCode", IMSID);
                }}).log().all();
                Response response = IMSConfirmOrder.post("/api/webhook/boxme/delivery-status");
                logInfo(logTest, "-----> IMSConfirmOrder Request URL: https://" + GlobalVariables.ImsDeliveryEnvironment + "/api/webhook/boxme/delivery-status");
                logInfo(logTest, "-----> IMSConfirmOrder Request Body: \n{TrackingCode:" + trackingCode + "\nStatusCode:" + finalDeliveryCode+ "\nOrderCode:" + IMSID + "}");
                logInfo(logTest, "-----> IMSConfirmOrder Response Body: " + response.getBody().asString());

            }

        } catch (Exception e) {
            log4j.error("IMSConfirmOrder method - ERROR: " + e);
            logException(logTest, "IMSConfirmOrder method - ERROR: ", e);
        }
    }

    public void SellyApproveOrder(ExtentTest logTest, ArrayList orderIDList) throws IOException {
        try {

            for(int i=0; i < orderIDList.size(); i++){
                String orderID = (String) orderIDList.get(i);
                RequestSpecification confirmOrder = this.adminConfirmOrder();
                confirmOrder.body(new HashMap<String, Object>() {{
                    put("remarks", "Notes");
                }}).log().all();
                Response response = confirmOrder.patch("/order/" + orderID + "/approve");
                logInfo(logTest, "-----> adminApproveOrder URI: https://" + GlobalVariables.SellyAdminEnvironment + "/order/" + orderID + "/approve");
                logInfo(logTest, "-----> adminApproveOrder Response Body: " + response.getBody().asString());
            }
        } catch (Exception e) {
            log4j.error("adminApproveOrder method - ERROR: " + e);
            logException(logTest, "adminApproveOrder method - ERROR: ", e);
        }
    }

    public void verifySellyOrderStatus(ExtentTest logTest, ArrayList orderIDList, String deliveryStatus_expected, String orderStatus_expected) throws IOException {
        try {

            for(int i=0; i < orderIDList.size(); i++){
                String orderID = (String) orderIDList.get(i);
                RequestSpecification getOrderDetail = this.getOrderDetail();
                Response response = getOrderDetail.get("/order/" + orderID + "/items");

                if(response.getStatusCode() == 200) {
                    jsonBody = (JSONObject) jsonParser.parse(response.body().asString());
                    String orderStatus_actual = (String) ((JSONObject) ((JSONObject) jsonBody.get("data")).get("data")).get("status");
                    String deliveryStatus_actual = (String) ((JSONObject)((JSONObject) ((JSONObject) jsonBody.get("data")).get("data")).get("delivery")).get("status");

                    verifyExpectedAndActualResults(logTest, orderStatus_expected, orderStatus_actual);
                    verifyExpectedAndActualResults(logTest, deliveryStatus_expected, deliveryStatus_actual);
                }else {
                    logInfo(logTest, "-----> ORDER NOT FOUND" + response.getBody().asString());
                }
            }
        } catch (Exception e) {
            log4j.error("verifySellyOrderStatus method - ERROR: " + e);
            logException(logTest, "verifySellyOrderStatus method - ERROR: ", e);
        }
    }

    public void verifyIMSOrderStatus(ExtentTest logTest, JSONArray IMSIdArray, String IMSOrderStatus_expected) throws IOException {
        try {

            for(int i=0; i < IMSIdArray.size(); i++){
                JSONObject IMSOrderObject = (JSONObject) IMSIdArray.get(i);
                String IMSOrderID_expected = (String) IMSOrderObject.get("IMSOrderID");

                RequestSpecification getIMSOrderDetail = this.getIMSOrderDetail();
                Response response = getIMSOrderDetail.get("/admin/order/" + IMSOrderID_expected);

                jsonBody = (JSONObject) jsonParser.parse(response.body().asString());
                String IMSOrderID_actual = (String) ((JSONObject) jsonBody.get("data")).get("_id");
                String IMSOrderStatus_actual = (String) ((JSONObject) jsonBody.get("data")).get("status");

                verifyExpectedAndActualResults(logTest, IMSOrderID_expected, IMSOrderID_actual);
                verifyExpectedAndActualResults(logTest, IMSOrderStatus_expected, IMSOrderStatus_actual);
            }
        } catch (Exception e) {
            log4j.error("veryfyIMSOrderStatus method - ERROR: " + e);
            logException(logTest, "veryfyIMSOrderStatus method - ERROR: ", e);
        }
    }

    public ArrayList<String> createMultiOrder(ExtentTest logTest, String sellerToken, JSONObject customer) throws IOException {
        try {

            createMultiSessionOrder(logTest, sellerToken);
            deliverySessionArray = createMultiDeliverySession(logTest, sellerToken, customer);

            JSONObject payloadObject = new JSONObject();
            payloadObject.put("checkSum", "SmaAalemakwAskd");
            JSONObject customerObject = new JSONObject();
            customerObject.put("id", customer.get("customerID"));
            customerObject.put("location", customer.get("locationID"));
            payloadObject.put("customer", customerObject);
            payloadObject.put("paymentMethod", "COD");
            payloadObject.put("sessionKey", sessionKey);
            JSONArray sessionArray = new JSONArray();

            for(int i=0; i < deliverySessionArray.size(); i++){
                itemsObject = (JSONObject) deliverySessionArray.get(i);
                sessionOrderIDList.add((String) itemsObject.get("sessionOrderID"));

                JSONArray sessionDeliveries = (JSONArray) itemsObject.get("deliveries");
                JSONObject deliveryObject = (JSONObject) sessionDeliveries.get(0);
                deliverySessionIDList.add((String) deliveryObject.get("session"));

                JSONObject sessionItem = new JSONObject();
                sessionItem.put("delivery", deliverySessionIDList.get(i));
                sessionItem.put("order", sessionOrderIDList.get(i));
                sessionArray.add(sessionItem);
            }
            payloadObject.put("session", sessionArray);

            RequestSpecification createMultiOrderSpecification = this.createMultiOrderSpecification(sellerToken);
            createMultiOrderSpecification.body(payloadObject).log().all();

            Response response = createMultiOrderSpecification.post("/order/multiple");

            logInfo(logTest, "-----> createMultiOrder Request Body: " + payloadObject.toString());
            logInfo(logTest, "-----> createMultiOrder Response Body: " + response.getBody().asString());

            jsonBody = (JSONObject) jsonParser.parse(response.body().asString());
            JSONArray orderIdArray = (JSONArray) ((JSONObject) jsonBody.get("data")).get("orderIds");

            for(int i=0; i < orderIdArray.size(); i++) {
                String orderID = (String) orderIdArray.get(i);
                orderIDList.add(orderID);
            }
            return orderIDList;

        } catch (Exception e) {
            log4j.error("createMultiOrder method - ERROR: " + e);
            logException(logTest, "createMultiOrder method - ERROR: ", e);
        }
        return orderIDList;
    }

    public JSONArray createMultiDeliverySession(ExtentTest logTest, String sellerToken, JSONObject customer) throws IOException {
        try {

            RequestSpecification createMultiDeliverySessionSpec = this.createMultiDeliverySessionSpecification(sellerToken);
//            String payload = String.format(readPayloadDataFromJsonFile(GlobalVariables.create_multi_delivery_payload), "SmaAalemakwAskd", sessionIDList,  "60581310fcfe4748d00691b0", "60581310fcfe4748d00691af");
//            createMultiDeliverySessionSpec.body(payload);

            JSONObject payloadObject = new JSONObject();
            payloadObject.put("checkSum", "SmaAalemakwAskd");
            JSONObject customerObject = new JSONObject();
            customerObject.put("id", customer.get("customerID"));
            customerObject.put("location", customer.get("locationID"));
            payloadObject.put("customer", customerObject);
            payloadObject.put("sessionOrders", sessionIDList);

            createMultiDeliverySessionSpec.body(payloadObject).log().all();

            Response response = createMultiDeliverySessionSpec.post("/order/delivery/sessions-multiple");

            logInfo(logTest, "-----> createMultiDeliverySessionSpec Request Body: " + payloadObject.toString());
            logInfo(logTest, "-----> createMultiDeliverySessionSpec Response Body: " + response.getBody().asString());

            deliverySessionObject = (JSONObject) jsonParser.parse(response.body().asString());

            deliverySessionArr = (JSONArray) ((JSONObject) deliverySessionObject.get("data")).get("session");

            return deliverySessionArr;

        } catch (Exception e) {
            log4j.error("createMultiDeliverySession method - ERROR: " + e);
            logException(logTest, "createMultiDeliverySession method - ERROR: ", e);
        }
        return deliverySessionArr;
    }

    public JSONObject createCustomer(ExtentTest logTest, String sellerToken) throws IOException {
        try {

            RequestSpecification createCustomer = this.createCustomer(sellerToken);
            String payload = String.format(readPayloadDataFromJsonFile(GlobalVariables.create_customer), "Customer" + Math.floor(Math.random()*9999));
            createCustomer.body(payload).log().all();

            Response response = createCustomer.post("/customers");

            logInfo(logTest, "-----> createCustomer Request Body: " + payload);
            logInfo(logTest, "-----> createCustomer Response Body: " + response.getBody().asString());

            responseObject = (JSONObject) jsonParser.parse(response.body().asString());

            customerID = (String) ((JSONObject) ((JSONObject) responseObject.get("data")).get("customer")).get("_id");
            locationID = (String) ((JSONObject)((JSONArray) ((JSONObject) ((JSONObject) responseObject.get("data")).get("customer")).get("location")).get(0)).get("_id");
//            locationID = (String) locationObject.get("_id");
            logInfo(logTest, "-----> customerID: " + customerID);
            logInfo(logTest, "-----> locationID: " + locationID);

            customerObject = new JSONObject();
            customerObject.put("customerID", customerID);
            customerObject.put("locationID", locationID);

            return customerObject;

        } catch (Exception e) {
            log4j.error("createCustomer method - ERROR: " + e);
            logException(logTest, "createCustomer method - ERROR: ", e);
        }
        return customerObject;
    }

    public void createMultiSessionOrder(ExtentTest logTest, String sellerToken) throws IOException {
        try {

            cartArr = getItemsInCart(logTest, sellerToken);

            ArrayList<String> itemIDList = new ArrayList<String>();
//            ArrayList<String> itemMarketPriceList = new ArrayList<String>();
//            ArrayList<String> itemCodeList = new ArrayList<String>();

            for(int i=0; i < cartArr.size(); i++){
                JSONObject itemsObject = (JSONObject) cartArr.get(i);
                JSONArray itemArr = (JSONArray) itemsObject.get("items");
                for(int y=0; y < itemArr.size(); y++){
                    JSONObject itemObject = (JSONObject) itemArr.get(y);
                    String itemID = (String) itemObject.get("_id");
                    itemIDList.add(itemID);
                }

            }
            logInfo(logTest, "-----> Item ID list " + itemIDList);

            RequestSpecification createMultiSessionOrderSpec = this.createMultiSessionOrderSpecification(sellerToken);
            createMultiSessionOrderSpec.body(new HashMap<String, Object>() {{
                put("items", itemIDList);
            }}).log().all();
            Response response = createMultiSessionOrderSpec.post("/order/sessions-multiple");

            logInfo(logTest, "-----> createMultiSessionOrder Response Body: " + response.getBody().asString());

            jsonSession = (JSONObject) jsonParser.parse(response.body().asString());
            sessionKey = (String) ((JSONObject) jsonSession.get("data")).get("key");
            sessionArray = (JSONArray) ((JSONObject) jsonSession.get("data")).get("sessions");

            for(int z=0; z < sessionArray.size(); z++) {
                JSONObject sessionsObject = (JSONObject) sessionArray.get(z);
                String sessionID = (String) sessionsObject.get("_id");
                sessionIDList.add(sessionID);
            }
            logInfo(logTest, "-----> sessionID List: " + sessionIDList);
            logInfo(logTest, "-----> session Key: " + sessionKey);

        } catch (Exception e) {
            log4j.error("createMultiSessionOrder method - ERROR: " + e);
            logException(logTest, "createMultiSessionOrder method - ERROR: ", e);
        }
    }

}
