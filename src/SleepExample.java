public class SleepExample {
	public static void main(String args[])
			throws InterruptedException {
		String importantInfo[] = {"Mares eat oats",
				"Does eat oats", "Little lambs eat ivy",
				"A kid will eat ivy too"};

		for (int i = 0; i < importantInfo.length; i++) {
			Thread.sleep(2000);
			System.out.println(importantInfo[i]);
		}
	}
}