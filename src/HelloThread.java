public class HelloThread implements Runnable {

	public void run() {
		System.out.println("Hello from a thread!");
	}

	public static void main(String args[]) {
		Thread t = new Thread(new HelloThread());
		t.start();
	}
}