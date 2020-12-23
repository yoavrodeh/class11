import java.util.Random;

public class BetterSafeArray {
	private int[] a;
	private Object[] locks;

	public BetterSafeArray(int size) {
		a = new int[size];
		locks = new Object[size];
		for (int i = 0; i < size; i++)
			locks[i] = new Object();
	}

	public void inc(int index, int amount) {
		synchronized (locks[index]) {
			a[index] += amount;
		}
	}

	public int get(int index) {
		synchronized (locks[index]) {
			return a[index];
		}
	}
	
	
	// Create an array of size 10, and 5 threads.
	// Each thread increases 100000 random indices 
	// in the array by 1.
	// Check that eventually we get 500000, 
	// and that without synchronization we don't.
	public static void main(String[] args) {
		long startT = System.currentTimeMillis();
		BetterSafeArray bsa = new BetterSafeArray(10);
		Thread[] threads = new Thread[5];
		for (int i = 0; i < 5; i++) {
			threads[i] = new Thread(new Runnable() {
				public void run() {
					Random rand = new Random();
					for (int j = 0; j < 100000; j++)
						bsa.inc(rand.nextInt(10), 1);
				}
			});
			threads[i].start();
		}
		try {
			for (Thread t : threads)
				t.join();
		} catch (InterruptedException e) {
		}
		int sum = 0;
		for (int i = 0; i < 10; i++)
			sum += bsa.get(i);
		System.out.println("Sum is " + sum);
		System.out.println("Time taken " + 
				(System.currentTimeMillis() - startT));
		// Try it with SafeArray instead and see the 
		// improvement in running time.
		// Also try it without the synchronization 
		// and see that sum is wrong.
	}
}
