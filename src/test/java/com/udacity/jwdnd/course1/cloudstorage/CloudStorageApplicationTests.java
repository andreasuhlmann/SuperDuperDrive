package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.File;
import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--start-maximized");
		chromeOptions.addArguments("--remote-allow-origins=*");
		this.driver = new ChromeDriver(chromeOptions);
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password) {
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));
	}

	// Write a Selenium test that verifies that the home page is not accessible without logging in.
	@Test
	public void testHomepageNoLogin() {
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());
	}

	// Write a Selenium test that signs up a new user, logs that user in,
	// verifies that they can access the home page, then logs out
	// and verifies that the home page is no longer accessible.
	@Test
	public void testHomepageLogin() {
		doMockSignUp("Hans", "Gerald", "HG", "123456");
		doLogIn("HG", "123456");

		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Home", driver.getTitle());

		WebElement logoutButton = driver.findElement(By.xpath("/html/body/div/div[1]/form/button"));
		logoutButton.click();

		Assertions.assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());
	}

	private void doNoteCreation(String title, String description) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div[2]/div/div[2]/button")));
		WebElement createNoteButton = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/button"));
		createNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement inputTitle = driver.findElement(By.id("note-title"));
		inputTitle.click();
		inputTitle.sendKeys(title);

		WebElement inputDescription = driver.findElement(By.id("note-description"));
		inputDescription.click();
		inputDescription.sendKeys(description);

		WebElement saveButton = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[2]/div[2]/div/div/div[3]/button[2]"));
		saveButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement notesTabReloaded = driver.findElement(By.id("nav-notes-tab"));
		notesTabReloaded.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		WebElement table = driver.findElement(By.id("userTable"));
		Assertions.assertTrue(table.getText().contains(title));
		Assertions.assertTrue(table.getText().contains(description));
	}

	// Write a Selenium test that logs in an existing user,
	// creates a note and verifies that the note details are visible in the note list.
	@Test
	public void testNoteCreation() {
		doMockSignUp("Hans", "Gerald", "HG1", "123456");
		doLogIn("HG1", "123456");
		doNoteCreation("My Title", "My Description");
	}

	// Write a Selenium test that logs in an existing user with existing notes,
	// clicks the edit note button on an existing note, changes the note data,
	// saves the changes, and verifies that the changes appear in the note list.
	@Test
	public void testNoteChanges() {
		doMockSignUp("Hans", "Gerald", "HG2", "123456");
		doLogIn("HG2", "123456");
		doNoteCreation("My Title", "My Description");

		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div[2]/div/div[2]/div[2]/table/tbody/tr/td[1]/button")));
		WebElement updateNoteButton = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div[2]/table/tbody/tr/td[1]/button"));
		updateNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement inputTitle = driver.findElement(By.id("note-title"));
		inputTitle.clear();
		inputTitle.click();
		inputTitle.sendKeys("New Title");

		WebElement inputDescription = driver.findElement(By.id("note-description"));
		inputDescription.clear();
		inputDescription.click();
		inputDescription.sendKeys("New Description");

		WebElement saveChangesButton = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[2]/div[3]/div/div/div[3]/button[2]"));
		saveChangesButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement notesTabReloaded = driver.findElement(By.id("nav-notes-tab"));
		notesTabReloaded.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		WebElement table = driver.findElement(By.id("userTable"));
		Assertions.assertTrue(table.getText().contains("New Title"));
		Assertions.assertTrue(table.getText().contains("New Description"));
	}

	// Write a Selenium test that logs in an existing user with existing notes,
	// clicks the delete note button on an existing note,
	// and verifies that the note no longer appears in the note list.
	@Test
	public void testNoteDeletion() {
		doMockSignUp("Hans", "Gerald", "HG3", "123456");
		doLogIn("HG3", "123456");
		doNoteCreation("My Title", "My Description");

		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div[2]/div/div[2]/div[2]/table/tbody/tr/td[1]/a")));
		WebElement deleteNoteButton = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div[2]/table/tbody/tr/td[1]/a"));
		deleteNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement notesTabReloaded = driver.findElement(By.id("nav-notes-tab"));
		notesTabReloaded.click();

		WebElement table = driver.findElement(By.id("userTable"));
		Assertions.assertFalse(table.getText().contains("New Title"));
		Assertions.assertFalse(table.getText().contains("New Description"));
	}

	private void doCredentialCreation(String url, String username, String password) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		credentialsTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div[2]/div/div[3]/button")));
		WebElement createCredentialButton = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[3]/button"));
		createCredentialButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement inputUrl = driver.findElement(By.id("credential-url"));
		inputUrl.click();
		inputUrl.sendKeys(url);

		WebElement inputUsername = driver.findElement(By.id("credential-username"));
		inputUsername.click();
		inputUsername.sendKeys(username);

		WebElement inputPassword = driver.findElement(By.id("credential-password"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		WebElement saveButton = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[3]/div[2]/div/div/div[3]/button[2]"));
		saveButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement credentialsTabReloaded = driver.findElement(By.id("nav-credentials-tab"));
		credentialsTabReloaded.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		WebElement table = driver.findElement(By.id("credentialTable"));
		Assertions.assertTrue(table.getText().contains(url));
		Assertions.assertTrue(table.getText().contains(username));
		Assertions.assertTrue(table.getText().contains("***"));
		Assertions.assertFalse(table.getText().contains(password));
	}

    // Write a Selenium test that logs in an existing user,
    // creates a credential and verifies that the credential details are visible in the credential list.
    @Test
    public void testCredentialCreation() {
        doMockSignUp("Hans", "Gerald", "HG4", "123456");
        doLogIn("HG4", "123456");
		doCredentialCreation("www.google.com", "Hans G.", "123456");
    }

    // Write a Selenium test that logs in an existing user with existing credentials,
    // clicks the edit credential button on an existing credential,
    // changes the credential data, saves the changes, and verifies that the changes appear in the credential list.
	@Test
	public void testCredentialChanges() {
		doMockSignUp("Hans", "Gerald", "HG5", "123456");
		doLogIn("HG5", "123456");
		doCredentialCreation("www.google.com", "Hans G.", "123456");

		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div[2]/div/div[3]/div[2]/table/tbody/tr/td[1]/button")));
		WebElement updateCredentialButton = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[3]/div[2]/table/tbody/tr/td[1]/button"));
		updateCredentialButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement inputUrl = driver.findElement(By.id("credential-url"));
		inputUrl.clear();
		inputUrl.click();
		inputUrl.sendKeys("www.bing.com");

		WebElement inputUsername = driver.findElement(By.id("credential-username"));
		inputUsername.clear();
		inputUsername.click();
		inputUsername.sendKeys("H. Gerald");

		WebElement inputPassword = driver.findElement(By.id("credential-password"));
		inputPassword.clear();
		inputPassword.click();
		inputPassword.sendKeys("654321");

		WebElement saveChangesButton = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[3]/div[3]/div/div/div[3]/button[2]"));
		saveChangesButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement credentialsTabReloaded = driver.findElement(By.id("nav-credentials-tab"));
		credentialsTabReloaded.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		WebElement table = driver.findElement(By.id("credentialTable"));
		Assertions.assertTrue(table.getText().contains("www.bing.com"));
		Assertions.assertTrue(table.getText().contains("H. Gerald"));
		Assertions.assertTrue(table.getText().contains("***"));
		Assertions.assertFalse(table.getText().contains("654321"));
	}


    // Write a Selenium test that logs in an existing user with existing credentials,
    // clicks the delete credential button on an existing credential,
    // and verifies that the credential no longer appears in the credential list.
	@Test
	public void testCredentialDeletion() {
		doMockSignUp("Hans", "Gerald", "HG6", "123456");
		doLogIn("HG6", "123456");
		doCredentialCreation("www.google.com", "Hans G.", "123456");

		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div[2]/div/div[3]/div[2]/table/tbody/tr/td[1]/a")));
		WebElement deleteCredentialButton = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[3]/div[2]/table/tbody/tr/td[1]/a"));
		deleteCredentialButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement credentialsTabReloaded = driver.findElement(By.id("nav-credentials-tab"));
		credentialsTabReloaded.click();

		WebElement table = driver.findElement(By.id("credentialTable"));
		Assertions.assertFalse(table.getText().contains("www.google.com"));
		Assertions.assertFalse(table.getText().contains("Hans G."));
	}

}
