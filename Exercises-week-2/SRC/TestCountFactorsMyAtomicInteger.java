import java.util.concurrent.atomic.AtomicInteger;
public class TestCountFactorsMyAtomicInteger{
	static final MyAtomicInteger counter = new MyAtomicInteger();
	public static void main(String[] args){
		long start = System.nanoTime();
		parallelN(10, 5_000_000);
		System.out.println("Time:" + (System.nanoTime() - start));
		System.out.println(counter.get());

	}



	public static void parallelN(int nThreads, int range){
		final int slice = range / nThreads;
		Thread[] threads = new Thread[nThreads];
		for(int i = 0; i < nThreads; i++){
			final int from = i*slice;
			final int to= from +slice;
			Thread t = new Thread( new Runnable() {
				public void run(){
					for(int p = from; p < to; p++){
						counter.addAndGet(TestCountFactors.countFactors(p));			
					}
				}
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
