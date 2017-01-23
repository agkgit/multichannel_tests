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
	Map<String, WebDriver> botDriversDict = new HashMap<String, WebDriver>();
	List<Thread> threadsFrodind = new ArrayList<Thread>();

	public void openDialog(String teleBot) {
		if (currentTeleBot != teleBot) {
			currentTeleBot = teleBot;
				if (botDriversDict.get(teleBot) == null) {
					String defaultProfilePath = "C:\\Users\\QA\\AppData\\Roaming\\Mozilla\\Firefox\\Profiles\\zfm2045d.default";
					FirefoxProfile profile = new FirefoxProfile(new File(defaultProfilePath));
					WebDriver driver = new FirefoxDriver(profile);
					driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
					WebDriverRunner.setWebDriver(driver);
					botDriversDict.put(teleBot, driver);
			}
			WebDriverRunner.setWebDriver(botDriversDict.get(teleBot));
			Selenide.open(PATH + currentTeleBot);
		}
	}

	public void sendMessage(String textMessage) {

		$(By.className("composer_rich_textarea")).shouldBe(visible);
		$(By.className("composer_rich_textarea")).sendKeys(textMessage);

		$(By.className("im_submit_send_label")).shouldBe(visible);
		$(By.className("im_submit_send_label")).click();
	}

	public void froding(int count, String message) {

		Thread thread = new Thread(
				() -> {
					String tempDriverName = new String(currentTeleBot);
					String tempMessage = new String(message);
					int i = 0;
					while(i < count) {
						System.out.println("tempDriverName = " + tempDriverName + " tempMessage = " + tempMessage);
						botDriversDict.get(tempDriverName).findElement(By.className("composer_rich_textarea")).sendKeys(tempMessage);
						botDriversDict.get(tempDriverName).findElement(By.className("im_submit_send_label")).click();
						i++;
					}
					botDriversDict.get(tempDriverName).quit();
				}
				);

		thread.start();

		threadsFrodind.add(thread);
	}


	public void joiningThreads () throws InterruptedException {
		for (Thread thread : threadsFrodind) {
			thread.join();
		}
	}

	public void quitWebDrivers() {
		for (String key : botDriversDict.keySet()) {
			System.out.println(key);
			botDriversDict.get(key).quit();
		}
	}
}