public class SharedMemory implements Runnable {
	private int i;
	
	public void run() {
		while (i < 6) 
			System.out.println(i++ + " printed by "
					+ Thread.currentThread().getName());
	}

	public static void main(String args[]) {
		Runnable r = new SharedMemory();
		Thread t1 = new Thread(r);
		Thread t2 = new Thread(r);
		t1.start();
		t2.start();
	}
//	1 printed by Thread-0
//	2 printed by Thread-0
//	0 printed by Thread-1
//	3 printed by Thread-0
//	4 printed by Thread-1
//	5 printed by Thread-0
}