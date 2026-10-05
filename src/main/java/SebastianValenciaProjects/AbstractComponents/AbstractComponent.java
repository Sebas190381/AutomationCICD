package SebastianValenciaProjects.AbstractComponents;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import SebastianValenciaProjects.pageObjects.CartPage;
import SebastianValenciaProjects.pageObjects.OrderPage;

public class AbstractComponent {
	WebDriver driver;


	public AbstractComponent(WebDriver driver) {

		this.driver = driver;
	}

	@FindBy(css="[routerlink*='cart']")
	WebElement cartHeader;

	@FindBy(css="[routerlink*='myorders']")
	WebElement myOrders;

	public void waitForElementToAppear(By findBy){
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(30));
		wait.until(ExpectedConditions.visibilityOfElementLocated(findBy));
	}

	public void waitForWebElementToAppear(WebElement findBy){
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(30));
		wait.until(ExpectedConditions.visibilityOfAllElements(findBy));
	}

	public void waitForElementToDisappear(WebElement webE){
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(30));
		wait.until(ExpectedConditions.invisibilityOf(webE));
	}


	public OrderPage goToOrdersPage() {
		try {
			myOrders.click();
		} catch (WebDriverException e) {
			// Fallback: overlay may intercept the click, click via JavaScript instead
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", myOrders);
		}
		OrderPage orderPage = new OrderPage(driver);
		return orderPage;
	}

	public CartPage goToCartPage() {
		try {
			cartHeader.click();
		} catch (WebDriverException e) {
			// Fallback: overlay may intercept the click, click via JavaScript instead
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", cartHeader);
		}
		CartPage cartPage = new CartPage(driver);
		return cartPage;
	}

}
