public class SyncProblem {
	private int c = 0;

	public static void main(String[] args) {
		SyncProblem se = new SyncProblem();
		se.runExample();
	}

	private class Counter implements Runnable {
		private Object lock = new Object();
		public void run() {
			for (int i = 0; i < 1000; i++)
				synchronized(lock) {
					c++;
				}
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
