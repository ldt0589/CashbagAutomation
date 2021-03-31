package resource.api.Selly;

import com.aventstack.extentreports.ExtentTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.tools.ant.types.selectors.SelectSelector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import resource.api.common.RestAssuredConfiguration.*;
import resource.common.GlobalVariables;
import resource.common.TestBase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class CartAPI extends TestBase {
    private String issueIdPath = "/{issueId}";

    private JSONParser jsonParser = new JSONParser();
    private RestAssuredConfig restConfig = new RestAssuredConfig();
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
    private int y = 0;
    private static FileWriter file;

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
//                log().ifValidationFails().
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
            ArrayList<String> inventoryList = new ArrayList<String>();

            if (itemArray.size() < 1)
                logInfo(logTest, "-----> CART IS EMPTY -----");
            else {
                for(i = 0; i < itemArray.size(); i++){
                    JSONObject inventoryObject = (JSONObject) itemArray.get(i);
                    inventoryID = ((JSONObject) inventoryObject.get("inventory")).get("id").toString();
                    inventoryList.add(inventoryID);
                }

                for(y = 0; y < itemArray.size(); y++){
                    logInfo(logTest, "-----> " + y + ". Delete Inventory ID: " + inventoryList.get(y));
                    RequestSpecification clearItemsSpec = this.ItemSpecification(sellerToken);
                    clearItemsSpec.queryParam("inventoryID", inventoryList.get(y));
                    Response response = clearItemsSpec.delete("/cart/items");
                    clearItemsSpec.log().all();
                    logInfo(logTest, "-----> clearItemsInCart Response Body: " + response.getBody().asString());
                }
            }


        }catch (Exception e) {
            log4j.error("clearItemsInCart method - ERROR: " + e);
            logException(logTest, "clearItemsInCart method - ERROR: ", e);
        }

    }



    public JSONArray getItemsInCart(ExtentTest logTest, String sellerToken) throws IOException {
        try {
            RequestSpecification getCartItemlSpec = this.ItemSpecification(sellerToken);
            Response response = getCartItemlSpec.get("/cart/items");

            logInfo(logTest, "-----> getItemsInCart Response Body: " + response.getBody().asString());

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

            ArrayList<String> itemIDList = new ArrayList<String>();
            ArrayList<String> itemMarketPriceList = new ArrayList<String>();
            ArrayList<String> itemCodeList = new ArrayList<String>();

            itemArray = getItemsInProduct(logTest, sellerToken, productID);

            for(i=0; i < itemArray.size(); i++){
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
            RequestSpecification addItemToCartSpec = this.addToCartSpecification(sellerToken);
            for(y = 0; y < itemIDList.size(); y++){
                logInfo(logTest, "-----> " + y + ". SKU Code: " + itemCodeList.get(y));
                logInfo(logTest, "-----> " + y + ". SKU ID: " + itemIDList.get(y));
                logInfo(logTest, "-----> " + y + ". Market Price: " + itemMarketPriceList.get(y) + " đ");

                addItemToCartSpec.body(new HashMap<String, Object>() {{
                    put("priceSell", Integer.parseInt(itemMarketPriceList.get(y)));
                    put("quantity", 1);
                    put("skuId", itemIDList.get(y));
                }}).log().all();

                Response response = addItemToCartSpec.post("/cart/items");
                logInfo(logTest, "-----> addItemIntoCart Response Body: " + response.getBody().asString());

            }

        } catch (Exception e) {
            log4j.error("addItemIntoCart method - ERROR: " + e);
            logException(logTest, "addItemIntoCart method - ERROR: ", e);
        }
    }

    public void writeJsonFile(ExtentTest logTest, JSONObject fileObject, String filePath) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), fileObject);
            logInfo(logTest, "Successfully Wrote JSON Object to File...");
            logInfo(logTest, "JSON Object: " + fileObject);

        } catch (Exception e) {
            log4j.error("writeJsonFile method - ERROR: " + e);
            logException(logTest, "writeJsonFile method - ERROR: ", e);
        }
    }

    public JSONArray getItemsInProduct(ExtentTest logTest, String sellerToken, String productID) throws IOException {
        try {
            RequestSpecification getProductDetailSpec = this.ItemSpecification(sellerToken);
            Response response = getProductDetailSpec.get("/products/" + productID);

            logInfo(logTest, "-----> getItemsInProduct Response Body: " + response.getBody().asString());

            jsonProductDetail = (JSONObject) jsonParser.parse(response.body().asString());

            JSONObject productdetailObject = (JSONObject)((JSONObject) jsonProductDetail.get("data")).get("product");
            String productInventory = (String) ((JSONObject)((JSONObject) productdetailObject.get("info")).get("inventory")).get("name");
            itemArray = (JSONArray)((JSONObject)((JSONObject) jsonProductDetail.get("data")).get("product")).get("items");

            switch (productInventory) {
                case "Selly":
                    writeJsonFile(logTest, productdetailObject, GlobalVariables.Product_In_Selly_Inventory_Json_file);
                    break;
                case "Zody":
                    writeJsonFile(logTest, productdetailObject, GlobalVariables.Product_In_Zody_Inventory_Json_file);
                    break;
                case "Cashbag":
                    writeJsonFile(logTest, productdetailObject, GlobalVariables.Product_In_Cashbag_Inventory_Json_file);
                    break;
                case "Unibag":
                    writeJsonFile(logTest, productdetailObject, GlobalVariables.Product_In_Unibag_Inventory_Json_file);
                    break;
                case "DaNang":
                    writeJsonFile(logTest, productdetailObject, GlobalVariables.Product_In_DaNang_Inventory_Json_file);
                    break;
                case "HCM":
                    writeJsonFile(logTest, productdetailObject, GlobalVariables.Product_In_HCM_Inventory_Json_file);
                    break;
                default:
                    logInfo(logTest, "-----> No Inventory here");
            }

            writeJsonFile(logTest, productdetailObject, GlobalVariables.Product_In_Selly_Inventory_Json_file);

            return itemArray;

        } catch (Exception e) {
            log4j.error("getProductDetail method - ERROR: " + e);
            logException(logTest, "getProductDetail method - ERROR: ", e);
        }

        return itemArray;
    }

}