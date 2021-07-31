package resource.common;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.perfecto.reportium.client.ReportiumClient;
import resource.utility.ConfigFileReader;
import resource.utility.Utility;
import org.apache.log4j.xml.DOMConfigurator;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static resource.common.GlobalVariables.*;

public class TestBase extends Utility {

    public static boolean isTestSuiteExecutable = false;
    public boolean isTestCaseExecutable = true;

    public static ExtentTest logSuite = null;
    public ExtentTest logClass = null;
    public ExtentTest logMethod = null;
    public ExtentTest logStep = null;
    public ReportiumClient reportiumClient = null;

    public String testCaseName;
    public String testNameWithStatus;
    public static ArrayList<String> testcaseList = new ArrayList<String>();

    @BeforeSuite(alwaysRun=true)
    public synchronized void beforeSuite() throws IOException {

        // Initiate log4j property system
        log4jConfiguration();
        DOMConfigurator.configure("src/main/resources/configuration/log4j.xml");

        log4j.info("beforeSuite method - Start");

        // Initial test report
        try {
            htmlReporter = new ExtentHtmlReporter(reportFilePath);
            htmlReporter.loadXMLConfig(new File(PROJECT_PATH + "/src/main/resources/configuration/config.xml"));
            report = new ExtentReports();
            report.attachReporter(htmlReporter);
            logSuite = createTestForExtentReport(report, "Initial Setup");
        } catch (Exception e) {
            log4j.error("ERROR while initializing Extend report: " + e.getStackTrace());
            logException(logSuite, "ERROR while initializing Extend report", e);
        }

        // Create report folder
        logInfo(logSuite, "Report folder: " + reportLocation);
        File folder = new File(reportLocation);
        folder.mkdirs();

        // Initial test data
        try {
            String testDataFilePath = TEST_CONFIGURATION;
            configFileReader = new ConfigFileReader(testDataFilePath, logSuite);
            logInfo(logSuite, "Test data: " + testDataFilePath);
        } catch (Exception e) {
            log4j.error("ERROR while reading Test Configuration: " + e.getStackTrace());
            logException(logSuite, "ERROR while reading test Configuration: ", e);
        }

        // Validate testingType
        try {
            TESTING_TYPE = System.getProperty("testingType") == null ? configFileReader.getDataFromConfigurationFile("TestingType") : System.getProperty("testingType");
            if (TESTING_TYPE != null && (TESTING_TYPE.equalsIgnoreCase("Full Regression") || TESTING_TYPE.equalsIgnoreCase("Regression") || TESTING_TYPE.equalsIgnoreCase("BVT") || TESTING_TYPE.equalsIgnoreCase("Smoke") || TESTING_TYPE.equalsIgnoreCase("ProdSmoke") || TESTING_TYPE.equalsIgnoreCase("ProdCancelWarranty") || TESTING_TYPE.equalsIgnoreCase("ProdClaimRefund") || TESTING_TYPE.equalsIgnoreCase("SkySmoke") || TESTING_TYPE.equalsIgnoreCase("SkyRegression") || TESTING_TYPE.equalsIgnoreCase("SkyApiSmoke") || TESTING_TYPE.equalsIgnoreCase("SkyApiRegression") || TESTING_TYPE.equalsIgnoreCase("StaticSiteSmoke") || TESTING_TYPE.equalsIgnoreCase("WarriorAdyen")))
                logInfo(logSuite, "Testing Type: " + TESTING_TYPE);
            else
                logFail(logSuite, "Invalid 'testingType' parameter: " + TESTING_TYPE);
        } catch (Exception e) {
            log4j.error("Error when getting 'testingType' parameter: " + e);
            logException(logSuite, "Error when getting 'testingType' parameter: ", e);
        }

        // Validate environment
        try {
            ENVIRONMENT = System.getProperty("environment") == null ? configFileReader.getDataFromConfigurationFile("Environment") : System.getProperty("environment");
            if (ENVIRONMENT == null || ENVIRONMENT == "")
                logFail(logSuite, "Invalid 'environment' parameter: " + ENVIRONMENT);
            else {
                logInfo(logSuite, "Environment: " + ENVIRONMENT);
            }
        } catch (Exception e) {
            log4j.error("Error when getting 'environment'' parameter: " + e);
            logException(logSuite, "Error when getting 'environment' parameter: ", e);
        }

        // Validate runOn
        try {
            RUN_ON = System.getProperty("runOn") == null ? configFileReader.getDataFromConfigurationFile("RunOn") : System.getProperty("runOn");
            if (RUN_ON != null && (RUN_ON.equalsIgnoreCase("Local") || RUN_ON.equalsIgnoreCase("Mobile") || RUN_ON.equalsIgnoreCase("PerfectoMobile") || RUN_ON.equalsIgnoreCase("Grid") || RUN_ON.equalsIgnoreCase("sel-hub-1.qa") || RUN_ON.equalsIgnoreCase("sel-hub-1.production")))
                logInfo(logSuite, "Run On: " + RUN_ON);
            else
                logFail(logSuite, "Invalid 'runOn' parameter: " + RUN_ON);
        } catch (Exception e) {
            log4j.error("Error when getting 'runOn' parameter: " + e);
            logException(logSuite, "Error when getting 'runOn' parameter", e);
        }

        // Validate browserName
        try {
            BROWSER = System.getProperty("browserName") == null ? configFileReader.getDataFromConfigurationFile("BrowserName") : System.getProperty("browserName");
            if (BROWSER == null || BROWSER == "") {
                logFail(logSuite, "Invalid 'browserName' parameter: " + BROWSER);
            } else {
                logInfo(logSuite, "Browser name: " + BROWSER);
            }

        } catch (Exception e) {
            log4j.error("Error when getting 'browserName' parameter" + e);
            logException(logSuite, "Error when getting 'browserName' parameter", e);
        }

        if (RUN_ON.equalsIgnoreCase("Local")||RUN_ON.equalsIgnoreCase("Grid")) {
            //Show OS name
            logInfo(logSuite, "OS name: " + OS_NAME);
        } else {
            // Validate platform
            try {
                PLATFORM = System.getProperty("platform") == null ? configFileReader.getDataFromConfigurationFile("Platform") : System.getProperty("platform");
                logInfo(logSuite, "Platform : " + PLATFORM);
                switch(PLATFORM){
                    case("Windows 10"):
                    case("Windows 8.1"):
                    case("Windows 7"):
                        PLATFORM_NAME = "Windows";
                        PLATFORM_VERSION = PLATFORM.split(" ")[1];
                        break;
                    case("macOS High Sierra"):
                    case("OS X El Capitan"):
                        PLATFORM_NAME = "Mac";
                        PLATFORM_VERSION = PLATFORM;
                        break;
                    case("iOS"):
                        PLATFORM_NAME = "iOS";
                        break;
                    case("ANDROID"):
                        PLATFORM_NAME = "Android";
                        break;
                    default:
                        logFail(logSuite, "Invalid 'platform' parameter: " + PLATFORM);
                }
            } catch (Exception e) {
                log4j.error("Error when getting 'platform' parameter" + e);
                logException(logSuite, "Error when getting 'platform' parameter", e);
            }
        }

        report.setSystemInfo("environment", ENVIRONMENT);
        report.setSystemInfo("Browser", BROWSER);

        isTestSuiteExecutable = true;
        log4j.info("beforeSuite method - End");
    }

    @BeforeClass(alwaysRun=true)
    public synchronized void beforeClass() throws IOException {
        log4j.info("beforeClass method - start");

        // Get test case class name
        testCaseName = this.getClass().getSimpleName();

        // Check if TC is executable or not
        if (isTestSuiteExecutable) {
            isTestCaseExecutable = true;
        }

        log4j.info("beforeClass method - End");
    }

    @BeforeMethod(alwaysRun=true)
    public synchronized void beforeMethod(Object[] data) throws IOException {
        log4j.info("beforeMethod method - Start");
        logStep = null;
        TOTAL_EXECUTED++;

        if(data != null && data.length > 0) {
            // Get test data for test case
            Hashtable<String, String> dataTest = (Hashtable<String, String>) data[0];

            //Get Retry count
            if (RETRY_FAILED_TESTS.equalsIgnoreCase("Yes")) {
                int retryCount = getRetryCount(testcaseList, testCaseName + ": " + dataTest.get("No."));
                if (retryCount > 0) {
                    testNameWithStatus = testCaseName + ": " + dataTest.get("No.") + ": RETRY" + retryCount;
                    //logMethod.assignCategory("RETRY");
                } else {
                    testNameWithStatus = testCaseName + ": " + dataTest.get("No.");
                }
            } else {
                testNameWithStatus = testCaseName + ": " + dataTest.get("No.");
            }

            //Initialize logClass
            logClass = createTestForExtentReport(report, testNameWithStatus);

            // Initial logMethod
            logMethod = createNodeForExtentReport(logClass, dataTest.get("TCID") + ": " + dataTest.get("TestDataPurpose"));
            log4j.info(dataTest.get("TCID") + ": " + dataTest.get("TestDataPurpose"));

            // Assign test category
            logMethod.assignCategory(dataTest.get("TestingType"));

            // Start web driver
            initializeDriver(logMethod);

            log4j.info("beforeMethod method - End");
        }
        else{
            logClass = createTestForExtentReport(report, testCaseName);
            logSkip(logClass, "This test case has no data to run");
        }
    }

    @AfterMethod(alwaysRun=true)
    public synchronized void afterMethod() throws IOException {
        log4j.info("afterMethod method - Start");

        //Update test execution status to the testcaseList
        testcaseList.add(testNameWithStatus + ": " + logMethod.getStatus());

        // Quit
        quit(logMethod);
        logMethod = null;

        log4j.info("afterMethod method - End");
    }

    @AfterClass(alwaysRun=true)
    public synchronized void afterClass() throws IOException {
        log4j.info("afterClass method - Start");

        // Remove skip test case from report
        if (isTestCaseExecutable == false && SHOW_SKIP == false) report.removeTest(logClass);

        List statusHierarchy = Arrays.asList(
                Status.FATAL,
                Status.FAIL,
                Status.ERROR,
                Status.WARNING,
                Status.PASS,
                Status.SKIP,
                Status.DEBUG,
                Status.INFO
        );

        report.config().statusConfigurator().setStatusHierarchy(statusHierarchy);

        // Save test result to HTML file after each test class
        report.flush();

        logClass = null;

        log4j.info("afterClass method - End");
    }

    @AfterSuite(alwaysRun=true)
    public synchronized void afterSuite() throws Exception {
        log4j.info("afterSuite method - Start");

        //Get the total count of TCs passed and failed
        getTestCaseExecutionCount(testcaseList);

        //This code block is used to keep limited number of reports
//        if (!RUN_ON.equalsIgnoreCase("Local")) {
//            String currentDirectory = System.getProperty("userAPI.dir");
//            File dir = new File(currentDirectory + "/src/main/resources/output");
//            File[] files = dir.listFiles();
//            Arrays.sort(files, Comparator.comparingLong(File::lastModified));
//            for (int i = 0; i < files.length - NUMBER_OF_REPORT; i++) {
//                deleteDirectory(files[i]);
//            }
//        }

        log4j.info("afterSuite method - End");
    }

    @DataProvider
    public Object[][] getDataForTest() throws IOException {
        String DataFilePath = TEST_DATA_JSON + this.getClass().getPackage().getName().replace(".","/") + "/data.json";
        Object[][] data =  getData(testCaseName, DataFilePath, logClass);
        if (data.length==0) {
            logClass = createTestForExtentReport(report, testCaseName);
            logClass.fail(testCaseName + " is not present in the data.json file");
            TOTAL_FAILED++;
        }
        return data;
    }
}
