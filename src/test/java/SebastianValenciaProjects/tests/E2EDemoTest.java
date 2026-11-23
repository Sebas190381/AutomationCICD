package SebastianValenciaProjects.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import SebastianValenciaProjects.TestComponents.BaseTest;
import SebastianValenciaProjects.pageObjects.CartPage;
import SebastianValenciaProjects.pageObjects.CheckOutPage;
import SebastianValenciaProjects.pageObjects.ConfirmationPage;
import SebastianValenciaProjects.pageObjects.OrderPage;
import SebastianValenciaProjects.pageObjects.ProductCatalogue;

/**
 * E2EDemoTest — Single end-to-end purchase demo.
 *
 * One test that walks the complete user journey in a single session:
 *
 *   login -> catalogue -> add to cart -> verify cart
 *         -> checkout -> select country -> place order
 *         -> verify confirmation -> verify order in order history
 *
 * Purpose-built for the E2E demo evidence: exactly one iteration,
 * one product, deterministic data, run through the Selenium Grid.
 *
 * Run with:
 *   mvn test -Dsurefire.suiteXmlFiles=testSuites/E2EDemo.xml -Dbrowser=grid
 */
public class E2EDemoTest extends BaseTest {

	private static final String EMAIL = "bastian8103@gmail.com";
	private static final String PASSWORD = "Fenix198103+";
	private static final String PRODUCT = "ZARA COAT 3";
	private static final String COUNTRY = "india";

	@Test(groups = {"E2EDemo"}, retryAnalyzer = SebastianValenciaProjects.TestComponents.Retry.class)
	public void fullPurchaseFlow() {

		// 1. Login
		ProductCatalogue productCatalogue = landingPage.loginApplication(EMAIL, PASSWORD);

		// 2. Add product to cart
		productCatalogue.addProductToCart(PRODUCT);

		// 3. Verify product in cart
		CartPage cartPage = productCatalogue.goToCartPage();
		Assert.assertTrue(cartPage.verifyProductDisplay(PRODUCT),
				PRODUCT + " should be displayed in the cart");

		// 4. Checkout and select country
		CheckOutPage checkOutPage = cartPage.goTocheckOut();
		checkOutPage.selectCountry(COUNTRY);

		// 5. Place the order
		ConfirmationPage confirmationPage = checkOutPage.goToSubmitPage();
		String confirmationMessage = confirmationPage.LocateFinalMessage();
		Assert.assertTrue(confirmationMessage.equalsIgnoreCase("Thankyou for the order."),
				"Expected order confirmation, got: " + confirmationMessage);

		// 6. Verify the order appears in order history
		OrderPage ordersPage = productCatalogue.goToOrdersPage();
		Assert.assertTrue(ordersPage.verifyOrderDisplay(PRODUCT),
				PRODUCT + " should appear in order history");
	}
}
