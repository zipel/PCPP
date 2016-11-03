import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executor;
import java.util.Random;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class StripedWriteMapTest {

    private final int nThreads, nTrials;
    private final CyclicBarrier barrier;
    private volatile AtomicInteger count = new AtomicInteger(0);
    private final OurMap<Integer, String> map;
    private static int counter;
    final protected AtomicIntegerArray counts;

    public StripedWriteMapTest(OurMap<Integer, String> map, 
           int nThreads ,int nTrials) {
        this.map = map;
        this.nThreads = nThreads;
        this.nTrials = nTrials;
        counts = new AtomicIntegerArray(nThreads);
        this.barrier = new CyclicBarrier(nThreads + 1);
    }

    void test(ExecutorService pool) {
        try {
            for (int i = 0; i < nThreads; i++) {
                pool.execute(new TestThread());
            }
            barrier.await();
            barrier.await();
            final int[] sum = new int[]{0}; 
            map.forEach((k, v) -> sum[0] += k);
            String msg = String.format("Sum[] = %d, count = %d", sum[0], count.get());
            System.out.println(msg);
            assert count.get() == sum[0];
            map.forEach((k, v) -> {
                String[] arr = v.split(":");
                assert arr.length == 2;
                int key  = Integer.valueOf(arr[1]); 
                int threadId = Integer.valueOf(arr[0]);
                assert key == k;
                assert threadId < 100 && threadId >= 0;
                counts.getAndDecrement(threadId);
            });
            int res = counts.accumulateAndGet(0, 0, (x, y) -> x + y );
            assert  res == 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    class TestThread implements Runnable {
        final int id;
        
        public TestThread() {
            id = counter++;
        }

        public void run() {
            try { 
                Random random = new Random();
                int sum = 0;
                barrier.await();
                for (int i = 0; i < nTrials; i++) {
                    int key1 = random.nextInt(100);
                    String value;
                    if ((value = map.put(key1, String.format("%d:%d", id, key1))) == null) {
                        sum += key1;
                        counts.getAndIncrement(id);
                    } else {
                        int otherId = Integer.valueOf(value.split(":")[0]);
                        counts.getAndDecrement(otherId);
                        counts.getAndIncrement(id);
                    }
                    
                    int key2 = random.nextInt(100);
                    if ((value = map.remove(key2)) != null) {
                        sum -= key2;
                        int otherId = Integer.valueOf(value.split(":")[0]);
                        counts.getAndDecrement(otherId);
                    }

                    int key3 = random.nextInt(100);
                    if (map.putIfAbsent(key3, String.format("%d:%d", id, key3)) == null) {
                        sum += key3;
                        counts.getAndIncrement(id);
                    }
                }
                count.getAndAdd(sum);
                barrier.await();
            } catch (Exception e) {
            }
        }

    }

    
    public static void main(String[] args) {
        OurMap<Integer, String> map = new StripedWriteMap<Integer, String>(77, 7);
        //OurMap<Integer, String> map = new WrapConcurrentHashMap<>();
        StripedWriteMapTest suite = new StripedWriteMapTest(map, 16, 100000);
        ExecutorService exec = Executors.newCachedThreadPool(); 
        suite.test(exec);
        exec.shutdown();
    }

}


    




