Feature: Purchase the order from Ecommerce Website
	I want to use this template for my feature file
	
	Background:
	Given I landed on Ecommerce Page
	
	@Regression
	Scenario Outline: Positive Test of submitting the order
	
		Given Logged in with username "<userName>" and password "<password>"
		When I add product "<productName>" to cart
		And Checkout "<productName>" and submit the order
		Then "Thankyou for the order." message is diplayed on confirmation Page 
	
		Examples:
		|userName|password|productName|
		|bastian8103@gmail.com|Fenix198103+|ZARA COAT 3|
