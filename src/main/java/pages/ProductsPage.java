package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductsPage extends BasePage {

    private final By pageTitle    = By.className("title");
    private final By productItems = By.className("inventory_item");
    private final By cartLink     = By.className("shopping_cart_link");
    private final By cartBadge    = By.className("shopping_cart_badge");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public int getProductCount() {
        waitForVisible(productItems);
        return driver.findElements(productItems).size();
    }

    // example productId: "sauce-labs-backpack"
    public void addToCart(String productId) {
        click(By.id("add-to-cart-" + productId));
    }

    public void removeFromCart(String productId) {
        click(By.id("remove-" + productId));
    }

    public int getCartBadgeCount() {
        if (driver.findElements(cartBadge).isEmpty()) {
            return 0;
        }
        return Integer.parseInt(getText(cartBadge));
    }

    public void openCart() {
        click(cartLink);
    }
}