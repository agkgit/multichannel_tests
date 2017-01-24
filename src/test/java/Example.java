import org.junit.Test;

public class Example {

	@Test
	public void testOne() throws InterruptedException {

		TelegramVisitor visitor = new TelegramVisitor();

		visitor.openDialog("rhelp_bot");
		visitor.frod(40, "rhelp_bot");

		visitor.openDialog("redhelp_bot");
		visitor.frod(40, "redhelp_bot");

		visitor.openDialog("red_helper_bot");
		visitor.frod(40, "red_helper_bot");

		visitor.joinThreads();
	}
}