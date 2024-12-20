package es.codeurjc.cosmic_news;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(classes = CosmicNewsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) 
public class CosmicNewsTests {
    
    @LocalServerPort
    int port;

	WebDriver driver;
	
	@BeforeEach
	public void setup() {
		ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
		options.addArguments("--headless");
		options.addArguments("--window-size=1920,1080");
        driver = new ChromeDriver(options);
	}
	
	@AfterEach
	public void teardown() {
		if(driver != null) {
			driver.quit();
		}
	}

	//USER TESTS
	
	@Test
	public void loginAndEdit() throws InterruptedException {
		driver.get("https://localhost:"+ port +"/");
        
        Thread.sleep(1000);	

        driver.findElement(By.id("profile")).click();
		
		driver.findElement(By.name("username")).sendKeys("xd");
		driver.findElement(By.name("password")).sendKeys("xd");

        Thread.sleep(1000);	
		
		driver.findElement(By.id("accept")).click();

        Thread.sleep(1000);	

        assertThat("Nick: GodPedro").isEqualTo(driver.findElement(By.id("nick")).getText());

		driver.findElement(By.id("edit")).click();

        Thread.sleep(1000);	

		driver.findElement(By.name("nick")).sendKeys("Test");

		Thread.sleep(1000);	
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", driver.findElement(By.id("save")));

        Thread.sleep(1000);	

        assertThat("Nick: GodPedroTest").isEqualTo(driver.findElement(By.id("nick")).getText());
	}

    @Test
	public void register() throws InterruptedException {
		driver.get("https://localhost:"+ port +"/");
        
        Thread.sleep(1000);	

        driver.findElement(By.id("profile")).click();

        Thread.sleep(1000);	

        driver.findElement(By.partialLinkText("Regístrate")).click();;
		
		driver.findElement(By.name("name")).sendKeys("Test");
		driver.findElement(By.name("surname")).sendKeys("User");
        driver.findElement(By.name("nick")).sendKeys("TestUser");
        driver.findElement(By.name("mail")).sendKeys("test");
		driver.findElement(By.name("pass")).sendKeys("test");

        Thread.sleep(1000);	
       
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", driver.findElement(By.id("save")));

        Thread.sleep(1000);	

        driver.findElement(By.id("profile")).click();
		
		driver.findElement(By.name("username")).sendKeys("test");
		driver.findElement(By.name("password")).sendKeys("test");

        Thread.sleep(1000);	
		
		driver.findElement(By.id("accept")).click();

        Thread.sleep(1000);	

        assertThat("Nick: TestUser").isEqualTo(driver.findElement(By.id("nick")).getText());
	}

	//NEWS TESTS

	@Test
	public void likeAndDeleteNews() throws InterruptedException{
		this.loginAdmin();
		Thread.sleep(1000);

		driver.findElement(By.partialLinkText("Adolf Schaller")).click();

		Thread.sleep(1000);

		driver.findElement(By.id("like")).click();

		Thread.sleep(1000);

		assertThat("Likes: 1").isEqualTo(driver.findElement(By.id("numLikes")).getText());

		driver.findElement(By.id("like")).click();

		Thread.sleep(1000);

		assertThat("Likes: 0").isEqualTo(driver.findElement(By.id("numLikes")).getText());

		driver.findElement(By.id("delete")).click();

		Thread.sleep(1000);

		assertTrue(driver.findElements(By.partialLinkText("Adolf Schaller")).isEmpty());

	}

	//PICTURE TESTS

	@Test
	public void likeAndFilterPicture() throws InterruptedException{
		this.loginAdmin();
		Thread.sleep(1000);

		driver.findElement(By.id("pictures")).click();

		Thread.sleep(1000);

		driver.findElement(By.id("loadMore")).click();

		Thread.sleep(1000);

		driver.findElement(By.partialLinkText("lobo")).click();

		Thread.sleep(1000);

		JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", driver.findElement(By.id("like")));

		Thread.sleep(1000);

		assertThat("Likes: 1").isEqualTo(driver.findElement(By.id("numLikes")).getText());

		driver.findElement(By.id("back")).click();

		Thread.sleep(1000);

		driver.findElement(By.name("dropdown")).click();

		Thread.sleep(1000);

		driver.findElement(By.linkText("Likes")).click();

		Thread.sleep(1000);

		assertNotNull(driver.findElement(By.partialLinkText("lobo")));

	}

	//QUIZZ TESTS
	@Test
	public void quizz() throws InterruptedException{
		this.loginAdmin();
		Thread.sleep(1000);

		driver.findElement(By.id("quizzesButton")).click();

		Thread.sleep(1000);

		driver.findElement(By.partialLinkText("Bienvenida")).click();

		Thread.sleep(1000);

		JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", driver.findElement(By.id("option2-1")));
		js.executeScript("arguments[0].click();", driver.findElement(By.id("option1-2")));
		js.executeScript("arguments[0].click();", driver.findElement(By.id("option4-3")));
		js.executeScript("arguments[0].click();", driver.findElement(By.id("option3-4")));
		js.executeScript("arguments[0].click();", driver.findElement(By.id("option1-5")));

		Thread.sleep(1000);

		js.executeScript("arguments[0].click();", driver.findElement(By.id("done")));

		Thread.sleep(1000);

		assertNotNull(driver.findElement(By.id("perfect")));

	}

	// CALENDAR TESTS

	@Test
	public void calendar() throws InterruptedException{
		this.loginAdmin();
		Thread.sleep(1000);

		driver.findElement(By.id("dropdownButton")).click();
		driver.findElement(By.linkText("Calendario")).click();

		Thread.sleep(1000);

		driver.findElement(By.id("addEvent")).click();

		Thread.sleep(1000);

		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("document.getElementsByName('date')[0].value='" + today.format(formatter) + "';");

		driver.findElement(By.name("description")).sendKeys("Test notification");

		Thread.sleep(1000);

		driver.findElement(By.id("save")).click();

		Thread.sleep(1000);

		WebElement dayElement = driver.findElement(By.cssSelector(".day[data-day='" + String.valueOf(today.getDayOfMonth()) + "']"));

		js.executeScript("arguments[0].scrollIntoView(true);", dayElement);

		Thread.sleep(1000);

		dayElement.click();

		Thread.sleep(1000);

		driver.findElement(By.id("notifyButton")).click();

		Thread.sleep(1000);

		driver.findElement(By.id("header-img")).click();

		Thread.sleep(1000);

		assertThat("Test notification").isEqualTo(driver.findElement(By.id("notificationDesc")).getText());

	}

	private void loginAdmin(){
		driver.get("https://localhost:"+ port +"/");

        driver.findElement(By.id("profile")).click();
		
		driver.findElement(By.name("username")).sendKeys("xd");
		driver.findElement(By.name("password")).sendKeys("xd");
		
		driver.findElement(By.id("accept")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
		WebElement headerImg = wait.until(ExpectedConditions.elementToBeClickable(By.id("header-img")));
		headerImg.click();
	}
}
