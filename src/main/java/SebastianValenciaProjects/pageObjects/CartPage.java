package SebastianValenciaProjects.pageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import SebastianValenciaProjects.AbstractComponents.AbstractComponent;

public class CartPage extends AbstractComponent{

	WebDriver driver;


	public CartPage(WebDriver driver) {
		// TODO Auto-generated constructor stub

		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(css=".totalRow button")
	WebElement goToCheckout;

	By goPurchaseButton = By.cssSelector(".totalRow button");
	By item = By.cssSelector(".cartWrap");
	By confirmMessageE = By.cssSelector(".hero-primary");

	public  boolean verifyProductDisplay(String productName)
	{
		  List<WebElement> cartProducts = driver.findElements(By.cssSelector(".cartSection h3"));
		  Boolean match = cartProducts.stream().anyMatch(cartPrduct-> cartPrduct.getText().equalsIgnoreCase(productName));
		  return match;
	}

	public CheckOutPage goTocheckOut() {
		goToCheckout.click();
		CheckOutPage checkOutPage = new CheckOutPage(driver);
		return checkOutPage;

	}

}
