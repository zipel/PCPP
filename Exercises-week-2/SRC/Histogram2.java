public class Histogram2 implements Histogram {
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
}
