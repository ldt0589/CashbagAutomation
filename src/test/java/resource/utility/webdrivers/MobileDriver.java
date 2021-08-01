package resource.utility.webdrivers;

import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.restassured.RestAssured;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import resource.common.GlobalVariables;
import resource.utility.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class MobileDriver {
    public RemoteWebDriver mobileDriver;

    private static Log log = LogFactory.getLog("Mobile logs");

    public synchronized RemoteWebDriver initialDriver(String platformName, String appLocation, ExtentTest logTest) throws IOException {
        try {

            Utility.startAppiumServer(logTest);

            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("platformName", platformName);
            caps.setCapability("app", appLocation);
//            String deviceId = getAvailableDevice(platformName);
            String deviceId = "ce091719b40a9d0405";

            if (deviceId == null || deviceId == "") {
                log.error("No available devices found: " + caps);
                throw new Exception("No available devices found: " + caps);
            } else {
                log.info("Device ID: " + deviceId);
                caps.setCapability("deviceName", deviceId);
            }

            switch (platformName.toLowerCase()) {
                case "ios":
                    mobileDriver = new IOSDriver(new URL(GlobalVariables.APPIUM_SERVER_ADDRESS), caps);
                    break;
                case "android":
                    mobileDriver = new AndroidDriver(new URL(GlobalVariables.APPIUM_SERVER_ADDRESS), caps);
                    break;
                default:
                    throw new Exception("PLATFORM is invalid: ");
            }

            log.info(caps.toJson());
            Utility.sleep(5);
        } catch (Exception e) {
            log.error("initialDriver method - Error: " + e);
            logTest.fail("initialDriver method - Error" + e);
        }

        log.info("Started Mobile driver");
        return mobileDriver;
    }

    public void getAvailableDevice(String platformName)
    {
//        Process process = null;
//        String commandString = String.format("%s", "adb devices");
//        System.out.print("Command is "+commandString+"\n");
//        try {
//            process = ProcessHelper.runTimeExec(commandString);
//        } catch (IOException e) {
//        }
//        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//        String line;
//        while ((line = reader.readLine()) != null) {
//            System.out.print(line+"\n");
//        }
    }
}