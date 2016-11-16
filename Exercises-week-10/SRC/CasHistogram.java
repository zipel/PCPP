import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.Executors;
import java.util.concurrent.Executor;

public class CasHistogram implements Histogram {
    private final AtomicInteger[] counts;

    public CasHistogram(int bin) {
        counts = new AtomicInteger[bin];

        for (int i = 0; i < bin; i ++) {
            counts[i] = new AtomicInteger(0);
        }
    }

    public void increment(int bin) {
        AtomicInteger ai = counts[bin];
        int oldV,  newV;
        do {
            oldV = ai.get();
            newV = oldV + 1;
        } while (!ai.compareAndSet(oldV, newV));
    }
    
    public int getCount(int bin) {
        return counts[bin].get();
    }

    public int getSpan() {
        return counts.length;
    }
    
    public int[] getBins() {
        final int[] res = new int[counts.length];
        Executor exec = Executors.newCachedThreadPool();
        exec.execute( () -> {
            for (int i = 0; i < res.length; i++) {
                final int j = i;
                res[j] = counts[j].get();
            }
        });
        return res;
    }
    
    public int getAndClear(int bin) {
        AtomicInteger ai = counts[bin];
        int oldV;
        do {
            oldV = ai.get();
        } while (!ai.compareAndSet(oldV, 0));
        return oldV;
    }

    public void transferBins(Histogram hist) {
        int thisSpan = this.counts.length,
            histSpan = hist.getSpan(),
            span = Math.min(thisSpan, histSpan);

        for (int i = 0; i < span; i++) {
            int newV, oldV;
            AtomicInteger ai = counts[i];
            do {
                oldV = ai.get();
                newV = hist.getAndClear(i) + oldV;
            } while (!ai.compareAndSet(oldV, newV));
        }
    }

}
