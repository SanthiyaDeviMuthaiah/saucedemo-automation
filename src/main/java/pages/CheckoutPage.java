package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {

    private final By firstName      = By.id("first-name");
    private final By lastName       = By.id("last-name");
    private final By postalCode     = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By finishButton   = By.id("finish");
    private final By errorMessage   = By.cssSelector("[data-test='error']");
    private final By confirmation   = By.className("complete-header");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void fillCustomerInfo(String first, String last, String zip) {
        type(firstName, first);
        type(lastName, last);
        type(postalCode, zip);
    }

    public void clickContinue() {
        click(continueButton);
    }

    public void clickFinish() {
        click(finishButton);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public String getConfirmationMessage() {
        return getText(confirmation);
    }
}