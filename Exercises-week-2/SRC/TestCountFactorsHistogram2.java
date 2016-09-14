import java.util.concurrent.*;
import java.util.Arrays;
public class TestCountFactorsHistogram2 {
	static final Histogram2 histogram = new Histogram2(1000);
	public static void main(String[] args){
		final int range = 5_000_000;
		parallelN(10, range);
		int count = 0;
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
class Histogram2 implements Histogram {
	private int[] counts;	
	public Histogram2(int bins) {
		this.counts = new int[bins];
	}
	public synchronized void increment(int bin){				       
		counts[bin] = counts[bin] + 1;
	}
	public synchronized  int getCount(int bin){
		return counts[bin];
	}
	public int getSpan() {	
		return counts.length;
	}
	public synchronized int[] getBins(){
		return Arrays.copyOf(counts, counts.length);
	}
}


