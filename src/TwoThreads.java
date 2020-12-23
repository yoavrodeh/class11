public class TwoThreads implements Runnable {
	public void run() {
		for (int i = 0; i < 3; i++)
			System.out.println(i + " printed by "
					+ Thread.currentThread().getName());
	}

	public static void main(String args[]) {
		Runnable r = new TwoThreads();
		Thread t1 = new Thread(r);
		Thread t2 = new Thread(r);
		t1.start();
		t2.start();
	}
//		0 printed by Thread-0
//		1 printed by Thread-0
//		0 printed by Thread-1
//		2 printed by Thread-0
//		1 printed by Thread-1
//		2 printed by Thread-1
}