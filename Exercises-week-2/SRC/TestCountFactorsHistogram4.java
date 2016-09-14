import java.util.concurrent.atomic.AtomicIntegerArray;

class Histogram4 implements Histogram {
	private AtomicIntegerArray arr;

	public Histogram4(int bins){
		this.arr = new AtomicIntegerArray(bins);
	}

	public void increment(int bin){
		arr.incrementAndGet(bin);
	}

	public int getCount(int bin){
		return arr.get(bin);
	}
	public int getSpan(){
	    return arr.length();
	}
	
	public int[] getBins(){
		int[] tmp = new int[arr.length()];
		for(int i = 0; i < 0; i++)
			tmp[i] = arr.get(i);
		return tmp;
	}



}
public class TestCountFactorsHistogram4{
	static final Histogram4 histogram1 = new Histogram4(23);
	public static void main(String[] args){
		final int range = 5_000_000;
		int count = 0;
		long start = System.nanoTime();
		parallelN(10, range, histogram1);
		System.out.println("Time: " + (System.nanoTime() - start));
		for(int i = 0; i < 23; i++)
			System.out.println(i + " : " + histogram1.getCount(i));
	}

	 
	public static void parallelN(int nThreads, int range, Histogram histogram){
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




