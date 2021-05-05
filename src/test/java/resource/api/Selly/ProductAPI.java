package resource.api.Selly;

import com.aventstack.extentreports.ExtentTest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import resource.common.GlobalVariables;
import resource.common.TestBase;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class ProductAPI extends TestBase {
    private String issueIdPath = "/{issueId}";

    private JSONParser jsonParser = new JSONParser();
    private JSONArray itemArray = null;

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

    public JSONArray VerifySKUPrice(ExtentTest logTest, String sellerToken, String SellyAdminToken, int limitItem) throws IOException {
        try {
            RequestSpecification getProductList = this.getProductListSpec(SellyAdminToken);
            getProductList.queryParam("limit", limitItem);
            Response ProductListResponse = getProductList.get("/products");

            JSONObject ProductListObject = (JSONObject) jsonParser.parse(ProductListResponse.body().asString());
            JSONArray productListArray = (JSONArray)((JSONObject) ProductListObject.get("data")).get("products");

            for(int i=0; i<productListArray.size(); i++){
                String ProductID = (String) ((JSONObject) productListArray.get(i)).get("_id");
                JSONObject pricePercent = (JSONObject) ((JSONObject) productListArray.get(i)).get("pricePercent");
                String SupplierCommission = String.valueOf(pricePercent.get("supplier"));
                logInfo(logTest, "-----> PRODUCT " + i + "\nProduct ID: " + ProductID);
                logInfo(logTest, "-----> Supplier Commission Percent: " + SupplierCommission);

                RequestSpecification getProductDetail = this.ItemSpecification(sellerToken);
                Response ProductDetailResponse = getProductDetail.get("/products/" + ProductID);

                if(ProductDetailResponse.getStatusCode() == 200){
                    JSONObject ProductDetailObject = (JSONObject) jsonParser.parse(ProductDetailResponse.body().asString());
                    JSONArray ItemSKUArray = (JSONArray)((JSONObject)((JSONObject) ProductDetailObject.get("data")).get("product")).get("items");


                    for (int z = 0; z < ItemSKUArray.size(); z++) {
                        String ItemID = (String) ((JSONObject) ItemSKUArray.get(z)).get("_id");
                        String ItemSKU = (String) ((JSONObject) ItemSKUArray.get(z)).get("sku");

                        String ItemMarketPrice = String.valueOf(((JSONObject) ((JSONObject) ItemSKUArray.get(z)).get("price")).get("market"));

                        logInfo(logTest, "-----> SKU " + z + "\n SKU ID: " + ItemID);
                        logInfo(logTest, "-----> SKU: " + ItemSKU);
                        logInfo(logTest, "-----> Market Price: " + ItemMarketPrice);

                        String ItemBasePrice = String.valueOf(((JSONObject) ((JSONObject) ItemSKUArray.get(z)).get("price")).get("base"));
                        String ItemMinPrice = String.valueOf(((JSONObject) ((JSONObject) ItemSKUArray.get(z)).get("price")).get("minimum"));
                        String ItemMaxPrice = String.valueOf(((JSONObject) ((JSONObject) ItemSKUArray.get(z)).get("price")).get("maximum"));
                        String ItemProfitPrice = String.valueOf(((JSONObject) ((JSONObject) ItemSKUArray.get(z)).get("price")).get("profit"));
                        String ItemSupplierPrice = String.valueOf(((JSONObject) ((JSONObject) ItemSKUArray.get(z)).get("price")).get("supplier"));

                        Float ItemProfitPrice_Expected = Float.parseFloat(ItemMarketPrice) * Float.parseFloat(SupplierCommission) / 100 * GlobalVariables.SellerSharingCommission / 100;
                        Float ItemBasePrice_Expected = Float.parseFloat(ItemMarketPrice) - ItemProfitPrice_Expected;
                        Float ItemSupplierPrice_Expected = Float.parseFloat(ItemMarketPrice) * ((100 - Float.parseFloat(SupplierCommission)) / 100);

                        logInfo(logTest, "-----> Verify Item Base Price: ");
                        verifyExpectedAndActualNumber(logTest, Float.parseFloat(ItemBasePrice), ItemBasePrice_Expected);

                        logInfo(logTest, "-----> Verify Item Min Price: ");
                        verifyExpectedAndActualNumber(logTest, Float.parseFloat(ItemMinPrice), ItemBasePrice_Expected);

                        logInfo(logTest, "-----> Verify Item Max Price: ");
                        verifyExpectedAndActualNumber(logTest, Float.parseFloat(ItemMaxPrice), Float.parseFloat(ItemMarketPrice));

                        logInfo(logTest, "-----> Verify Item Profit Price: ");
                        verifyExpectedAndActualNumber(logTest, Float.parseFloat(ItemProfitPrice), ItemProfitPrice_Expected);

                        logInfo(logTest, "-----> Verify Item Supplier Price: ");
                        verifyExpectedAndActualNumber(logTest, Float.parseFloat(ItemSupplierPrice), ItemSupplierPrice_Expected);
                    }

                }else
                    logInfo(logTest, "-----> PRODUCT NOT FOUND ");

            }
            return itemArray;

        } catch (Exception e) {
            log4j.error("getProductDetail method - ERROR: " + e);
            logException(logTest, "getProductDetail method - ERROR: ", e);
        }

        return itemArray;
    }

}