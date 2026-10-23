package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;

public class LoginDataTest extends BaseTest {

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][] {
                // username, password, expected result (SUCCESS or the error text)
                {"standard_user",   "secret_sauce", "SUCCESS"},
                {"locked_out_user", "secret_sauce", "locked out"},
                {"standard_user",   "wrong_pass",   "do not match"},
                {"",                "secret_sauce", "Username is required"},
                {"standard_user",   "",             "Password is required"},
                {"invalid_user",    "invalid_pass", "do not match"}
        };
    }

    @Test(dataProvider = "loginData", groups = {"regression"})
    public void loginWithMultipleData(String username, String password, String expected) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);

        if (expected.equals("SUCCESS")) {
            Assert.assertEquals(new ProductsPage(driver).getPageTitle(), "Products");
        } else {
            Assert.assertTrue(loginPage.getErrorMessage().contains(expected));
        }
    }
}