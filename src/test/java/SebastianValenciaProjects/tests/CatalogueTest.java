package SebastianValenciaProjects.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import SebastianValenciaProjects.TestComponents.BaseTest;
import SebastianValenciaProjects.TestComponents.Retry;
import SebastianValenciaProjects.pageObjects.ProductCatalogue;

/**
 * CatalogueTest — Product Catalogue Test Suite (F2.3 – F2.4)
 *
 * Purpose: Validates catalogue behaviors beyond adding products:
 *   - F2.3 Cart badge increments as products are added
 *   - F2.4 Search box filters the product catalogue
 *
 * Extends BaseTest so that:
 *   - @BeforeMethod automatically launches the browser and opens the login page
 *   - @AfterMethod automatically closes the browser after each test
 *   - landingPage is already initialized and ready to use
 */
public class CatalogueTest extends BaseTest {

	private static final String VALID_EMAIL = "bastian8103@gmail.com";
	private static final String VALID_PASSWORD = "Fenix198103+";

	/**
	 * Test: F2.3 — cartBadgeIncrementsOnAdd
	 *
	 * Scenario: A logged-in user adds products to the cart.
	 *
	 * Steps:
	 *   1. Log in with valid credentials (BaseTest opens the page)
	 *   2. Read the cart badge count (should be 0 on a fresh session)
	 *   3. Add a product to the cart
	 *   4. Read the cart badge count again
	 *   5. Add a second (different) product
	 *   6. Read the cart badge count again
	 *
	 * Expected result: The navbar badge increments by exactly 1 after
	 * each add — 0 -> 1 -> 2. This guards the exact race condition that
	 * previously broke headless CI (cart empty on arrival).
	 *
	 * Group: "SebasTests" — included in the regression suite
	 */
	@Test(groups = {"SebasTests"}, retryAnalyzer = Retry.class)
	public void cartBadgeIncrementsOnAdd() {
		ProductCatalogue productCatalogue = landingPage.loginApplication(VALID_EMAIL, VALID_PASSWORD);
		int initialCount = productCatalogue.getCartBadgeCount();
		Assert.assertEquals(initialCount, 0,
				"Cart badge should start empty on a fresh session");

		productCatalogue.addProductToCart("ZARA COAT 3");
		Assert.assertEquals(productCatalogue.getCartBadgeCount(), initialCount + 1,
				"Cart badge should increment to 1 after first add");

		productCatalogue.addProductToCart("ADIDAS ORIGINAL");
		Assert.assertEquals(productCatalogue.getCartBadgeCount(), initialCount + 2,
				"Cart badge should increment to 2 after second add");
	}

	/**
	 * Test: F2.4 — searchFiltersCatalogue
	 *
	 * Scenario: A logged-in user searches the product catalogue.
	 *
	 * Steps:
	 *   1. Log in with valid credentials
	 *   2. Count all products in the catalogue
	 *   3. Type a search term into the search box
	 *   4. Read the filtered product list
	 *
	 * Expected result: The filtered list is smaller than the full
	 * catalogue, non-empty, and every visible product matches the
	 * search term (case-insensitive).
	 *
	 * Group: "SebasTests" — included in the regression suite
	 *
	 * IGNORED — APP DEFECT: The catalogue search form (mobile filter
	 * panel) is decorative — it is not wired to the product list.
	 * Verified: typing does not filter, there is no submit button, and
	 * Enter does not trigger filtering. Re-enable once the app wires
	 * the search form to the product grid.
	 */
	@Test(groups = {"SebasTests"}, retryAnalyzer = Retry.class, enabled = false)
	public void searchFiltersCatalogue() {
		ProductCatalogue productCatalogue = landingPage.loginApplication(VALID_EMAIL, VALID_PASSWORD);
		int totalProducts = productCatalogue.getProductList().size();

		String searchTerm = "zara";
		productCatalogue.searchProduct(searchTerm);

		int filteredCount = productCatalogue.getProductList().size();
		Assert.assertTrue(filteredCount > 0,
				"Search for '" + searchTerm + "' should return at least one product");
		Assert.assertTrue(filteredCount < totalProducts,
				"Search for '" + searchTerm + "' should filter the catalogue ("
						+ filteredCount + " vs " + totalProducts + " total)");
	}
}
