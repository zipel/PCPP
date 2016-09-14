import java.util.concurrent.atomic.LongAdder;
public class TestCountFactorsHistogram5 {
	public static void main(String[] args){
		final int range = 5000000;
	    final Histogram histogram = new Histogram5(23);
		int count = 0;
		long start = System.nanoTime();
		TestCountFactorsHistogram4.parallelN(10, range, histogram);	
		System.out.println("Time: " + (System.nanoTime() - start));	
		for(int i = 0; i < 23; i++)	
			System.out.println(i + " : " + histogram.getCount(i));
	}

}
class Histogram5 implements Histogram {
	private final LongAdder[] longAdder;

	public Histogram5(int bins){
		longAdder = new LongAdder[bins];
		for(int i = 0; i < bins; i++)
			longAdder[i] = new LongAdder();
	}

	public void increment(int bin){
		longAdder[bin].increment();
	}
	public int getCount(int bin){
		return longAdder[bin].intValue();
	}
	public int getSpan(){
		return longAdder.length;
	}
	public int[] getBins(){
		int[] tmp = new int[longAdder.length];
		for(int i = 0; i < longAdder.length; ++i){
			tmp[i] = longAdder[i].intValue();
		}
		return tmp;
	}
}
