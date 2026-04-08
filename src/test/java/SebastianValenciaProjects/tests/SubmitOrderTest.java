package SebastianValenciaProjects.tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import SebastianValenciaProjects.TestComponents.BaseTest;
import SebastianValenciaProjects.pageObjects.CartPage;
import SebastianValenciaProjects.pageObjects.CheckOutPage;
import SebastianValenciaProjects.pageObjects.ConfirmationPage;
import SebastianValenciaProjects.pageObjects.OrderPage;
import SebastianValenciaProjects.pageObjects.ProductCatalogue;

public class SubmitOrderTest extends BaseTest{

		@Test(dataProvider="getData", groups={"Purchase"})
		public void submitOrder(HashMap<String,String> input) throws IOException
		{
			String country = "india";


			ProductCatalogue productCatalogue = landingPage.loginApplication(input.get("email"), input.get("password"));

			productCatalogue.addProductToCart(input.get("product"));


			CartPage cartPage = productCatalogue.goToCartPage();
			Boolean match = cartPage.verifyProductDisplay(input.get("product"));
			Assert.assertTrue(match);


			CheckOutPage checkOutPage = cartPage.goTocheckOut();
			checkOutPage.selectCountry(country);

			ConfirmationPage confirmationPage = checkOutPage.goToSubmitPage();
			String messageToValidate = confirmationPage.LocateFinalMessage();

			AssertJUnit.assertTrue(messageToValidate.equalsIgnoreCase("Thankyou for the order."));

		}

		@Test(dependsOnMethods = {"submitOrder"},dataProvider="getData", groups={"SebasTests"})

		public void orderHistoryTest(HashMap<String,String> input) {
			ProductCatalogue productCatalogue = landingPage.loginApplication(input.get("email"), input.get("password"));
			OrderPage ordersPage = productCatalogue.goToOrdersPage();
			ordersPage.verifyOrderDisplay(input.get("product"));

		}

		//Extent reports -

		@DataProvider
		public Object[][] getData() throws IOException{
			List<HashMap<String,String>> data = getJsonDataToMap(System.getProperty("user.dir")+"//src//test//java//SebastianValenciaProjects//data//PurchaseOrder.json");
			return new Object [][] {{data.get(0)}, {data.get(1)},{data.get(2)}};

		}


}
