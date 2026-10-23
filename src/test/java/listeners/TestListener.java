package listeners;

import base.BaseTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ExtentManager;
import utils.ScreenshotUtil;

public class TestListener implements ITestListener {

    private final ExtentReports extent = ExtentManager.getInstance();
    private ExtentTest test;

    @Override
    public void onTestStart(ITestResult result) {
        test = extent.createTest(result.getMethod().getMethodName());
        test.assignCategory(result.getMethod().getGroups());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.pass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Object instance = result.getInstance();
        if (instance instanceof BaseTest) {
            WebDriver driver = ((BaseTest) instance).getDriver();
            if (driver != null) {
                String base64 = ScreenshotUtil.takeBase64(driver);
                test.fail(result.getThrowable(),
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64, "Failure screenshot").build());
                ScreenshotUtil.saveToFile(driver, result.getMethod().getMethodName());
                return;
            }
        }
        test.fail(result.getThrowable());   // API tests: no screenshot
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.skip("Test skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();   // writes the report file
    }
}