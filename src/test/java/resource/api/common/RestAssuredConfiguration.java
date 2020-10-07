package resource.api.common;

//import com.api.automation_services.ValidationAPIServices;
import com.aventstack.extentreports.ExtentTest;
import resource.utility.Utility;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.util.HashMap;

//import static com.common.GlobalVariables.*;
import static io.restassured.RestAssured.given;

/**
 * Created by quynh.vo on 11/24/2017.
 */
public class RestAssuredConfiguration extends Utility {

    /**
     * Purpose: Get the Auth2 Bearer token to use in automation service API
     */
//    public String getAuth2Token(String userName, String passWord, String scope, ExtentTest logTest) throws IOException {
//        logInfo(logTest, "Call the oauth2 API to get Bearer token - start");
//
//        Response response =
//                given().
//                        formParam("grant_type", GRANT_TYPE).
//                        formParam("client_secret", CLIENT_SECRET).
//                        formParam("client_id", CLIENT_ID).
//                        formParam("username", userName).
//                        formParam("password", passWord).
//                        formParam("scope", scope).
//                        when().
//                        relaxedHTTPSValidation().
//                        post("https://www-" + ENVIRONMENT + ".squaretrade.com/rest/oauth/2/token").
//                        then().
//                        extract().
//                        response();
//
//        handleResponseStatusCode(response, 200, logTest);
//        String accessToken = response.path("access_token");
//        logInfo(logTest, "Call the oauth2 API to get Bearer token - end");
//        return accessToken;
//    }
//
//
//
//
//
//    /**
//     * @param res         - Response body
//     * @param pathBody    - JSOn path to the list in response
//     * @param requestSpec - Request specification, to loop the GET call until see value in response
//     * @param endpoint    - Endpoint URL
//     * @param logTest     - Extend report
//     *                    Purpose: Loop the GET call to see value in response. It take a little time to reflect records in Database, so we have to loop the call to handle this case.
//     */
//    public Response handleNullResponse(Response res, String pathBody, RequestSpecification requestSpec, String endpoint, ExtentTest logTest) throws IOException {
//        try {
//            int count = 0;
//            int sizeOfList = res.body().path(pathBody + ".size()");
//            while (sizeOfList == 0 && count < WAIT_TIME * 2) {
//                res = requestSpec.get(endpoint);
//                sizeOfList = res.body().path(pathBody + ".size()");
//                handleResponseStatusCode(res, 200, logTest);
//                sleep(2);
//                count += 2;
//            }
//
//            if (count == WAIT_TIME * 2) {
//                logFail(logTest, "Waited for data from database " + count + " seconds. But, database did not get updated.");
//            } else {
//                logInfo(logTest, "Waited for data from database " + count + " seconds.");
//            }
//        } catch (Exception e) {
//            log4j.error(" handleNullResponse - ERROR - " + e);
//            logException(logTest, " handleNullResponse - ERROR", e);
//        }
//        return res;
//    }
//
//    public Response handleGetResponse(Response res, RequestSpecification requestSpec, String endpoint, String pathBody, ExtentTest logTest) throws IOException {
//        try {
//            HashMap data;
//            int statusCode = res.getStatusCode();
//            int size = 0;
//            int repeatTime = 0;
//            while ((statusCode != 200 || size == 0) && repeatTime < WAIT_TIME) {
//                res = requestSpec.get(endpoint);
//                statusCode = res.getStatusCode();
//                data = res.then().extract().response().path(pathBody);
//                if (data != null) size = data.size();
//                repeatTime += 1;
//                sleep(3);
//            }
//
//            if (repeatTime == WAIT_TIME) {
//                logFail(logTest, "Waiting for database update - FAILED");
//            } else {
//                logInfo(logTest, "Waiting for database update - PASSED");
//            }
//        } catch (Exception e) {
//            log4j.error("handleGetResponse - ERROR - " + e);
//            logException(logTest, "handleGetResponse - ERROR", e);
//        }
//
//        return res;
//    }
//
//    public Response handleGetResponseWithParam(Response response, String endpoint, String param, String pathBody, ExtentTest logTest) throws IOException {
//        try {
//            HashMap data;
//            int statusCode = response.getStatusCode();
//            int size = 0;
//            int repeatTime = 0;
//            while ((statusCode != 200 || size == 0) && repeatTime < WAIT_TIME) {
//                RequestSpecification requestSpec = new ValidationAPIServices().validationAPIRequestSpecification();
//                response = requestSpec.get(endpoint, param);
//                statusCode = response.getStatusCode();
//                data = response.then().extract().response().path(pathBody);
//                if (data != null) size = data.size();
//                repeatTime += 1;
//                sleep(3);
//            }
//
//            if (repeatTime == WAIT_TIME) {
//                logFail(logTest, "Waiting for database update - FAILED");
//            } else {
//                logInfo(logTest, "Waiting for database update - PASSED");
//            }
//        } catch (Exception e) {
//            log4j.error("handleGetResponse - ERROR - " + e);
//            logException(logTest, "handleGetResponse - ERROR", e);
//        }
//
//        return response;
//    }

    /**
     * @param res        - Response body
     * @param statusCode - expect status code
     * @param logTest    - Extend report
     *                   Purpose: Check status code response matches with expectation or not
     */
    public void handleResponseStatusCode(Response res, int statusCode, ExtentTest logTest) throws IOException {
        try {
            if (res.getStatusCode() == statusCode)
                logInfo(logTest, "API calling successful. Status code response is " + res.getStatusCode());
            else
                logFail(logTest, "API calling was unsuccessful: Status code response is " + res.getStatusCode() + " instead of " + statusCode);
        } catch (Exception e) {
            log4j.error(" handleResponseStatusCode - ERROR - " + e);
            logException(logTest, " handleResponseStatusCode - ERROR", e);
        }
    }
}
