package resource.api;

import com.aventstack.extentreports.ExtentTest;
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
    private JSONObject jsonActual = null;

    private RequestSpecification UserSpecification() {
        return given().
                baseUri("https://" + GlobalVariables.ENVIRONMENT).
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


    public HashMap getUserInfo(ExtentTest logTest, String email) throws IOException {
        HashMap userInfo = null;
        try {
            RequestSpecification getUserInfoSpec = new userAPI().UserSpecification();
            getUserInfoSpec.queryParam("email", email);
            Response response = getUserInfoSpec.get("/api/user");

            logInfo(logTest, "----->GET User Info - RESPONSE: " + "<br>" + response.getBody().asString());

            userInfo = response.then().extract().response().path("");
            return userInfo;

        } catch (Exception e) {
            log4j.error("getUserInfo method - ERROR: " + e);
            logException(logTest, "getUserInfo method - ERROR: ", e);
        }

        return userInfo;
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