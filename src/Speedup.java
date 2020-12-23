public class Speedup {
	private int counter = 0;

	private synchronized void addToCounter(int amount) {
		counter += amount;
	}

	private class PrmCounter implements Runnable {
		private int from, to;

		private PrmCounter(int from, int to) {
			this.from = from;
			this.to = to;
		}

		private boolean isPrime(int n) {
			int bound = (int) Math.sqrt(n);
			for (int i = 2; i <= bound; i++)
				if (n % i == 0)
					return false;
			return true;
		}

		public void run() {
			int count = 0;
			for (int n = from; n < to; n++)
				if (isPrime(n))
					count++;
			addToCounter(count);
		}
	}

	public int numPrimes(int upto, int numThreads) {
		Thread[] ts = new Thread[numThreads];
		for (int i = 0; i < numThreads; i++) {
			int length = upto /  numThreads;
			int from = Math.max(2, length * i);
			int to = i == numThreads - 1 ? 
					upto : length * (i + 1);
			ts[i] = new Thread(new PrmCounter(from, to));
			ts[i].start();
		}
		for (Thread t : ts)
			try {
				t.join();
			} catch (InterruptedException e) {}
		return counter;
	}

	public static void main(String[] args) {
		for (int threads = 1; threads < 15; threads++) {
			long startT = System.currentTimeMillis();
			Speedup s = new Speedup();
			int result = s.numPrimes(10000000, threads);
			System.out.format("%d : %d (time = %d)%n",
					threads, result,
					System.currentTimeMillis()-startT);
		}
	}
	
//	1 : 664579 (time = 6401)
//	2 : 664579 (time = 3522)
//	3 : 664579 (time = 2933)
//	4 : 664579 (time = 2756)
//	5 : 664579 (time = 2183)
//	6 : 664579 (time = 1710)
//	7 : 664579 (time = 1557)
//	8 : 664579 (time = 1469)
//	9 : 664579 (time = 1479)
//	10 : 664579 (time = 1484)
//	11 : 664579 (time = 1481)
//	12 : 664579 (time = 1361)
//	13 : 664579 (time = 1290)
//	14 : 664579 (time = 1215)
}
