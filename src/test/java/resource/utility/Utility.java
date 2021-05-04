package resource.utility;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.google.gson.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import resource.common.GlobalVariables;
import resource.utility.webdrivers.DriverFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Utility {
    public static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<RemoteWebDriver>();

    // Add initialization of drivers
    public static String subWindowHandler = null;
    public static ExtentReports report = null;
    public static ExtentHtmlReporter htmlReporter = null;

    //Initiate variable for log4j
    public static Log log4j;
    public static ExcelReader excelReader = null;
    public static ConfigFileReader configFileReader = null;

    //Initiate local variables for generating time stamp

    public static String timeStampString = generateTimeStampString("yyyy-MM-dd-HH-mm-ss");

    //Initiate local variables for sending email
//    public static String reportLocation = GlobalVariables.OUTPUT_PATH + "report-" + timeStampString + "/";
    public static String reportLocation = GlobalVariables.OUTPUT_PATH + "report/";
    public static String reportFilePath = reportLocation + "report-" + timeStampString + ".html";

    //Variable for generate random string
    static Calendar now = Calendar.getInstance();


    public static RemoteWebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(RemoteWebDriver webDriver) {
        driver.set(webDriver);
    }

    public static void initializeDriver(ExtentTest logTest) throws IOException {
        try {
            switch (GlobalVariables.RUN_ON.toLowerCase()) {
                case "perfectomobile":
                    Utility.setDriver(DriverFactory.createInstance(GlobalVariables.PLATFORM_NAME, GlobalVariables.PLATFORM_VERSION, GlobalVariables.MANUFACTURER, GlobalVariables.MODEL, GlobalVariables.BROWSER, GlobalVariables.BROWSER_VERSION, GlobalVariables.RESOLUTION, GlobalVariables.LOCATION, logTest));
                    break;
                case "grid":
                    Utility.setDriver(DriverFactory.createInstanceGrid(GlobalVariables.BROWSER,logTest));
                    maximizeWindow();
                    break;
                default:
                    Utility.setDriver(DriverFactory.createInstance(GlobalVariables.BROWSER, logTest));
                    maximizeWindow();
                    break;
            }

            // Check if running on Mobile or Desktop
            Platform platForm = ((RemoteWebDriver) Utility.getDriver()).getCapabilities().getPlatform();
            GlobalVariables.IS_MOBILE = (platForm == Platform.ANDROID || platForm == Platform.IOS);

            Utility.getDriver().manage().deleteAllCookies();
            Utility.getDriver().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            Utility.getDriver().manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
        } catch (Exception e) {
            GlobalVariables.TOTAL_FAILED++;
            log4j.error("initializeDriver method - ERROR - " + e);
            logException(logTest, "initializeDriver method - ERROR", e);
        }
    }

    public static void log4jConfiguration() {
        try {
            log4j = LogFactory.getLog(new Object().getClass());
        } catch (Exception e) {
            log4j.error("log4jConfiguration method - ERROR: " + e);
        }
    }

    public static String getStackTrade(StackTraceElement[] stackTradeElements) {
        try {
            String stackTrade = "";
            for (StackTraceElement element : stackTradeElements) {
                stackTrade += element.toString() + "</br>";
                // Get stacktrade from comm.module level only
                if (element.toString().startsWith("feature"))
                    break;
            }
            return stackTrade;
        } catch (Exception ex) {
            return "";
        }
    }

    public static void verifyExpectedAndActualResults(ExtentTest logTest, String expected, String actual) throws IOException {
        try {
            if (actual.trim().equalsIgnoreCase(expected)) {
                logPass(logTest, "Expected Result: " + expected + "<br/>Actual Result: " + actual);
            } else {
                logFail(logTest, "Expected Result: " + expected + "<br/>Actual Result: " + actual);
            }
        } catch (Exception e) {
            log4j.error("verifyExpectedAndActualResults method - ERROR - " + e);
            logException(logTest, "verifyExpectedAndActualResults method - ERROR", e);
        }
    }

    public static void verifyExpectedAndActualNumber(ExtentTest logTest, Float expected, Float actual) throws IOException {
        try {
            if (actual.equals(expected)) {
                logPass(logTest, "Expected Result: " + expected + "<br/>Actual Result: " + actual);
            } else {
                logFail(logTest, "Expected Result: " + expected + "<br/>Actual Result: " + actual);
            }
        } catch (Exception e) {
            log4j.error("verifyExpectedAndActualNumber method - ERROR - " + e);
            logException(logTest, "verifyExpectedAndActualNumber method - ERROR", e);
        }
    }

    public static void verifyUserStatistic(ExtentTest logTest, String expected, String actual) throws IOException {
        try {
            if (actual.trim().equalsIgnoreCase(expected)) {
                logPass(logTest, "Expected Result: " + expected + "<br/>Actual Result: " + actual);
            } else {
                logFail(logTest, "Expected Result: " + expected + "<br/>Actual Result: " + actual);
            }
        } catch (Exception e) {
            log4j.error("verifyUserStatistic method - ERROR - " + e);
            logException(logTest, "verifyUserStatistic method - ERROR", e);
        }
    }

    public static void checkControlExist(ExtentTest logTest, WebElement elementName, String objectName) throws IOException {
        try {
            waitForControl(elementName);
            if (!doesControlExist(elementName)) logFail(logTest, objectName + " does not exist.");
            else logPass(logTest, objectName + " exists.");
        } catch (Exception e) {
            log4j.error("checkControlExist - ERROR - " + e);
            logException(logTest, "checkControlExist method - ERROR", e);
        }
    }

    public static void checkControlNotExist(ExtentTest logTest, WebElement elementName, String objectName) throws IOException {
        try {
            if (doesControlExist(elementName))
                logFail(logTest, objectName + " exist.");
            else
                logPass(logTest, objectName + " does not exists.");
        } catch (Exception e) {
            log4j.error("checkControlNotExist - ERROR - " + e);
            logException(logTest, "checkControlNotExist method - ERROR", e);
        }
    }

    public static void checkControlProperty(WebElement elementName, String elementProperty, String propertyValue, ExtentTest logTest) throws IOException {
        try {
            log4j.debug("checkControlPropertyValue method...Start");

            waitForControl(elementName);
            String actualPropertyValue = elementName.getAttribute(elementProperty);
            verifyExpectedAndActualResults(logTest, propertyValue, actualPropertyValue);

            log4j.info("checkControlPropertyValue method...End");

        } catch (Exception e) {
            log4j.error("checkControlPropertyValue - ERROR - " + e);
            logException(logTest, "checkControlPropertyValue method - ERROR", e);
        }
    }

    public static void navigateToTestSite(ExtentTest logTest, String url) throws IOException {
        try {
//            logInfo(logTest, "Navigate to site: " + url);
            Utility.getDriver().navigate().to(url);
            maximizeWindow();
            waitForPageLoaded();
        } catch (Exception e) {
            log4j.error("navigateToTestSite method - ERROR - " + e);
            logException(logTest, "navigateToTestSite method - ERROR", e);
        }
    }

    public static void logInfo(ExtentTest logTest, String description) {
        log4j.info(description);
        logTest.info(description);
    }

    public static void logFail(ExtentTest logTest, String description) throws IOException {
        try {
            // Report test fails and capture screenshot
//            captureScreenshot("FAILED screenshot: ", "fail-", logTest);
            logTest.fail(MarkupHelper.createLabel(description, ExtentColor.RED));
//        throw new SkipException(description);
        } catch (SkipException ex) {
            logTest.fail(MarkupHelper.createLabel(description + "</br>" + getStackTrade(ex.getStackTrace()), ExtentColor.RED));
            Assert.fail(description);
        }
    }

    public static void logPass(ExtentTest logTest, String description) {
        logTest.pass(MarkupHelper.createLabel(description, ExtentColor.GREEN));
    }

    public static void logException(ExtentTest logTest, String description, Exception exception) throws IOException {
        try {
            // Report test fails and capture screenshot
            captureScreenshot("ERROR screenshot: ", "error-", logTest);

            throw new SkipException(description);
        } catch (SkipException ex) {
            logTest.error(MarkupHelper.createLabel(description + "</br>" + exception.toString() + "</br>" + getStackTrade(exception.getStackTrace()), ExtentColor.ORANGE));
            Assert.fail(description);
        }
    }

    public static void logSkip(ExtentTest logTest, String description) {
        logTest.skip(MarkupHelper.createLabel(description, ExtentColor.GREY));
    }

    public static ExtentTest logStepInfo(ExtentTest logTest, String description, Object... args) throws IOException {
        log4j.info(description);
        return logTest.createNode(description);
    }

    public static ExtentTest createNodeForExtentReport(ExtentTest parentTest, String description) {
        return parentTest.createNode(description);
    }

    public static void sleep(long timeout) {
        try {
            Thread.sleep(timeout * 1000);
        } catch (Exception e) {
            log4j.warn("Exception is sleep method - ERROR: " + e);
        }
    }

    public static void waitForControl(WebElement controlName) {
        try {
            new WebDriverWait(Utility.getDriver(), GlobalVariables.WAIT_TIME).until(ExpectedConditions.visibilityOf(controlName));
        } catch (Exception ex) {
            // TBD
        }
    }

    public static void waitForControlToBeClickable(WebElement controlName) {
        new WebDriverWait(Utility.getDriver(), GlobalVariables.WAIT_TIME).until(ExpectedConditions.visibilityOf(controlName));
        new WebDriverWait(Utility.getDriver(), GlobalVariables.WAIT_TIME).until(ExpectedConditions.elementToBeClickable(controlName));
    }

    public static void waitForControlToBeEnable(WebElement controlName) {
        try {
            if (isControlEnable(controlName)) {
                int i = 0;
                while (i < GlobalVariables.WAIT_TIME/15 && isControlEnable(controlName)) {
                    sleep(1);
                }
            }
        } catch (Exception e) {
            log4j.error("waitForControlToBeEnable method - ERROR - " + e);
        }
    }

    public static void waitForPageLoaded() {
        Wait<WebDriver> wait = new WebDriverWait(Utility.getDriver(), GlobalVariables.WAIT_TIME);
        try {
            // Wait for HTML load
            wait.until(new Function<WebDriver, Boolean>() {
                public Boolean apply(WebDriver driver) {
                    sleep(1);
                    boolean readyState = ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
                    boolean activeJQuery = ((JavascriptExecutor) driver).executeScript("if (typeof jQuery != 'undefined') { return jQuery.active == 0; } else {  return true; }").equals(true);
                    return readyState && activeJQuery;
                }
            });
        } catch (Exception e) {
            // TBD
        }
    }

    public static void refreshPage() {
        try {
            Utility.getDriver().navigate().refresh();
            waitForPageLoaded();
        } catch (Exception e) {
            log4j.error("refreshPage method - ERROR - " + e);
        }
    }

    public static void forceClick(WebElement controlName) {
        waitForControl(controlName);
        JavascriptExecutor executor = (JavascriptExecutor) Utility.getDriver();
        executor.executeScript("arguments[0].click();", controlName);
    }

    public static void scrollIntoView(WebElement controlName) {
        waitForControl(controlName);
        JavascriptExecutor executor = (JavascriptExecutor) Utility.getDriver();
        executor.executeScript("arguments[0].scrollIntoView(true);", controlName);
    }

    public static boolean doesControlExist(WebElement control){
        try {
            return control.isDisplayed();

        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isControlEnable(WebElement control){
        try {
            return control.isEnabled();

        } catch (Exception e) {
            return false;
        }
    }

    public static String getWindowHandle(WebDriver driver) {
        // get all the window handles after the popup window appears
        Set<String> afterPopup = driver.getWindowHandles();

        Iterator<String> iterator = afterPopup.iterator();
        while (iterator.hasNext())
            subWindowHandler = iterator.next();

        return subWindowHandler;
    }

    public static void switchToWindowHandle() throws IOException {
        try {
            String popupWidowHandle = getWindowHandle(Utility.getDriver());
            Utility.getDriver().switchTo().window(popupWidowHandle);
            maximizeWindow();
        } catch (Exception e) {
            log4j.error("switchToWindowHandle method - ERROR - " + e);
        }
    }

    public static void maximizeWindow() throws IOException {
        try {
            if (GlobalVariables.OS_NAME.contains("mac")|| GlobalVariables.OS_NAME.contains("linux"))
                Utility.getDriver().manage().window().setSize(new Dimension(1440, 900));
            else
                Utility.getDriver().manage().window().maximize();
        } catch (Exception e) {
            log4j.error("maximizeWindow method - ERROR - " + e);
        }
    }

    public static void quit(ExtentTest logTest) throws IOException {
        try {
            Utility.getDriver().quit();
            logInfo(logTest, "Closed browser and released device");
        } catch (Exception e) {
            log4j.error("Unable to close browser/release device: " + e);
        }
    }

    public static boolean isTestCaseExecutable(String sheetName, String testName, ExcelReader excelReader) throws IOException {
        /* Handle for Precondition tests of IDC*/
        /* Get the test case ID only to compare*/
        if (testName.contains("_Pre_")) testName = testName.split("_")[0];

        // As first row is a header, we are going to start looking for our test1
        // from row#2
        for (int rowNum = 2; rowNum <= excelReader.getRowCount(sheetName); rowNum++) {
            // Find out the test case we are looking for
            if (excelReader.getCellData(sheetName, "TestCaseId", rowNum).contains(testName)) {
                // Find out whether Runmode of test case is set to "Yes", then,
                // return true, otherwise return false
                GlobalVariables.RUN_MODE = excelReader.getCellData(sheetName, "RunMode", rowNum);
                return GlobalVariables.RUN_MODE.equalsIgnoreCase("Y");
            }
        }
        // Return false in case if we do not find out the test case which we are
        // looking for
        return false;
    }

    public static boolean isTestDataExecutable(Hashtable<String, String> data, ExtentTest logTest) throws IOException {
        boolean testDataRun = false;
        boolean testingType = false;

        try {
            // if Testing Type = Smoke, execute Smoke test cases
            if ((GlobalVariables.TESTING_TYPE.equalsIgnoreCase("Smoke") || GlobalVariables.TESTING_TYPE.equalsIgnoreCase("ProdSmoke")) && data.get("TestingType").equalsIgnoreCase("Smoke")) {
                testingType = true;
            }
            // if Testing Type = BVT, execute BVT + Smoke test cases
            else if (GlobalVariables.TESTING_TYPE.equalsIgnoreCase("BVT") && (data.get("TestingType").equalsIgnoreCase("BVT") || data.get("TestingType").equalsIgnoreCase("Smoke"))) {
                testingType = true;
            }
            // if Testing Type = Regression, execute BVT + Smoke + Regression + Full Regression test cases
            else if (GlobalVariables.TESTING_TYPE.equalsIgnoreCase("Regression") && (data.get("TestingType").equalsIgnoreCase("BVT") || data.get("TestingType").equalsIgnoreCase("Smoke") || data.get("TestingType").equalsIgnoreCase("Regression"))) {
                testingType = true;
            }
            // if Testing Type = Full Regression, execute BVT + Smoke + Regression + Full Regression test cases
            else if (GlobalVariables.TESTING_TYPE.equalsIgnoreCase("Full Regression") && (data.get("TestingType").equalsIgnoreCase("BVT") || data.get("TestingType").equalsIgnoreCase("Smoke") || data.get("TestingType").equalsIgnoreCase("Regression") || data.get("TestingType").equalsIgnoreCase("Full Regression"))) {
                testingType = true;
            }
            else if (GlobalVariables.TESTING_TYPE.equalsIgnoreCase("ProdCancelWarranty") || GlobalVariables.TESTING_TYPE.equalsIgnoreCase("ProdClaimRefund") || GlobalVariables.TESTING_TYPE.equalsIgnoreCase("SkySmoke") || GlobalVariables.TESTING_TYPE.equalsIgnoreCase("SkyRegression") || GlobalVariables.TESTING_TYPE.equalsIgnoreCase("SkyApiSmoke") || GlobalVariables.TESTING_TYPE.equalsIgnoreCase("SkyApiRegression") || GlobalVariables.TESTING_TYPE.equalsIgnoreCase("StaticSiteSmoke") || GlobalVariables.TESTING_TYPE.equalsIgnoreCase("WarriorAdyen")) {
                testingType = true;
            }

            if (data.get("RunMode").equalsIgnoreCase("Y") && testingType == true)
                testDataRun = true;
            else {
                if (!data.get("RunMode").equalsIgnoreCase("Y"))
                    logSkip(logTest, "Skipping test as RunMode was set to NO");
                else
                    logSkip(logTest, "Skipping test as TestingType is not appropriated");
            }

        } catch (Exception e) {
            log4j.error("isTestDataExecutable method - ERROR - " + e);
            logException(logTest, "isTestDataExecutable method - ERROR", e);
        }
        return testDataRun;
    }

    // ******** Reading from Excel starts here ****************//

    public static void captureScreenshot(String detail, String screenshotName, ExtentTest logTest) throws IOException {
        try {
            sleep(2);

            // Get screenshot name
            screenshotName = screenshotName + generateTimeStampString("yyyy-MM-dd-HH-mm-ss") + ".png";

            // Capture screenshot (If driver == null, it means there is no window opens => Don't capture screenshot)
            TakesScreenshot ts = (TakesScreenshot) Utility.getDriver();
            File source = ts.getScreenshotAs(OutputType.FILE);
            String dest = reportLocation + screenshotName;
            File destination = new File(dest);
            FileUtils.copyFile(source, destination);

            // Add current URL to report
            if (getDriver() != null)
                logTest.info("Page URL: " + Utility.getDriver().getCurrentUrl());

            // Add screenshot to report
            String screenshotLink = "<a href=\"" + screenshotName + "\">" + screenshotName + "</a>";
            if (logTest.getStatus() == Status.ERROR)
                logTest.error(detail + screenshotLink).addScreenCaptureFromPath(screenshotName);
            else if (logTest.getStatus() == Status.FAIL)
                logTest.fail(detail + screenshotLink).addScreenCaptureFromPath(screenshotName);
            else
                logTest.pass(detail + screenshotLink).addScreenCaptureFromPath(screenshotName);
        } catch (Exception e) {
            log4j.info("Exception while taking screenshot: " + e.getMessage());
        }
    }

    public static long getCurrentTimeInMiliseconds() {
        return System.currentTimeMillis();
    }

    public ExtentTest createTestForExtentReport(ExtentReports report, String description) {
        return report.createTest(description);
    }

    public String convertSnakeCaseToCamelCase(String name, ExtentTest logTest)throws IOException{
        String newName = null;
        try{
                newName = String.format(name.replaceAll("\\_(.)", "%S"), (Object[]) name.replaceAll("[^_]*_(.)[^_]*", "$1_").split("_"));
            }
        catch (Exception e){
            log4j.error("convertSnakeCaseToCamelCase method - ERROR - " + e);
            logException(logTest, "convertSnakeCaseToCamelCase method - ERROR", e);
        }
        return newName;
    }

    public static String generateTimeStampString(String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String timestampStr = dtf.format(now);
        return timestampStr;
    }

    public static String generateTimeStampString(int length) {
        String timestampStr = null;
        if (length <= 14 && length > 0) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime now = LocalDateTime.now();
            timestampStr = dtf.format(now);
        }
        return right(timestampStr, length);
    }

    public static int getDayOfMonth() {
        return now.get(Calendar.DATE);
    }

    public static int getMonth() {
        return now.get(Calendar.MONTH);
    }

    public static int getyear() {
        return now.get(Calendar.YEAR);
    }

    public static String right(String value, int length) {
        // To get right characters from a string, change the begin index.
        return value.substring(value.length() - length);
    }

    public static Object[][] getData(String testName, String dataFilePath, ExtentTest logTest) throws IOException {

        Object[][] data = new Object[0][1];

        //Read json file data using Gson library
        BufferedReader br = new BufferedReader(new FileReader(dataFilePath));
        JsonElement jsonElement = new JsonParser().parse(br);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        //Check for the test name in the json file
        boolean blnTCExist = jsonObject.has(testName);
        if (!blnTCExist) {
            log4j.error(testName + " is not present in the data.json file - " + dataFilePath);
            return data;
        }

        //Get test data for the specific test case
        JsonArray jsonArray = jsonObject.getAsJsonArray(testName);
        data = jsonArrayToObjectArray(jsonArray);
        return data;
    }

    public static Object[][] jsonArrayToObjectArray(JsonArray jsonArray){

        Object[][] data = new Object[0][1];
        int index = 0;
        Gson gson = new Gson();

        if (jsonArray.size()>0) {
            data = new Object[jsonArray.size()][1];
            for (Object obj : jsonArray) {
                Hashtable<String, String> hashTable = new Hashtable<String, String>();
                data[index][0] = gson.fromJson((JsonElement) obj, hashTable.getClass());
                index++;
            }
        }
        return data;
    }

    public static boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                boolean success = deleteDirectory(files[i]);
                if (!success) {
                    return false;
                }
            }
        }
        System.out.println("removing file or directory : " + dir.getName());
        return dir.delete();
    }

    public static void verifyActualIsNotEmptyResults(ExtentTest logTest,String actual) throws IOException {
        try {
            if (StringUtils.isEmpty(actual)) {
                logFail(logTest, "Expected Result: is not empty " + "<br/>Actual Result: " + actual);
            } else {
                logPass(logTest, "Expected Result: is not empty " + "<br/>Actual Result: " + actual);
            }
        } catch (Exception e) {
            log4j.error("verifyActualIsNotEmptyResults method - ERROR - " + e);
            logException(logTest, "verifyActualIsNotEmptyResults method - ERROR", e);
        }
    }

    public int getRetryCount(ArrayList<String> testCaseList, String testName) {
        int count = 0;

        for (int i=0; i<testCaseList.size(); i++) {
            if (testCaseList.get(i).contains(testName)) {
                count++;
            }
        }
        return count;
    }


    public void getTestCaseExecutionCount(ArrayList<String> testCaseList) {

        for (int i=0; i<testCaseList.size(); i++) {
            if (testCaseList.get(i).contains(": pass")) {
                if (testCaseList.get(i).contains(": RETRY")) {
                    GlobalVariables.TOTAL_PASSED_WITH_RETRY++;
                } else {
                    GlobalVariables.TOTAL_PASSED++;
                }
            } else if(testCaseList.get(i).contains(": skip")) {
                GlobalVariables.TOTAL_SKIPPED++;
            } else {
                if (GlobalVariables.RETRY_FAILED_TESTS.equalsIgnoreCase("Yes")) {
                    if (testCaseList.get(i).contains(": RETRY2")) {
                        GlobalVariables.TOTAL_FAILED++;
                    }
                } else {
                    GlobalVariables.TOTAL_FAILED++;
                }
            }
        }

        GlobalVariables.TOTAL_TESTCASES = GlobalVariables.TOTAL_PASSED + GlobalVariables.TOTAL_PASSED_WITH_RETRY + GlobalVariables.TOTAL_FAILED + GlobalVariables.TOTAL_SKIPPED;
    }
}
