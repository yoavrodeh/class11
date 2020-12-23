public class InterruptExample2 implements Runnable {
	@Override
	public void run() {
		long count = 0;
		while (!Thread.interrupted())
			count++;
		System.out.println("Counted " + count);
		System.out.println(Thread.interrupted()); 
		// false
	}

	public static void main(String[] args)
			throws InterruptedException {
		Thread t = new Thread(new InterruptExample2());
		t.start();
		Thread.sleep(100);
		t.interrupt();
	}
}
