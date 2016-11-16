import java.util.concurrent.atomic.AtomicReference;
import java.util.List;
import java.util.ArrayList;
import java.util.function.*;
import java.util.concurrent.Executors;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;

public class SimpleRWTryLock implements RWTryLock {
    final AtomicReference<Holders> ar;

    public SimpleRWTryLock() {
        ar = new AtomicReference<>();
    }
    public boolean readerTryLock() {
        Thread cth = Thread.currentThread();
        Holders h;  
        do {
            h = ar.get();
            if (h == null)
                return ar.compareAndSet(h, new ReaderList(cth, (ReaderList) h));
        } while (h.isWriter());
        return ar.compareAndSet(h, new ReaderList(cth, (ReaderList) h));
    }

    void print() {
        Holders h = ar.get();
        if (h == null) return;
        if (h.isWriter())
            System.out.println(((Writer)h).thread);
        else 
        for (ReaderList l = (ReaderList) h; l != null; l = l.next) 
            System.out.println(l.thread);
    }

    public void readerUnlock() { 
        Thread th = Thread.currentThread();
        Holders hold; 
        do {
            hold = ar.get();
            if (hold == null || hold.isWriter() || !((ReaderList)hold).contains(th)) 
                throw new RuntimeException(hold + " " + th);
        } while(!ar.compareAndSet(hold, ((ReaderList)hold).remove(th)));
    }

    public boolean writerTryLock() {
        return ar.compareAndSet(null, new Writer(Thread.currentThread()));
    }

    public void writerUnlock() {
            Holders holder = ar.get();
            if (!holder.isWriter() || !ar.compareAndSet(holder, null)) 
                throw new RuntimeException(String.format("Thread: %s, does not hold write lock.",
                        Thread.currentThread().toString()));
    }

    private static interface Holders { boolean isWriter(); }

    private static class ReaderList implements Holders {
        private final Thread thread;
        private final ReaderList next;

        public ReaderList(Thread thread, ReaderList next) {
            this.thread = thread;
            this.next = next;
        }

        public boolean isWriter() { return false; }

        public boolean contains(Thread t) {
            for (ReaderList rl = this; rl != null; rl = rl.next)
                if (thread == t) 
                    return true;
            return false;
        }
        
        public ReaderList remove(Thread t) {
            if (t == thread) return next;
            if (next == null) return this;
            return new ReaderList(thread, remove(t, next));
        }

        private static ReaderList remove(Thread t, ReaderList rl) {
            if (rl == null) return null;
            if (t == rl.thread) return rl.next;
            return new ReaderList(rl.thread, remove(t, rl.next));
        }
     }

    private static class Writer implements Holders {
        public final Thread thread;

        public Writer(Thread thread) {
            this.thread = thread;
        }

        public boolean isWriter() { return true;}

    }

    public static void main(String[] args) {
        sequentialTest();
        parallelTest();
    }

    public static void sequentialTest() {
        SimpleRWTryLock lock = new SimpleRWTryLock();
        for (int i = 0; i < 10; i++) {
            assert lock.readerTryLock();
            assert !lock.writerTryLock();
        }

        for (int i = 0; i < 10; i++) {
            lock.readerUnlock();
        }

        assert lock.writerTryLock();
        for (int i = 0; i < 10; i++) {
           assert !lock.writerTryLock(); 
        }

        lock.writerUnlock();
        expectedRunExcFor(l -> l.writerUnlock(), lock);
        expectedRunExcFor(l -> l.readerUnlock(), lock);

        //Should Deadlock
      //  lock.readerTryLock();
      // assert false;
    }

    public static void parallelTest() {
        final SimpleRWTryLock lock = new SimpleRWTryLock();
        // Ensures that the write lock is acquired first
        CyclicBarrier barrier = new CyclicBarrier(4);
        ExecutorService exec = Executors.newCachedThreadPool();
        final Thread[] t = new Thread[0];
        exec.execute( () -> { 
            try { 
                assert lock.writerTryLock();
                barrier.await();
                
                // 'Ensures' the other threads has already reached the second await. 
                // In case that one of threads (represented by @runnable) 
                // has not reached the await it should throw assertion error and 
                // and then the program should not terminate 
                Thread.sleep(1000);
                lock.writerUnlock();
                barrier.await();
            }
            catch (Exception e) {}
        }); 
        Runnable runnable = () -> {
            try { 
                barrier.await();
                assert !lock.writerTryLock();
                barrier.await();
                assert lock.readerTryLock();
            } 
            catch (Exception e) {}
        };
        for (int i = 0; i < 3; i++) 
            exec.execute(runnable);
        exec.shutdown();
        expectedRunExcFor(l -> l.readerUnlock(), lock);
    }

    private static void expectedRunExcFor(Consumer<RWTryLock> f,
            RWTryLock lock) {
        try {
            f.accept(lock);
            assert false;
        } catch (RuntimeException e) {}
    }

}

