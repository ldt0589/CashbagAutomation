package resource.pages;
import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import resource.utility.Utility;

import java.util.concurrent.TimeUnit;

public class MobileLoginPage extends Utility {

    public MobileLoginPage() {

        PageFactory.initElements(new AppiumFieldDecorator(Utility.getDriver()), this);
    }

    @AndroidFindBy(id = "vn.selly.staging:id/openTabAccount")
//    @iOSFindBy()
    private WebElement tabAccount;

    @AndroidFindBy(id = "vn.selly.staging:id/btAction")
    private WebElement buttonNext;

    @AndroidFindBy(id = "textinput_placeholder")
    private WebElement inputPhone;

    public void open_Login_screen(ExtentTest logTest) {
//        Utility.sleep(3);
        waitForControl(tabAccount);
        tabAccount.click();
//        buttonNext.click();
    }

    public void login_application(String phoneNumber, String otp, ExtentTest logTest){
        inputPhone.sendKeys(phoneNumber);
        buttonNext.click();
        inputPhone.sendKeys(otp);
        buttonNext.click();
    }
}