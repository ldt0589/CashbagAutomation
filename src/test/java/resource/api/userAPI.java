package resource.api;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.model.AbstractStructure;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import resource.common.GlobalVariables;
import resource.utility.Utility;
import resource.api.common.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;

import static io.restassured.RestAssured.given;

public class userAPI extends RestAssuredConfiguration {
    private String issueIdPath = "/{issueId}";

    private JSONParser jsonParser = new JSONParser();
    private JSONObject jsonExpected = null;
    private JSONObject jsonUserInfo = null;
    private JSONObject jsonUserMeInfo = null;
    private String userStatistic = null;
    private String userId = null;
    private String userToken = null;

    private RequestSpecification UserSpecification() {
        return given().
                baseUri("https://" + GlobalVariables.ENVIRONMENT).
                relaxedHTTPSValidation();
    }

    private RequestSpecification UserMeSpecification(String userToken) {
        return given().
                baseUri("https://" + GlobalVariables.API_ENVIRONMENT).
                header("version","1.2").
                header("Authorization", "Bearer " + userToken).
                relaxedHTTPSValidation();
    }

    public void deleteUserAccount(ExtentTest logTest, String email) throws IOException {
        Response response = null;
        logInfo(logTest, "----->Delete userAPI account: " + email);

        RequestSpecification deleteUserSpec = new userAPI().UserSpecification();
        deleteUserSpec.queryParam("email", email);
        response = deleteUserSpec.delete("/api/user");

//        logInfo(logTest, "----->Delete User Account - RESPONSE: " + "<br>" + response.getBody().asString());

        handleResponseStatusCode(response, 200, logTest);

    }


    public JSONObject getUserInfo(ExtentTest logTest, String email) throws IOException {
        try {
            RequestSpecification getUserInfoSpec = new userAPI().UserSpecification();
            getUserInfoSpec.queryParam("email", email);
            Response response = getUserInfoSpec.get("/api/user");

            jsonUserInfo = (JSONObject) jsonParser.parse(response.body().asString());

            return jsonUserInfo;

            } catch (Exception e) {
            log4j.error("getUserInfo method - ERROR: " + e);
            logException(logTest, "getUserInfo method - ERROR: ", e);
            }

        return jsonUserInfo;
    }

    public String getUserId(ExtentTest logTest, String email) throws IOException {
        try {

            userId = ((JSONObject) ((JSONObject) getUserInfo(logTest, email).get("data")).get("user")).get("_id").toString();

            logInfo(logTest, "----->GET User ID: " + userId);

            return userId;

        } catch (Exception e) {
            log4j.error("getUserId method - ERROR: " + e);
            logException(logTest, "getUserId method - ERROR: ", e);
        }

        return userId;
    }

    public String getUserToken(ExtentTest logTest, String email) throws IOException {
        try {

            userToken = ((JSONObject) getUserInfo(logTest, email).get("data")).get("token").toString();

            logInfo(logTest, "----->GET User Token: " + userToken);

            return userToken;

        } catch (Exception e) {
            log4j.error("getUserToken method - ERROR: " + e);
            logException(logTest, "getUserToken method - ERROR: ", e);
        }

        return userToken;
    }

    public JSONObject getUserMe(ExtentTest logTest, String userToken) throws IOException {
        try {
            RequestSpecification getUserMeInfoSpec = new userAPI().UserMeSpecification(userToken);
            Response response = getUserMeInfoSpec.get("/me");

            jsonUserMeInfo = (JSONObject) jsonParser.parse(response.body().asString());

            return jsonUserMeInfo;

        } catch (Exception e) {
            log4j.error("getUserMe method - ERROR: " + e);
            logException(logTest, "getUserMe method - ERROR: ", e);
        }

        return jsonUserMeInfo;
    }

    public String getUserMeStatistic(ExtentTest logTest, String userToken, String statisticField) throws IOException {
        try {
            userStatistic = ((JSONObject) ((JSONObject) getUserMe(logTest, userToken).get("data")).get("user")).get("statistic").toString();
            userStatistic = ((JSONObject) ((JSONObject) ((JSONObject) getUserMe(logTest, userToken).get("data")).get("user")).get("statistic")).get(statisticField).toString();
            logInfo(logTest, "----->GET User Me Statistic: " + statisticField + " " + userStatistic);

            return userStatistic;

        } catch (Exception e) {
            log4j.error("getUserMeStatistic method - ERROR: " + e);
            logException(logTest, "getUserMeStatistic method - ERROR: ", e);
        }

        return userStatistic;
    }



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