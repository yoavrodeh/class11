public class TwoLocks {
	private long c1 = 0;
	private long c2 = 0;
	private Object lock1 = new Object();
	private Object lock2 = new Object();

	public void inc1() {
		synchronized (lock1) {
			c1++;
		}
	}
	public void inc2() {
		synchronized (lock2) {
			c2++;
		}
	}
	public long get1() { return c1; }
	public long get2() { return c2; }
}