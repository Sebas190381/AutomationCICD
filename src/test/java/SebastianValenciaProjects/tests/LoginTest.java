package SebastianValenciaProjects.tests;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import SebastianValenciaProjects.TestComponents.BaseTest;
import SebastianValenciaProjects.pageObjects.ProductCatalogue;

/**
 * LoginTest — Positive Login Test Suite
 *
 * Purpose: Validates that a registered user can successfully log in
 * to the e-commerce application using valid credentials.
 *
 * Extends BaseTest so that:
 *   - @BeforeMethod automatically launches the browser and opens the login page
 *   - @AfterMethod automatically closes the browser after each test
 *   - landingPage is already initialized and ready to use
 */
public class LoginTest extends BaseTest {

    /**
     * Test: successfulLogin
     *
     * Scenario: A registered user logs in with valid email and password.
     *
     * Steps:
     *   1. Open the login page (handled by BaseTest @BeforeMethod)
     *   2. Enter valid credentials and click Login
     *   3. Wait for the Product Catalogue page to load
     *   4. Assert that at least one product is visible — confirming login succeeded
     *
     * Expected result: Product list is displayed with 1 or more items.
     *
     * Group: "SebasTests" — included in the regression suite
     */
    @Test(groups = {"SebasTests"})
    public void successfulLogin() throws IOException {

        // Step 1: Call loginApplication() with valid credentials.
        // This method (defined in LandingPage) fills in the email and password
        // fields, clicks the Login button, and returns a ProductCatalogue object
        // representing the next page the user lands on after a successful login.
        ProductCatalogue productCatalogue = landingPage.loginApplication(
                "bastian8103@gmail.com",  // valid registered email
                "Fenix198103+"            // valid password
        );

        // Step 2: Retrieve the list of products displayed on the catalogue page.
        // getProductList() (defined in ProductCatalogue) waits for the products
        // to be visible in the DOM before returning them, so no extra wait is needed.
        List<WebElement> products = productCatalogue.getProductList();

        // Step 3: Assert that the product list is not empty.
        // If login failed, the app would stay on the login page and no products
        // would load — this assertion would then fail, catching the regression.
        Assert.assertTrue(
                products.size() > 0,
                "Login failed or no products loaded — product list is empty."
        );
    }
}
