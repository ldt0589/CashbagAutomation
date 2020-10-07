package resource.api;

import com.aventstack.extentreports.ExtentTest;
import resource.utility.Utility;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import resource.common.GlobalVariables;

import java.io.IOException;
import java.util.Hashtable;

import static io.restassured.RestAssured.given;

public class IssueApi extends Utility {
    private String issueIdPath = "/{issueId}";

    private JSONParser jsonParser = new JSONParser();
    private JSONObject jsonExpected = null;
    private JSONObject jsonActual = null;

    private RequestSpecification IssueSpecification() {
        return given().
                baseUri("http://" + GlobalVariables.ENVIRONMENT + ".atlassian.net").
                basePath("/rest/api/2/issue").
                relaxedHTTPSValidation().
                contentType("application/json").
                auth().preemptive().basic("tl31633@gmail.com", "EYhimdfdOOVEbLk5iRTz952F");
    }

    public Response getIssueAPI(ExtentTest logTest, String issueId) {
        logInfo(logTest, "callgetIssueAPI starts..........");
        log4j.info("callgetIssueAPI starts..........");

        RequestSpecification requestSpecification = IssueSpecification();
//        requestSpecification.pathParam("issueId", issueId);
        Response response = requestSpecification.get(issueId);

        log4j.info("Call Get Issue API - response body: " + response.getBody().asString());
        logInfo(logTest, "Call Get Issue API - response body: <br>" + response.getBody().asString());

        logInfo(logTest, "getIssueAPI ends..........");
        log4j.info("getIssueAPI ends..........");

        return response;
    }

    public void verifyGetIssueResponse(String issueId, Hashtable<String, String> data, Response response, ExtentTest logTest) throws IOException, ParseException {
        logInfo(logTest, "verifyGetIssueResponse starts..........");
        log4j.info("verifyGetIssueResponse starts..........");

        if (response.getStatusCode() == 200) {
            logPass(logTest, "API calling successful. Status code response is  200");

            jsonActual = (JSONObject) jsonParser.parse(response.body().asString());

            logInfo(logTest, "Verify the response is not empty");
            verifyActualIsNotEmptyResults(logTest, jsonActual.get("key").toString());

            String actualIssueId = jsonActual.get("key").toString();
            String actualIssueType = ((JSONObject) ((JSONObject) jsonActual.get("fields")).get("issuetype")).get("name").toString();
            String actualIssueSummary = ((JSONObject) jsonActual.get("fields")).get("summary").toString();

            verifyExpectedAndActualResults(logTest, issueId, actualIssueId);
            verifyExpectedAndActualResults(logTest, data.get("IssueType"), actualIssueType);
            verifyExpectedAndActualResults(logTest, data.get("IssueSummary"), actualIssueSummary);
        }
        else
            logFail(logTest, "API calling was unsuccessful: Status code response is " + response.getStatusCode() + " instead of 200");



        logInfo(logTest, "verifyGetIssueResponse ends..........");
        log4j.info("verifyGetIssueResponse ends..........");
    }

}