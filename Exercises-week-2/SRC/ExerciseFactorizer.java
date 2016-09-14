public class ExerciseFactorizer {
	public static void main(String[] args){
		Factorizer f = new Factorizer();
		//exerciseFactorizer(new Memoizer1<Long, long[]>(f));
		//exerciseFactorizer(new Memoizer2<Long, long[]>(f));
		//exerciseFactorizer(new Memoizer3<Long, long[]>(f));
		//exerciseFactorizer(new Memoizer4<Long, long[]>(f));	
		//exerciseFactorizer(new Memoizer5<Long, long[]>(f));
		exerciseFactorizer(new Memoizer0<Long, long[]>(f));
		System.out.println(f.getCount());
	}
	public static void exerciseFactorizer(Computable<Long, long[]> f){
		final int threadCount = 16;
		final long start = 10_000_000_000L, range = 20_000L;
		System.out.println(f.getClass());
		Thread[] threads = new Thread[threadCount];
		for( int t = 0; t < threadCount; t++){
			final long from1 = start, to1 = from1 + range;
			final long from2 = start + range + t * range/4, to2 = from2 + range;
			Thread thread = new Thread(() -> {
				long k = from1;
				while(k<to1){
					try{
						f.compute(k);
					}catch(InterruptedException e){}
					k++;
				}
				k = from2;
				while(k<to2){
					try{
						f.compute(k);
					}catch(InterruptedException e){}
					k++;

				}
			});
			thread.start();
			threads[t] = thread;
		}
		for(int t = 0; t < threadCount; ++t)
			try{
				threads[t].join();
			}catch(InterruptedException e){}
	}
}
