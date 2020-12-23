public class DeadLock {
	public static void main(String[] args) {
		Object lock1 = new Object();
		Object lock2 = new Object();
		
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				synchronized(lock1) {
					synchronized(lock2) {
						System.out.println("one!");
					}
				}
			}
		});
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				synchronized(lock2) {
					synchronized(lock1) {
						System.out.println("two!");
					}
				}
			}
		});
		t1.start();
		t2.start();
	}
}