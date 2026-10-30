package SebastianValenciaProjects.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import SebastianValenciaProjects.TestComponents.BaseTest;
import SebastianValenciaProjects.TestComponents.Retry;
import SebastianValenciaProjects.pageObjects.ProductCatalogue;

/**
 * AuthenticationTest — Session Management Test Suite (F1.4 – F1.7)
 *
 * Purpose: Validates session management behaviors of the application:
 *   - F1.4 Logout returns the user to the login page
 *   - F1.5 Password field is masked (type="password")
 *   - F1.6 Session persists across a page refresh
 *   - F1.7 Browser back after logout does not reveal user content
 *
 * Extends BaseTest so that:
 *   - @BeforeMethod automatically launches the browser and opens the login page
 *   - @AfterMethod automatically closes the browser after each test
 *   - landingPage is already initialized and ready to use
 */
public class AuthenticationTest extends BaseTest {

	private static final String VALID_EMAIL = "bastian8103@gmail.com";
	private static final String VALID_PASSWORD = "Fenix198103+";

	/**
	 * Test: F1.4 — logoutReturnsToLoginPage
	 *
	 * Scenario: A logged-in user clicks "Sign Out" in the navbar.
	 *
	 * Steps:
	 *   1. Log in with valid credentials (BaseTest opens the page)
	 *   2. Click Sign Out in the navbar
	 *   3. Wait for the login page to reappear
	 *
	 * Expected result: The login form (email field) is visible again,
	 * confirming the session was terminated.
	 *
	 * Group: "SebasTests" — included in the regression suite
	 */
	@Test(groups = {"SebasTests"}, retryAnalyzer = Retry.class)
	public void logoutReturnsToLoginPage() {
		ProductCatalogue productCatalogue = landingPage.loginApplication(VALID_EMAIL, VALID_PASSWORD);
		productCatalogue.logout();
		// logout() waits for the login email field to be visible again;
		// reaching this point without a TimeoutException means logout worked
	}

	/**
	 * Test: F1.5 — passwordFieldIsMasked
	 *
	 * Scenario: Security check on the login form.
	 *
	 * Steps:
	 *   1. Open the login page (handled by BaseTest @BeforeMethod)
	 *   2. Read the "type" attribute of the password input
	 *
	 * Expected result: The password input has type="password" so the
	 * browser renders dots instead of plain text.
	 *
	 * Group: "SebasTests" — included in the regression suite
	 */
	@Test(groups = {"SebasTests"}, retryAnalyzer = Retry.class)
	public void passwordFieldIsMasked() {
		String passwordType = landingPage.getPasswordFieldType();
		Assert.assertEquals(passwordType, "password",
				"Password input must be masked (type='password')");
	}

	/**
	 * Test: F1.6 — sessionPersistsOnRefresh
	 *
	 * Scenario: A logged-in user refreshes the browser.
	 *
	 * Steps:
	 *   1. Log in with valid credentials
	 *   2. Refresh the page (driver.navigate().refresh())
	 *   3. Wait for the product catalogue to render again
	 *
	 * Expected result: The user is still authenticated — the product
	 * catalogue is visible without being asked to log in again.
	 *
	 * Group: "SebasTests" — included in the regression suite
	 */
	@Test(groups = {"SebasTests"}, retryAnalyzer = Retry.class)
	public void sessionPersistsOnRefresh() {
		ProductCatalogue productCatalogue = landingPage.loginApplication(VALID_EMAIL, VALID_PASSWORD);
		productCatalogue.getProductList(); // ensure catalogue is loaded before refresh
		driver.navigate().refresh();
		ProductCatalogue refreshedCatalogue = new ProductCatalogue(driver);
		Assert.assertFalse(refreshedCatalogue.getProductList().isEmpty(),
				"User should remain logged in after page refresh");
	}

	/**
	 * Test: F1.7 — backAfterLogoutShowsNoUserData
	 *
	 * Scenario: A user logs out, then presses the browser back button.
	 *
	 * Steps:
	 *   1. Log in with valid credentials
	 *   2. Click Sign Out
	 *   3. Press browser back (driver.navigate().back())
	 *   4. Check what page is rendered
	 *
	 * Expected result: The user must NOT see the product catalogue again.
	 * The app should either stay on the login page or redirect to it.
	 *
	 * Group: "SebasTests" — included in the regression suite
	 */
	@Test(groups = {"SebasTests"}, retryAnalyzer = Retry.class)
	public void backAfterLogoutShowsNoUserData() {
		ProductCatalogue productCatalogue = landingPage.loginApplication(VALID_EMAIL, VALID_PASSWORD);
		productCatalogue.logout();
		driver.navigate().back();
		// After back-navigation the app must NOT show the catalogue.
		// The login form must be visible (either it never left, or the
		// app redirected to it because the session token was cleared).
		waitForLoginFieldToAppear();
	}

	// ------------------------------------------------------------------
	// Helpers
	// ------------------------------------------------------------------

	private void waitForLoginFieldToAppear() {
		WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(20));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userEmail")));
	}
}
