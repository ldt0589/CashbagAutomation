package resource.api.Selly;

import com.aventstack.extentreports.ExtentTest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import resource.api.common.RestAssuredConfiguration;
import resource.common.GlobalVariables;

import java.io.IOException;
import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class CartAPI extends RestAssuredConfiguration {
    private String issueIdPath = "/{issueId}";

    private JSONParser jsonParser = new JSONParser();
    private JSONObject jsonExpected = null;
    private JSONObject jsonUser = null;
    private JSONObject jsonProductDetail = null;
    private String itemObjects = null;
    private org.json.JSONObject itemObject = null;
    private String userStatistic = null;
    private String userId = null;
    private String skuID = null;
    private String skuCode = null;


    private RequestSpecification UserSpecification() {
        return given().
                baseUri("https://" + GlobalVariables.SellyEnvironment).
                relaxedHTTPSValidation();
    }

    private RequestSpecification ProductSpecification(String userToken) {
        return given().
                baseUri("https://" + GlobalVariables.SellyEnvironment).
                header("Authorization", "Bearer " + userToken).
                relaxedHTTPSValidation();
    }

//    public void deleteUserAccount(ExtentTest logTest, String email) throws IOException {
//        Response response = null;
//        logInfo(logTest, "----->Delete userAPI account: " + email);
//
//        RequestSpecification deleteUserSpec = new userAPI().UserSpecification();
//        deleteUserSpec.queryParam("email", email);
//        response = deleteUserSpec.delete("/api/user");
//
////        logInfo(logTest, "----->Delete User Account - RESPONSE: " + "<br>" + response.getBody().asString());
//
//        handleResponseStatusCode(response, 200, logTest);
//
//    }


//    public JSONObject getUserInfo(ExtentTest logTest, String email) throws IOException {
//        try {
//            RequestSpecification getUserInfoSpec = new userAPI().UserSpecification();
//            getUserInfoSpec.queryParam("email", email);
//            Response response = getUserInfoSpec.get("/api/user");
//
//            jsonUserInfo = (JSONObject) jsonParser.parse(response.body().asString());
//
//            return jsonUserInfo;
//
//            } catch (Exception e) {
//            log4j.error("getUserInfo method - ERROR: " + e);
//            logException(logTest, "getUserInfo method - ERROR: ", e);
//            }
//
//        return jsonUserInfo;
//    }

    public void getSkuID(ExtentTest logTest, String sellerToken, String productID) throws IOException {
        try {
//
//            JSONObject itemObjects = ((JSONObject) ((JSONObject) getProductDetail(logTest, sellerToken, productID).get("data")).get("items"));
//            List<T> itemList = new ArrayList<>();
            itemObjects = ((JSONObject) getProductDetail(logTest, sellerToken, productID).get("data")).get("items").toString();

            JSONArray itemArray = new JSONArray(itemObjects);
            for (int i = 0; i < itemArray.length(); i++) {
                itemObject = itemArray.getJSONObject(i);
//                String SKUID = itemObject.getString("_id");
//                String url = itemObject.getString("sku");
                logInfo(logTest, "----->SKU ID: " + itemObject.getString("_id"));
                logInfo(logTest, "----->SKU Code: " + itemObject.getString("sku"));

            }
//
////                skuID = ((JSONObject) ((JSONObject) getProductDetail(logTest, sellerToken, productID).get("data")).get("items")).get("_id").toString();
//

//
//            return itemObjects;
//
        } catch (Exception e) {
            log4j.error("getSkuID method - ERROR: " + e);
            logException(logTest, "getSkuID method - ERROR: ", e);
        }
//
//        return itemObjects;
    }

    public JSONObject getProductDetail(ExtentTest logTest, String sellerToken, String productID) throws IOException {
        try {
            RequestSpecification getProductDetailSpec = this.ProductSpecification(sellerToken);
            Response response = getProductDetailSpec.get("/products/" + productID);

            jsonProductDetail = (JSONObject) jsonParser.parse(response.body().asString());

            return jsonProductDetail;

        } catch (Exception e) {
            log4j.error("getProductDetail method - ERROR: " + e);
            logException(logTest, "getProductDetail method - ERROR: ", e);
        }

        return jsonProductDetail;
    }

    //    public JSONObject getSellerInfo(ExtentTest logTest, String userId) throws IOException {
//        try {
//            RequestSpecification getSellerInfoSpec = this.UserSpecification();
//            getSellerInfoSpec.queryParam("userId", userId);
//            Response response = getSellerInfoSpec.get("/users/token");
//
//            jsonUser = (JSONObject) jsonParser.parse(response.body().asString());
//
//            return jsonUser;
//
//            } catch (Exception e) {
//            log4j.error("getSellerInfo method - ERROR: " + e);
//            logException(logTest, "getSellerInfo method - ERROR: ", e);
//        }
//
//        return jsonUser;
//    }

//    public String getUserMeStatistic(ExtentTest logTest, String userToken, String statisticField) throws IOException {
//        try {
//            userStatistic = ((JSONObject) ((JSONObject) getUserMe(logTest, userToken).get("data")).get("user")).get("statistic").toString();
//            userStatistic = ((JSONObject) ((JSONObject) ((JSONObject) getUserMe(logTest, userToken).get("data")).get("user")).get("statistic")).get(statisticField).toString();
//            logInfo(logTest, "----->GET User Me Statistic: " + statisticField + " " + userStatistic);
//
//            return userStatistic;
//
//        } catch (Exception e) {
//            log4j.error("getUserMeStatistic method - ERROR: " + e);
//            logException(logTest, "getUserMeStatistic method - ERROR: ", e);
//        }
//
//        return userStatistic;
//    }



//    public void verifyGetIssueResponse(String issueId, Hashtable<String, String> data, Response response, ExtentTest logTest) throws IOException, ParseException {
//        logInfo(logTest, "verifyGetIssueResponse starts..........");
//        log4j.info("verifyGetIssueResponse starts..........");
//
//        if (response.getStatusCode() == 200) {
//            logPass(logTest, "API calling successful. Status code response is  200");
//
//            jsonActual = (JSONObject) jsonParser.parse(response.body().asString());
//
//            logInfo(logTest, "Verify the response is not empty");
//            verifyActualIsNotEmptyResults(logTest, jsonActual.get("key").toString());
//
//            String actualIssueId = jsonActual.get("key").toString();
//            String actualIssueType = ((JSONObject) ((JSONObject) jsonActual.get("fields")).get("issuetype")).get("name").toString();
//            String actualIssueSummary = ((JSONObject) jsonActual.get("fields")).get("summary").toString();
//
//            verifyExpectedAndActualResults(logTest, issueId, actualIssueId);
//            verifyExpectedAndActualResults(logTest, data.get("IssueType"), actualIssueType);
//            verifyExpectedAndActualResults(logTest, data.get("IssueSummary"), actualIssueSummary);
//        }
//        else
//            logFail(logTest, "API calling was unsuccessful: Status code response is " + response.getStatusCode() + " instead of 200");
//
//
//
//        logInfo(logTest, "verifyGetIssueResponse ends..........");
//        log4j.info("verifyGetIssueResponse ends..........");
//    }

}