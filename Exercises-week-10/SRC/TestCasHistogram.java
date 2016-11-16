import java.util.concurrent.CyclicBarrier;


class TestCasHistogram {
    private static Timer timer;
    public static void main(String[] args) {
        countPrimeFactorsWithStmHistogram();
    }

    private static void countPrimeFactorsWithStmHistogram() {
        Timer timer = null;
        final Histogram histogram = new CasHistogram(30);
        final int range = 4_000_000;
        final int threadCount = 10, perThread = range / threadCount;
        final CyclicBarrier startBarrier = new CyclicBarrier(threadCount + 1), 
        stopBarrier = startBarrier;
        final Thread[] threads = new Thread[threadCount];
        for (int t=0; t<threadCount; t++) {
            final int from = perThread * t, 
                    to = (t+1 == threadCount) ? range : perThread * (t+1); 
            threads[t] = 
                new Thread(() -> { 
                    try { startBarrier.await(); } 
                    catch (Exception exn) { }

                    for (int p=from; p<to; p++) 
		                histogram.increment(countFactors(p));
	                System.out.print("*");
	                try { stopBarrier.await();} 
                    catch (Exception exn) { }
	            });
            threads[t].start();
        }
    
        try { 
            timer = new Timer();
            startBarrier.await(); 
            timer = new Timer(); 
        } catch (Exception exn) { }

        final Histogram total = new CasHistogram(30);
        for (int i = 0; i < 200; i++) {
            try {Thread.sleep(30);
                total.transferBins(histogram);
            } catch (InterruptedException ie) { }
        }
        try { 
            stopBarrier.await();
            System.out.println("\n" + timer.check());
        } catch (Exception exn) { }
        dump(total);
    
    }

    public static void dump(Histogram histogram) {
        int totalCount = 0;
        for (int bin=0; bin<histogram.getSpan(); bin++) {
        System.out.printf("%4d: %9d%n", bin, histogram.getCount(bin));
        totalCount += histogram.getCount(bin);
        }
        System.out.printf("%9d%n", totalCount);
    }

    public static int countFactors(int p) {
        if (p < 2) 
        return 0;
        int factorCount = 1, k = 2;
        while (p >= k * k) {
        if (p % k == 0) {
            factorCount++;
            p /= k;
        } else 
        k++;
        }
        return factorCount;
    }
}
