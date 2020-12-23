public class SafeArray {
	private int[] a;
	
	public SafeArray(int size) {
		a = new int[size];
	}
	public synchronized void inc(int index, int amount) {
		a[index] += amount;
	}
	
	public synchronized int get(int index) {
		return a[index];
	}
}
