import org.junit.Test;

public class Example {

	@Test
	public void testOne() throws InterruptedException {

		TelegramVisitor visitor = new TelegramVisitor();

		visitor.openDialog("rhelp_bot");
		visitor.froding(20, "qwertyuiopasdfghjklzxcvbnm");

		visitor.openDialog("redhelp_bot");
		visitor.froding(20, "qwertyuiopasdfghjklzxcvbnm");
//
//		visitor.openDialog("red_helper_bot");
//		visitor.froding(20, "qwertyuiopasdfghjklzxcvbnm");

		visitor.joiningThreads();
	}
}