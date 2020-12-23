public class NonSyncExample {
	private int c = 0;

	public static void main(String[] args) {
		NonSyncExample se = new NonSyncExample();
		se.runExample();
	}

	private class Counter implements Runnable {
		public void run() {
			for (int i = 0; i < 1000; i++)
				c++;
		}
	}
	public void runExample() {
		Thread t1 = new Thread(new Counter());
		Thread t2 = new Thread(new Counter());
		t1.start();
		t2.start();
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {}
		System.out.println(c);
	}
}
