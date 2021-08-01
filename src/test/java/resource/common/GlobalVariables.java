package resource.common;

public class GlobalVariables {
    //JIRA
    public static String JIRA_URL = "https://lethang052407.atlassian.net";
    public static String JIRA_DOMAIN = "lethang052407";
    public static String JIRA_USERNAME = "lethang052407@gmail.com";
    public static String JIRA_TOKEN = "yxbZgQUQIWpxm1IPiDrA1ABA";
    public static String JIRA_PASSWORD = "Th@ng2407";

    //CB User INFO
    public static String user_token = null;
    public static String user_id = null;

    //CB referral INFO
    public static String CB_referral_bonus = "10000";
    public static String referral_code = "thang10k";

    //CB INFO
    public static String ENVIRONMENT = "svc.cashbagmain.com";
    public static String API_ENVIRONMENT = "api.cashbagmain.com";
    public static String CB_URL = "https://cashbagmain.com";
    public static String gg_username = "cashbag.qa@gmail.com";
    public static String gg_username2 = "lethang05240709@gmail.com";
    public static String gg_password = "Th@ng2407";

    //SELLY
    public static String SellyEnvironment = "app-api.unibag.xyz";
    public static String SellyAdminEnvironment = "admin-api.unibag.xyz";
    public static String SellyAdminToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MDY0Mjg0ZDhlZjliMjY4NTE3OWE2MWEiLCJleHAiOjE2MzM4NzkyNTksIm5hbWUiOiJSb290IiwicGVybWlzc2lvbnMiOltdLCJwaG9uZSI6Iis4NDc2Mzc3MDg2OSIsInR5cGUiOiJzdGFmZiJ9.sz9mUJssECBEK25zNc2SFvneM_K6Zu1dzo_XT9CRtls";
    public static String ImsDeliveryEnvironment = "delivery.sellyims.net";
    public static String ImsEnvironment = "order.sellyims.net";
    public static String IMSToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MDU4NDAzNzM1OGQzMjJlZDZiNzA3NjMiLCJhdmF0YXIiOiJodHRwczovL3NlbGx5LWltcy1kZXZlbG9wLnMzLWFwLXNvdXRoZWFzdC0xLmFtYXpvbmF3cy5jb20vMzg2ODc2Mjg3MzMxXzE2MTYzOTY3MzUxOTAuanBnIiwiZXhwIjoxNjMzODc3MzczLCJpc1Jvb3QiOnRydWUsIm5hbWUiOiJSb290IiwicGFydG5lciI6IjAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMCIsInBlcm1pc3Npb25zIjpbXSwicGhvbmUiOiIrODQ3NjM3NzA4NjkiLCJ0eXBlIjoiaW1zIiwidXNlciI6IjYwNTg0MDM3MzU4ZDMyMmVkNmI3MDc2MSJ9.ubRpoJdcXGJSyaOd1oyx-cbTBUrPs14Mf7-_zCed7ns";
    public static String SellerId = "6012952a45f56d576a800874";
    public static String SellerPhone = "0935321012";
    public static String SellyAdminID = "60915206ecdb41d888a7f384";

    //PRICE
    public static Integer SellerSharingCommission = 70;
//    public static Integer SupplierCommissionPercent = 70;


    //handling window
    public static String mainWindow = null;

    // Default wait time
    public static final int WAIT_TIME = 60;

    //Run parameters
    public static String TESTING_TYPE = "";
    public static String RUN_ON = "Local";
    public static String PLATFORM = "";
    public static String PLATFORM_NAME = "";
    public static String PLATFORM_VERSION = "";
    public static String MANUFACTURER = "";
    public static String MODEL = "";
    public static String BROWSER = "Chrome";
    public static String BROWSER_VERSION = "latest";
    public static String RESOLUTION = "1280x1024";
    public static String LOCATION = "NA-US-BOS";
    public static String RUN_MODE = "Y";
    public static String THREAD_COUNT = "1";
    public static boolean USE_PERFECTO_CONNECT = false;
    public static boolean IS_MOBILE = false;
    public static int NUMBER_OF_REPORT = 30;

    //Report data
    public static int TOTAL_TESTCASES = 0;
    public static int TOTAL_EXECUTED = 0;
    public static int TOTAL_PASSED = 0;
    public static int TOTAL_FAILED = 0;
    public static int TOTAL_SKIPPED = 0;
    public static int TOTAL_PASSED_WITH_RETRY = 0;
    public static String RETRY_FAILED_TESTS = "";

    //Project path
    public static final String PROJECT_PATH = System.getProperty("user.dir");
    public static final String RESOURCES_PATH = PROJECT_PATH + "/src/main/resources/";
    public static final String OUTPUT_PATH = PROJECT_PATH + "/src/main/resources/output/";

    //JSON FILE PATH
    public static final String create_customer = PROJECT_PATH + "/src/test/java/resource/payload_templates/Selly/create_customer_payload.json";
    public static final String create_multi_delivery_payload = PROJECT_PATH + "/src/test/java/resource/payload_templates/Selly/create_delivery_session_multiple_payload.json";
    public static final String Product_In_Selly_Inventory_Json_file = PROJECT_PATH + "/src/test/java/resource/product_detail/Product_in_Selly_Inventory.json";
    public static final String Product_In_Cashbag_Inventory_Json_file = PROJECT_PATH + "/src/test/java/resource/product_detail/Product_in_Cashbag_Inventory.json";
    public static final String Product_In_Zody_Inventory_Json_file = PROJECT_PATH + "/src/test/java/resource/product_detail/Product_in_Zody_Inventory.json";
    public static final String Product_In_Unibag_Inventory_Json_file = PROJECT_PATH + "/src/test/java/resource/product_detail/Product_in_Unibag_Inventory.json";
    public static final String Product_In_HCM_Inventory_Json_file = PROJECT_PATH + "/src/test/java/resource/product_detail/Product_in_HCM_Inventory.json";
    public static final String Product_In_DaNang_Inventory_Json_file = PROJECT_PATH + "/src/test/java/resource/product_detail/Product_in_DaNang_Inventory.json";

    //OS name
    public static final String OS_NAME = System.getProperty("os.name").toLowerCase();

    //The excel file that including all test cases and data test on whole project
    public static final String TEST_DATA_SHEET = "TestData";
    public static final String TEST_DATA_JSON = PROJECT_PATH + "/src/test/java/";
    public static final String TEST_CONFIGURATION = RESOURCES_PATH + "configuration/Configuration.properties";

    //SHOW/HIDE skip test case in report
    public static final boolean SHOW_SKIP = false;

    //Driver variables
    public static final String CHROME_DRIVER_MAC = PROJECT_PATH + "/src/main/resources/drivers/chromedriver";
    public static final String CHROME_DRIVER_WIN = PROJECT_PATH + "/src/main/resources/drivers/chromedriver_v91.exe";
    public static final String GECKO_DRIVER_MAC = PROJECT_PATH + "/src/main/resources/drivers/geckodriver-v0.23";
    public static final String GECKO_DRIVER_WIN = PROJECT_PATH + "/src/main/resources/drivers/geckodriver-v0.23.exe";

    //Upload item receipt path variables starts here...!
    public static final String RECEIPT_PNG_FILE_NAME = "item_receipt.png";
    public static final String RECEIPT_PDF_FILE_NAME = "item_receipt.pdf";

    //Mobile variables
    public static final String APPIUM_SERVER_IP = "0.0.0.0";
    public static final Integer APPIUM_PORT = 4723;
    public static final String APPIUM_SERVER_ADDRESS = "http://0.0.0.0:4723/wd/hub";
    public static final String APP_LOCATION = "F:\\AUTOMATION\\12.Appium\\vn.selly.staging_1.12.0.apk";
    public static final String PERFECTO_USERNAME = System.getenv("PERFECTO_USERNAME");
    public static final String PERFECTO_TOKEN = System.getenv("PERFECTO_TOKEN");
    public static final String PERFECTO_HOST = "allstate.perfectomobile.com";
    public static final String PERFECTO_HUB_ADDRESS = "https://allstate.perfectomobile.com/nexperience/perfectomobile/wd/hub";
    public static final String PERFECTO_CONNECT_MAC = PROJECT_PATH + "/src/main/resources/drivers/perfectoconnect";
    public static final String PERFECTO_CONNECT_WIN = PROJECT_PATH + "/src/main/resources/drivers/perfectoconnect.exe";
    public static String TUNNEL_ID = "";
    public static String phoneNumber = "0935321012";
    public static String otp = "123456";

    public static Process p;
    public static String TEST_NAME;

}
