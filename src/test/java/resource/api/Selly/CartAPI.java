package resource.api.Selly;

import com.aventstack.extentreports.ExtentTest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import resource.api.common.RestAssuredConfiguration;
import resource.common.GlobalVariables;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class CartAPI extends RestAssuredConfiguration {
    private String issueIdPath = "/{issueId}";

    private JSONParser jsonParser = new JSONParser();
    private JSONObject jsonExpected = null;
    private JSONObject jsonUser = null;
    private JSONObject jsonProductDetail = null;
    private JSONObject jsonCartItemsDetail = null;
    private JSONObject response = null;
    private JSONArray itemArray = null;
    private String userStatistic = null;
    private String userId = null;
    private String skuID = null;
    private String skuCode = null;
    private String inventoryID = null;
    private String inventoryName = null;
    private int i = 0;
    ArrayList<String> itemIDList = new ArrayList<String>();
    ArrayList<String> itemMarketPriceList = new ArrayList<String>();
    ArrayList<String> itemCodeList = new ArrayList<String>();


    private RequestSpecification ItemSpecification(String userToken) {
        return given().
                baseUri("https://" + GlobalVariables.SellyEnvironment).
                header("Authorization", "Bearer " + userToken).
                relaxedHTTPSValidation();
    }

    private RequestSpecification addToCartSpecification(String userToken) {
        return given().
                baseUri("https://" + GlobalVariables.SellyEnvironment).
                header("Authorization", "Bearer " + userToken).
                header("Content-Type","application/json").
                contentType("application/json").
                relaxedHTTPSValidation();
    }

//    private RequestSpecification getItemsCartSpecification(String userToken) {
//        return given().
//                baseUri("https://" + GlobalVariables.SellyEnvironment).
//                header("Authorization", "Bearer " + userToken).
//                relaxedHTTPSValidation();
//    }

    public void clearItemsInCart(ExtentTest logTest, String sellerToken) throws IOException {
        try {

            itemArray = getItemsInCart(logTest, sellerToken);



//            return itemArray;

        }catch (Exception e) {
            log4j.error("clearItemsInCart method - ERROR: " + e);
            logException(logTest, "clearItemsInCart method - ERROR: ", e);
        }
//        return itemArray;
    }

    public JSONArray getItemsInCart(ExtentTest logTest, String sellerToken) throws IOException {
        try {
            RequestSpecification getCartItemlSpec = this.ItemSpecification(sellerToken);
            Response response = getCartItemlSpec.get("/cart/items");

            jsonCartItemsDetail = (JSONObject) jsonParser.parse(response.body().asString());

            itemArray = (JSONArray) ((JSONObject) jsonCartItemsDetail.get("data")).get("cart");

            return itemArray;

            }catch (Exception e) {
            log4j.error("getItemsInCart method - ERROR: " + e);
            logException(logTest, "getItemsInCart method - ERROR: ", e);
        }
        return itemArray;
    }

    public void addItemIntoCart(ExtentTest logTest, String sellerToken, String productID) throws IOException {
        try {

            itemArray = getItemsInProduct(logTest, sellerToken, productID);

            for(int i=0; i < itemArray.size(); i++){
                JSONObject itemObject = (JSONObject)itemArray.get(i);
                String skuID = (String) itemObject.get("_id");
                String skuCode = (String) itemObject.get("sku");
                inventoryID = ((JSONObject) ((JSONObject) itemObject.get("info")).get("inventory")).get("id").toString();
                inventoryName = ((JSONObject)((JSONObject) itemObject.get("info")).get("inventory")).get("name").toString();
                String skuMarketPrice = ((JSONObject) itemObject.get("price")).get("market").toString();
                itemIDList.add(skuID);
                itemMarketPriceList.add(skuMarketPrice);
                itemCodeList.add(skuCode);
            }
            logInfo(logTest, "-----> INVENTORY ID: " + inventoryID + " && INVENTORY NAME: " + inventoryName + " <-------");
            for(; i < itemIDList.size(); i++){
                logInfo(logTest, "-----> " + i + ". SKU Code: " + itemCodeList.get(i));
                logInfo(logTest, "-----> " + i + ". SKU ID: " + itemIDList.get(i));
                logInfo(logTest, "-----> " + i + ". Market Price: " + itemMarketPriceList.get(i) + " đ");

                RequestSpecification addItemToCartSpec = this.addToCartSpecification(sellerToken);

                addItemToCartSpec.body(new HashMap<String, Object>() {{
                    put("priceSell", Integer.parseInt(itemMarketPriceList.get(i)));
                    put("quantity", 1);
                    put("skuId", itemIDList.get(i));
                }}).log().all();

                Response response = addItemToCartSpec.post("/cart/items");
                handleResponseStatusCode(response, 200, logTest);

            }

        } catch (Exception e) {
            log4j.error("addItemIntoCart method - ERROR: " + e);
            logException(logTest, "addItemIntoCart method - ERROR: ", e);
        }
    }

    public JSONArray getItemsInProduct(ExtentTest logTest, String sellerToken, String productID) throws IOException {
        try {
            RequestSpecification getProductDetailSpec = this.ItemSpecification(sellerToken);
            Response response = getProductDetailSpec.get("/products/" + productID);

            jsonProductDetail = (JSONObject) jsonParser.parse(response.body().asString());

            itemArray = (JSONArray)((JSONObject)((JSONObject) jsonProductDetail.get("data")).get("product")).get("items");

            return itemArray;

        } catch (Exception e) {
            log4j.error("getProductDetail method - ERROR: " + e);
            logException(logTest, "getProductDetail method - ERROR: ", e);
        }

        return itemArray;
    }

}