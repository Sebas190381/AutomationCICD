package SebastianValenciaProjects.pageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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
		// Native click required here: JS click fires before Angular's (click)
		// handler processes the event, so the product is never added to the cart
		// (toast shows but cart stays empty). This button is never overlaid.
		addToCartButton.click();
		waitForElementToAppear(toastMessage);
		waitForElementToDisappear(spinner);
	}

}
