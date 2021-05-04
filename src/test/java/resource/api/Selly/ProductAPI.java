package resource.api.Selly;

import com.aventstack.extentreports.ExtentTest;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import resource.common.GlobalVariables;
import resource.common.TestBase;

import java.io.FileWriter;
import java.io.IOException;

import static io.restassured.RestAssured.given;

public class ProductAPI extends TestBase {
    private String issueIdPath = "/{issueId}";

    private JSONParser jsonParser = new JSONParser();
    private RestAssuredConfig restConfig = new RestAssuredConfig();
    private JSONObject jsonExpected = null;
    private JSONObject jsonUser = null;
    private JSONObject jsonProductDetail = null;
    private JSONObject ProductDetailObject = null;
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

    private RequestSpecification getProductListSpec(String adminToken) {
        return given().
                baseUri("https://" + GlobalVariables.SellyAdminEnvironment).
                header("Authorization", "Bearer " + adminToken).
                relaxedHTTPSValidation();

    }

    public JSONArray VerifySKUPrice(ExtentTest logTest, String sellerToken, String SellyAdminToken) throws IOException {
        try {
            RequestSpecification getProductList = this.getProductListSpec(SellyAdminToken);
            Response ProductListResponse = getProductList.get("/products");

            logInfo(logTest, "-----> get ProductID Array Response Body: " + ProductListResponse.getBody().asString());

            JSONObject ProductListObject = (JSONObject) jsonParser.parse(ProductListResponse.body().asString());
            JSONArray productListArray = (JSONArray)((JSONObject) ProductListObject.get("data")).get("products");

            for(int i=0; i<productListArray.size(); i++){
                String ProductID = (String) ((JSONObject) productListArray.get(i)).get("_id");
                JSONObject pricePercent = (JSONObject) ((JSONObject) productListArray.get(i)).get("pricePercent");
                String SupplierCommission = String.valueOf(pricePercent.get("supplier"));
                logInfo(logTest, "-----> Product ID: " + ProductID);
                logInfo(logTest, "-----> Supplier Commission Percent: " + SupplierCommission);

                RequestSpecification getProductDetail = this.ItemSpecification(sellerToken);
                Response ProductDetailResponse = getProductDetail.get("/products" + ProductID);


                JSONObject ProductDetailObject = (JSONObject) jsonParser.parse(ProductDetailResponse.body().asString());
                JSONArray ItemSKUArray = (JSONArray)((JSONObject)((JSONObject) ProductDetailObject.get("data")).get("product")).get("items");

                for(int z=0; z<ItemSKUArray.size(); z++){
                    String ItemID = (String) ((JSONObject) ItemSKUArray.get(z)).get("_id");
                    String ItemSKU = (String) ((JSONObject) ItemSKUArray.get(z)).get("sku");
                    Float ItemMarketPrice = (Float)((JSONObject) ((JSONObject) ItemSKUArray.get(z)).get("price")).get("market");

                    Float ItemBasePrice = (Float)((JSONObject) ((JSONObject) ItemSKUArray.get(z)).get("price")).get("base");
                    Float ItemMinPrice = (Float)((JSONObject) ((JSONObject) ItemSKUArray.get(z)).get("price")).get("minimum");
                    Float ItemMaxPrice = (Float)((JSONObject) ((JSONObject) ItemSKUArray.get(z)).get("price")).get("maximum");
                    Float ItemProfitPrice = (Float)((JSONObject) ((JSONObject) ItemSKUArray.get(z)).get("price")).get("profit");
                    Float ItemSupplierPrice = (Float)((JSONObject) ((JSONObject) ItemSKUArray.get(z)).get("price")).get("supplier");

                    Float ItemProfitPrice_Expected = ItemMarketPrice*Long.parseLong(SupplierCommission)/100*GlobalVariables.SellerSharingCommission/100;
                    Float ItemBasePrice_Expected = ItemMarketPrice-ItemProfitPrice_Expected;
                    Float ItemSupplierPrice_Expected = ItemMarketPrice-(100-Long.parseLong(SupplierCommission))/100;

                    logInfo(logTest, "-----> Verify Item Base Price: ");
                    verifyExpectedAndActualNumber(logTest, ItemBasePrice, ItemBasePrice_Expected);

                    logInfo(logTest, "-----> Verify Item Min Price: ");
                    verifyExpectedAndActualNumber(logTest, ItemMinPrice, ItemBasePrice_Expected);

                    logInfo(logTest, "-----> Verify Item Max Price: ");
                    verifyExpectedAndActualNumber(logTest, ItemMaxPrice, ItemMarketPrice);

                    logInfo(logTest, "-----> Verify Item Profit Price: ");
                    verifyExpectedAndActualNumber(logTest, ItemProfitPrice, ItemProfitPrice_Expected);

                    logInfo(logTest, "-----> Verify Item Supplier Price: ");
                    verifyExpectedAndActualNumber(logTest, ItemSupplierPrice, ItemSupplierPrice_Expected);
                }
            }
            return itemArray;

        } catch (Exception e) {
            log4j.error("getProductDetail method - ERROR: " + e);
            logException(logTest, "getProductDetail method - ERROR: ", e);
        }

        return itemArray;
    }

}