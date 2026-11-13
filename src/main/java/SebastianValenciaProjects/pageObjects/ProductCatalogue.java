package SebastianValenciaProjects.pageObjects;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;

import SebastianValenciaProjects.AbstractComponents.AbstractComponent;

public class ProductCatalogue extends AbstractComponent{
	WebDriver driver;
	Actions a;


	public ProductCatalogue(WebDriver driver ) {
		super(driver);
		this.a = new Actions (driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}


	@FindBy(css=".mb-3")
	List<WebElement> products;

	@FindBy(css=".ng-animating")
	WebElement spinner;

	@FindBy(css="input[placeholder='search']")
	WebElement searchInput;

	@FindBy(css="[routerlink*='cart'] label")
	WebElement cartBadge;



	By productsBy = By.cssSelector(".mb-3");
	By addToCart = By.cssSelector(".card-body button:last-of-type");
	By toastMessage = By.cssSelector( "#toast-container");
	By toastMessage2 = By.cssSelector(".ng-animating");



	public List<WebElement> getProductList() {
		waitForElementToAppear(productsBy);
		return products;
	}

	public WebElement getProductByName(String productName) {
		System.out.println(getProductList().size());
		 WebElement prod = getProductList().stream().filter(product->
		  product.findElement(By.cssSelector("b")).getText().equals(productName)).
		  findFirst().orElse(null);
		return prod;
	}

	public void addProductToCart(String productName) {
		WebElement prod = getProductByName(productName);
		WebElement addToCartButton = prod.findElement(addToCart);
		// Capture the badge before the click so we can detect the change
		String badgeBefore = cartBadge.getText().trim();
		// Native click required here: JS click fires before Angular's (click)
		// handler processes the event, so the product is never added to the cart
		// (toast shows but cart stays empty). This button is never overlaid.
		// Cross-browser signal: wait for the cart badge to change. Firefox
		// headless renders the toast with empty text, so a toast-text wait
		// times out there even though the add succeeded. The badge only
		// increments after the add commits, so this also covers the Chrome
		// race where the "Login Successfully" toast was still visible.
		// Occasionally the click is swallowed (catalogue re-render in
		// Firefox headless), so retry the click up to 3 times.
		for (int attempt = 0; attempt < 3; attempt++) {
			try {
				addToCartButton.click();
			} catch (ElementClickInterceptedException e) {
				// The full-screen loading spinner can overlay the button
				// right after login — wait for it to clear, then retry
				waitForElementToDisappear(spinner);
			}
			try {
				new WebDriverWait(driver, Duration.ofSeconds(5))
						.until(d -> !cartBadge.getText().trim().equals(badgeBefore));
				break; // add committed
			} catch (TimeoutException e) {
				if (attempt == 2) {
					throw e;
				}
			}
		}
		waitForElementToDisappear(spinner);
	}

	/**
	 * Types a search term into the catalogue search box and waits for
	 * the product list to re-render with the filtered results.
	 */
	public void searchProduct(String searchTerm) {
		// The search box lives in the filter sidebar, which is off-viewport
		// in headless CI — scroll it into view before interacting
		((JavascriptExecutor) driver).executeScript(
				"arguments[0].scrollIntoView({block:'center'});", searchInput);
		try {
			searchInput.clear();
			searchInput.sendKeys(searchTerm);
		} catch (ElementNotInteractableException e) {
			// Fallback: set the value via JS and fire the 'input' event so
			// Angular's ngModel picks it up (same headless limitation)
			((JavascriptExecutor) driver).executeScript(
					"arguments[0].value = arguments[1];"
					+ "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));",
					searchInput, searchTerm);
		}
		// Angular filters the list asynchronously; wait until no visible
		// product contradicts the filter (list re-rendered)
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		wait.until(d -> getProductList().stream()
				.allMatch(p -> p.getText().toLowerCase().contains(searchTerm.toLowerCase())));
	}

	/**
	 * Returns the cart badge count from the navbar.
	 * Returns 0 when the badge label is empty (no items in cart).
	 */
	public int getCartBadgeCount() {
		String badgeText = cartBadge.getText().trim();
		return badgeText.isEmpty() ? 0 : Integer.parseInt(badgeText);
	}

}
