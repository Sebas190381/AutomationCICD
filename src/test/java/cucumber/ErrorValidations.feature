Feature: Error Validation	
	@Error
	Scenario Outline: Positive Test of submitting the order
		
		Given I landed on Ecommerce Page
		When  Logged in with username "<userName>" and password "<password>"
 		Then "Incorrect email or password." message is diplayed     
	
		Examples:
		|userName|password| 
		|bastian8103@gmail.com|Fenixnn198103+|
