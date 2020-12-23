public class WaitWithInterrupt {
	Thread one = new Thread(new One());
	Thread two = new Thread(new Two());
	boolean flag = false;

	private void waitForFlag(boolean waitFor) {
		while (flag != waitFor)
			try {
				Thread.sleep(Long.MAX_VALUE);
			} catch (InterruptedException e) {}
	}

	private void setFlag(boolean val, Thread other) {
		flag = val;
		other.interrupt();
	}

	class One implements Runnable {
		@Override
		public void run() {
			for (int i = 0; i < 10; i++) {
				waitForFlag(false);
				System.out.println(
						Thread.currentThread().getName()
								+ " " + i + " " + flag);
				setFlag(true, two);
			}
		}
	}

	class Two implements Runnable {
		@Override
		public void run() {
			for (int i = 0; i < 10; i++) {
				waitForFlag(true);
				System.out.println(
						Thread.currentThread().getName()
								+ " " + i + " " + flag);
				setFlag(false, one);
			}
		}
	}

	public static void main(String[] args) {
		WaitWithInterrupt s = new WaitWithInterrupt();
		s.one.start();
		s.two.start();
	}
}
