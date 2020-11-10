package resource.common;

public class GlobalVariables {

    //JIRA
    public static String JIRA_URL = "";
    public static String JIRA_USERNAME = "";
    public static String JIRA_PASSWORD = "";

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
    public static final String CHROME_DRIVER_WIN = PROJECT_PATH + "/src/main/resources/drivers/chromedriver_v85.exe";
    public static final String GECKO_DRIVER_MAC = PROJECT_PATH + "/src/main/resources/drivers/geckodriver-v0.23";
    public static final String GECKO_DRIVER_WIN = PROJECT_PATH + "/src/main/resources/drivers/geckodriver-v0.23.exe";

    //Upload item receipt path variables starts here...!
    public static final String RECEIPT_PNG_FILE_NAME = "item_receipt.png";
    public static final String RECEIPT_PDF_FILE_NAME = "item_receipt.pdf";

    //Perfecto variables
    public static final String PERFECTO_USERNAME = System.getenv("PERFECTO_USERNAME");
    public static final String PERFECTO_TOKEN = System.getenv("PERFECTO_TOKEN");
    public static final String PERFECTO_HOST = "allstate.perfectomobile.com";
    public static final String PERFECTO_HUB_ADDRESS = "https://allstate.perfectomobile.com/nexperience/perfectomobile/wd/hub";
    public static final String PERFECTO_CONNECT_MAC = PROJECT_PATH + "/src/main/resources/drivers/perfectoconnect";
    public static final String PERFECTO_CONNECT_WIN = PROJECT_PATH + "/src/main/resources/drivers/perfectoconnect.exe";
    public static String TUNNEL_ID = "";

    public static Process p;
    public static String TEST_NAME;

}
