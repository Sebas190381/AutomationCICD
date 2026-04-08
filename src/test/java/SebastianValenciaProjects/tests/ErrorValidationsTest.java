package SebastianValenciaProjects.tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import SebastianValenciaProjects.TestComponents.BaseTest;
import SebastianValenciaProjects.TestComponents.Retry;
import SebastianValenciaProjects.pageObjects.CartPage;
import SebastianValenciaProjects.pageObjects.ProductCatalogue;
public class ErrorValidationsTest extends BaseTest{

		// TODO Auto-generated method stub
		@Test(groups = {"SebasTests"},retryAnalyzer=Retry.class)
		public void LoginErrorValidation() throws IOException, InterruptedException
		{
			landingPage.loginApplication("bastian8103@gmail.com", "Fenix198103");

			Assert.assertEquals("Incorrect email or password.", landingPage.getErrorMessage());
		}

		// TODO Auto-generated method stub
		@Test
		public void ProductErrorValidation() throws IOException, InterruptedException
		{

			String productName = "ZARA COAT 3";

			ProductCatalogue productCatalogue = landingPage.loginApplication("bastian8103@gmail.com", "Fenix198103+");

			productCatalogue.addProductToCart(productName);

			CartPage cartPage = productCatalogue.goToCartPage();

			Boolean match = cartPage.verifyProductDisplay(productName);
			Assert.assertTrue(match);

		}

}
