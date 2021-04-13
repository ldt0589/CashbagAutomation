package resource.api.Selly;

import com.aventstack.extentreports.ExtentTest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import resource.common.GlobalVariables;
import resource.common.TestBase;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class UserAPI extends TestBase {
    private String issueIdPath = "/{issueId}";

    private JSONParser jsonParser = new JSONParser();
    private JSONObject jsonExpected = null;
    private JSONObject jsonUser = null;
    private JSONObject jsonAdmin = null;
    private JSONObject jsonUserMeInfo = null;
    private String userStatistic = null;
    private String userId = null;
    private String userToken = null;
    private String adminToken = null;

    private RequestSpecification UserSpecification() {
        return given().
                baseUri("https://" + GlobalVariables.SellyEnvironment).
                relaxedHTTPSValidation();
    }

    private RequestSpecification AdminSpecification() {
        return given().
                baseUri("https://" + GlobalVariables.SellyAdminEnvironment).
                relaxedHTTPSValidation();
    }


//    private RequestSpecification UserMeSpecification(String userToken) {
//        return given().
//                baseUri("https://" + GlobalVariables.API_ENVIRONMENT).
//                header("version","1.2").
//                header("Authorization", "Bearer " + userToken).
//                relaxedHTTPSValidation();
//    }

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

    public String getSellerToken(ExtentTest logTest, String userId) throws IOException {
        try {

            userToken = ((JSONObject) getSellerInfo(logTest, userId).get("data")).get("token").toString();

            logInfo(logTest, "----->GET Seller Token: " + userToken);

            return userToken;

        } catch (Exception e) {
            log4j.error("getSellerToken method - ERROR: " + e);
            logException(logTest, "getSellerToken method - ERROR: ", e);
        }

        return userToken;
    }

    public String getAdminToken(ExtentTest logTest, String adminID) throws IOException {
        try {

            adminToken = ((JSONObject) getAdminInfo(logTest, adminID).get("data")).get("token").toString();

            logInfo(logTest, "----->GET SELLY Admin Token: " + adminToken);

            return adminToken;

        } catch (Exception e) {
            log4j.error("getAdminToken method - ERROR: " + e);
            logException(logTest, "getAdminToken method - ERROR: ", e);
        }

        return adminToken;
    }

    public JSONObject getAdminInfo(ExtentTest logTest, String adminID) throws IOException {
        try {
            RequestSpecification getAdminInfoSpec = this.AdminSpecification();
            getAdminInfoSpec.queryParam("staffId", adminID);
            Response response = getAdminInfoSpec.get("/staffs/token");

            jsonAdmin = (JSONObject) jsonParser.parse(response.body().asString());

            return jsonAdmin;

        } catch (Exception e) {
            log4j.error("getAdminInfo method - ERROR: " + e);
            logException(logTest, "getAdminInfo method - ERROR: ", e);
        }

        return jsonAdmin;
    }

    public JSONObject getSellerInfo(ExtentTest logTest, String userId) throws IOException {
        try {
            RequestSpecification getSellerInfoSpec = this.UserSpecification();
            getSellerInfoSpec.queryParam("userId", userId);
            Response response = getSellerInfoSpec.get("/users/token");

            jsonUser = (JSONObject) jsonParser.parse(response.body().asString());

            return jsonUser;

            } catch (Exception e) {
            log4j.error("getSellerInfo method - ERROR: " + e);
            logException(logTest, "getSellerInfo method - ERROR: ", e);
        }

        return jsonUser;
    }

}