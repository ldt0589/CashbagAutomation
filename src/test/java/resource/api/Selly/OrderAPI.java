package resource.api.Selly;

import com.aventstack.extentreports.ExtentTest;
import groovy.json.JsonParser;
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
    private JSONArray sessionArray = null;
    private JSONObject jsonSession = null;
    private JSONParser jsonParser = new JSONParser();;
    public static ArrayList<String> sessionIDList = new ArrayList<String>();
    public static String sessionKey = null;

    private RequestSpecification createMultiSessionOrderSpecification(String userToken) {
        return given().
                baseUri("https://" + GlobalVariables.SellyEnvironment).
                header("Authorization", "Bearer " + userToken).
                header("Content-Type","application/json").
                contentType("application/json").
                relaxedHTTPSValidation();
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
