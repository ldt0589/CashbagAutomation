package feature.SELLY;

import org.testng.annotations.Test;
import resource.api.Selly.ProductAPI;
import resource.api.Selly.UserAPI;
import resource.common.GlobalVariables;
import resource.common.TestBase;

import java.io.IOException;
import java.util.Hashtable;

public class Selly_00_Verify_SKU_Price extends TestBase {

    private String sellerToken = null;
    private String SellyAdminToken = null;
    private UserAPI userAPI = new UserAPI();
    private ProductAPI ProductAPI = new ProductAPI();

    @Test(dataProvider = "getDataForTest", priority = 1, description = "Verify Product SKU Price")
    public void TC01(Hashtable<String, String> data) throws IOException {
        try {

            logStep = logStepInfo(logMethod, "Step #1: Get seller's token from Phone Number");
            sellerToken = userAPI.getSellerToken(logStep, GlobalVariables.SellerPhone);

            logStep = logStepInfo(logMethod, "Step #2: Get Selly Admin's token");
            SellyAdminToken = userAPI.getAdminToken(logStep, GlobalVariables.SellyAdminID);

            logStep = logStepInfo(logMethod, "Step #3: Verify Product SKU Price");
            ProductAPI.VerifySKUPrice(logStep, sellerToken, SellyAdminToken, 100);

        } catch (Exception e) {
            log4j.error(getStackTrade(e.getStackTrace())) ;
            logException(logMethod, testCaseName, e);
        }

    }

}
