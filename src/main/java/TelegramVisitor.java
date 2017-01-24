import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TelegramVisitor {

	String currentTeleBot = "";
	final String PATH = "https://web.telegram.org/#/im?p=@";

	Map<String, WebDriver>		botDriversMap =		new HashMap<String, WebDriver>();
	Map<String, Boolean>		profilesPathsMap =	new HashMap<String, Boolean>();
	List<Thread>				threadsFrodind =	new ArrayList<Thread>();

	TelegramVisitor() {

		profilesPathsMap.put("C:\\Users\\QA\\AppData\\Roaming\\Mozilla\\Firefox\\Profiles\\zfm2045a.default", false);
		profilesPathsMap.put("C:\\Users\\QA\\AppData\\Roaming\\Mozilla\\Firefox\\Profiles\\zfm2045b.default", false);
		profilesPathsMap.put("C:\\Users\\QA\\AppData\\Roaming\\Mozilla\\Firefox\\Profiles\\zfm2045c.default", false);
		profilesPathsMap.put("C:\\Users\\QA\\AppData\\Roaming\\Mozilla\\Firefox\\Profiles\\zfm2045d.default", false);
	}

	public void openDialog(String teleBot) throws InterruptedException {
		
		Thread.sleep(5000);
		
		if (currentTeleBot != teleBot) {
			currentTeleBot = teleBot;
				if (botDriversMap.get(teleBot) == null) {
					String defaultProfilePath = "";

					for (String key : profilesPathsMap.keySet()) {
						if (profilesPathsMap.get(key) == false) {
							defaultProfilePath = key;
							break;
						}
					}
					
					FirefoxProfile profile = new FirefoxProfile(new File(defaultProfilePath));
					WebDriver driver = new FirefoxDriver(profile);
					driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
					//WebDriverRunner.setWebDriver(driver);
					botDriversMap.put(teleBot, driver);
			}
			WebDriverRunner.setWebDriver(botDriversMap.get(teleBot));
			Selenide.open(PATH + currentTeleBot);
		}
	}

	public void sendMessage(String textMessage) {

		$(By.className("composer_rich_textarea")).shouldBe(visible);
		$(By.className("composer_rich_textarea")).sendKeys(textMessage);

		$(By.className("im_submit_send_label")).shouldBe(visible);
		$(By.className("im_submit_send_label")).click();
	}

	public void frod (int count, String message) {

		Thread thread = new Thread(
				() -> {
					String tempDriverName =		new String(currentTeleBot);
					String tempMessage =		new String(message);

					int i = 0;
					while(i < count) {
						System.out.println("\t\tсообщение к " + tempDriverName + "No. " + i++);
						botDriversMap.get(tempDriverName).findElement(By.className("composer_rich_textarea")).sendKeys(tempMessage + " " + i);
						botDriversMap.get(tempDriverName).findElement(By.className("im_submit_send_label")).click();
					}

					botDriversMap.get(tempDriverName).quit();
				}
		);

		thread.start();

		threadsFrodind.add(thread);
	}

	public void joinThreads () throws InterruptedException {
		for (Thread thread : threadsFrodind) {
			thread.join();
		}
	}

	public void quitWebDrivers() {
		for (String key : botDriversMap.keySet()) {
			System.out.println(key);
			botDriversMap.get(key).quit();
		}
	}
}