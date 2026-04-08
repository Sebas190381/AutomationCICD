package SebastianValenciaProjects.StepDefinitions;

import java.io.IOException;

import org.testng.Assert;
import org.testng.AssertJUnit;

import SebastianValenciaProjects.TestComponents.BaseTest;
import SebastianValenciaProjects.pageObjects.CartPage;
import SebastianValenciaProjects.pageObjects.CheckOutPage;
import SebastianValenciaProjects.pageObjects.ConfirmationPage;
import SebastianValenciaProjects.pageObjects.LandingPage;
import SebastianValenciaProjects.pageObjects.ProductCatalogue;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


public class StepDefinitionImplementation extends BaseTest{

	public LandingPage landingPage;
	public ProductCatalogue productCatalogue;
	public ConfirmationPage confirmationPage;

	@Given("I landed on Ecommerce Page")
	public void i_landed_on_Ecommerce_Page() throws IOException {
		landingPage = launchApplication();
		//
	}

	@Given("Logged in with username {string} and password {string}")
	public void logged_in_with_username_com_and_password(String userName, String password ) {
		productCatalogue= landingPage.loginApplication(userName,password);
	}

	@When ("I add product {string} to cart")
	public void i_add_product_to_cart(String product) {
		//List <WebElement>products = productCatalogue.getProductList();
		productCatalogue.addProductToCart(product);
	}

	@And ("Checkout {string} and submit the order")
	public void checkout_and_submit_the_order(String product) { 
		String country = "india";
		CartPage cartPage = productCatalogue.goToCartPage(); 
		Boolean match = cartPage.verifyProductDisplay(product);
		Assert.assertTrue(match);
		CheckOutPage checkOutPage = cartPage.goTocheckOut();
		checkOutPage.selectCountry(country); confirmationPage =
				checkOutPage.goToSubmitPage(); 
	}

	@Then ("{string} message is diplayed on confirmation Page") 
	public void message_is_diplayed_on_confirmation_Page(String string) {

		String messageToValidate = confirmationPage.LocateFinalMessage();
		AssertJUnit.assertTrue(messageToValidate.equalsIgnoreCase(string));
		driver.close();
	}
	
	@Then ("{string} message is diplayed") 
	public void something_messsage_is_displayed(String string) {
		Assert.assertEquals("Incorrect email or password.", landingPage.getErrorMessage());
		driver.close();
	}
	
	

}
