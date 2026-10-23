package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtil {

    // used to embed the image inside the HTML report
    public static String takeBase64(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    // saves a .png file in reports/screenshots
    public static String saveToFile(WebDriver driver, String testName) {
        File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Path destination = Paths.get("reports/screenshots", testName + "_" + time + ".png");
        try {
            Files.createDirectories(destination.getParent());
            Files.copy(source.toPath(), destination);
        } catch (IOException e) {
            throw new RuntimeException("Could not save screenshot", e);
        }
        return destination.toString();
    }
}