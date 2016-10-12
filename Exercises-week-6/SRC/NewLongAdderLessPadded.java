import java.util.concurrent.atomic.AtomicLong;

public class NewLongAdderLessPadded{
	private final AtomicLong[] counters;
	private final static int NSTRIPES = 31;

	public NewLongAdderLessPadded(){
		this.counters = new AtomicLong[NSTRIPES];
		for(int stripe = 0; stripe < NSTRIPES; stripe++){
			counters[stripe] = new AtomicLong();
		}
	}
	public void add(long delta){
		counters[Thread.currentThread().hashCode() % NSTRIPES].addAndGet(delta);
	}

	public long longValue(){
		long result = 0;
		for(AtomicLong al : counters)
			result += al.get();
		return result;
	}
}

