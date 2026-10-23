package tests;

import base.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ProductsPage;
import utils.ConfigReader;

public class CartCheckoutTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(CartCheckoutTest.class);
    private static final String PRODUCT = "sauce-labs-backpack";

    private ProductsPage productsPage;

    // runs AFTER BaseTest.setUp() because parent @BeforeMethod runs first
    @BeforeMethod
    public void loginFirst() {
        new LoginPage(driver).login(ConfigReader.get("standardUser"), ConfigReader.get("password"));
        productsPage = new ProductsPage(driver);
    }

    @Test(groups = {"smoke"})
    public void addProductShouldUpdateCartBadge() {
        log.info("Adding product and checking cart badge");
        productsPage.addToCart(PRODUCT);
        Assert.assertEquals(productsPage.getCartBadgeCount(), 1);
    }

    @Test(groups = {"regression"})
    public void cartShouldContainAddedProduct() {
        log.info("Checking product appears in cart");
        productsPage.addToCart(PRODUCT);
        productsPage.openCart();

        CartPage cartPage = new CartPage(driver);
        Assert.assertEquals(cartPage.getPageTitle(), "Your Cart");
        Assert.assertEquals(cartPage.getItemCount(), 1);
        Assert.assertTrue(cartPage.getItemNames().contains("Sauce Labs Backpack"));
    }

    @Test(groups = {"regression"})
    public void removeProductShouldEmptyCart() {
        log.info("Removing product from cart");
        productsPage.addToCart(PRODUCT);
        productsPage.openCart();

        CartPage cartPage = new CartPage(driver);
        cartPage.removeItem(PRODUCT);
        Assert.assertEquals(cartPage.getItemCount(), 0);
    }

    @Test(groups = {"smoke"})
    public void completeCheckoutShouldShowConfirmation() {
        log.info("Completing full checkout");
        productsPage.addToCart(PRODUCT);
        productsPage.openCart();
        new CartPage(driver).clickCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.fillCustomerInfo("Santhiya", "Devi", "600001");
        checkoutPage.clickContinue();
        checkoutPage.clickFinish();

        Assert.assertEquals(checkoutPage.getConfirmationMessage(), "Thank you for your order!");
    }

    @Test(groups = {"regression"})
    public void checkoutWithoutFirstNameShouldShowError() {
        log.info("Checkout with empty first name");
        productsPage.addToCart(PRODUCT);
        productsPage.openCart();
        new CartPage(driver).clickCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.fillCustomerInfo("", "Devi", "600001");
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.getErrorMessage().contains("First Name is required"));
    }
}