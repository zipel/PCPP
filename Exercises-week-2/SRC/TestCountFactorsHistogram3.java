import java.util.concurrent.atomic.AtomicInteger;
class Histogram3 implements Histogram{
	    private AtomicInteger[] counts;
		public Histogram3(int bins){
			this.counts = new AtomicInteger[bins];
			for(int i = 0; i < bins; i++)
				counts[i] = new AtomicInteger();
		}
		@Override
		public void increment(int bin){
			counts[bin].incrementAndGet();
		}

		public int getCount(int bin){
			return counts[bin].get();
		}

		public int  getSpan(){
			return counts.length;
			
		}
		public int[] getBins(){
			int[] arr = new int[counts.length];
			for(int i = 0; i < counts.length; i++)
				arr[i] = counts[i].get();
			return arr;
		}
}


public class TestCountFactorsHistogram3 {
	static final Histogram3  histogram = new Histogram3(1000);
	public static void main(String[] args){
		final int range = 5_000_000;
		int count = 0;
		long start = System.nanoTime();
		parallelN(10, range);
		System.out.println("Time:" + (System.nanoTime() - start));
		for(int i = 0; i<1000; i++){
			System.out.println(i + " : " + histogram.getCount(i));
			count += histogram.getCount(i);
			if(count == range)
				break;
		}
		
	}
	public static void parallelN(int nThreads, int range){
		final int slice = range / nThreads;
		Thread[] threads = new Thread[nThreads];
		for(int i = 0; i < nThreads; i++){
			final int from = i*slice;
			final int to= from +slice;
			Thread t = new Thread(() -> {
				for(int p = from; p < to; p++)
					histogram.increment(TestCountFactors.countFactors(p));   
				
			});
			threads[i] = t;
			t.start();
		}
		try{
			for(Thread t : threads)
				t.join();
		}catch(InterruptedException e){}
	}
}


