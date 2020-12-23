public class InterruptExample implements Runnable {
	@Override
	public void run() {
		for (int i = 0; i < 100; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("Interrupted! ");
				return;
			}
			System.out.println(i);
		}
	}
	
	public static void main(String args[])
			throws InterruptedException {
		Thread t = new Thread(new InterruptExample());
		t.start();
		Thread.sleep(7000);
		t.interrupt();
	}
}