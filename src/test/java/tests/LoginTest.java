
package tests;

import base.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;
import utils.ConfigReader;

public class LoginTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(LoginTest.class);

    @Test(groups = {"smoke"})
    public void validLoginShouldShowProductsPage() {
        log.info("Starting valid login test");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(
                ConfigReader.get("standardUser"),
                ConfigReader.get("password")
        );

        ProductsPage productsPage = new ProductsPage(driver);

        // Verify the Products page title
        Assert.assertEquals(
                productsPage.getPageTitle(),
                "Products"
        );

        // Verify that 6 products are displayed
        Assert.assertEquals(
                driver.findElements(
                        org.openqa.selenium.By.className("inventory_item")
                ).size(),
                6
        );
    }

    @Test(groups = {"regression"})
    public void invalidPasswordShouldShowError() {
        log.info("Starting invalid password test");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(
                ConfigReader.get("standardUser"),
                "wrong_password"
        );

        Assert.assertTrue(
                loginPage.getErrorMessage()
                        .contains("Username and password do not match")
        );
    }

    @Test(groups = {"regression"})
    public void lockedUserShouldSeeLockedMessage() {
        log.info("Starting locked user test");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(
                ConfigReader.get("lockedUser"),
                ConfigReader.get("password")
        );

        Assert.assertTrue(
                loginPage.getErrorMessage().contains("locked out")
        );
    }
}
