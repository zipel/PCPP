import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.function.IntToDoubleFunction;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
public class Benchmarking{
	private static final ExecutorService exec 
		= Executors.newWorkStealingPool();
	final static AtomicInteger ic = new AtomicInteger();
	public static void main(String[] args){
		Mark7("Loops", i -> {
			for(int j = 0; j < 1000; j++)
				ic.incrementAndGet();
			return ic.doubleValue();
		});
		Mark7("Creates a Task", i -> {
			Runnable e = () -> {
					for(int j = 0; j<1000; j++) 
						ic.incrementAndGet();
				};
			return ic.doubleValue(); 
		});
		Mark7("Creates and Cancels a task: ", i -> {
			Future<Void> f = exec.submit(() -> {
				for(int j = 0; j<1000; j++)
					ic.incrementAndGet();
				return null;
			});
			f.cancel(true);
			return ic.doubleValue();
		});
		Mark7("Creates, submits and gets the task: ", i -> {
			Future<Void> f = exec.submit(() -> {
				for(int j = 0; j<1000; j++)
					ic.incrementAndGet();
				return null;
			});
			try{f.get();}
			catch(ExecutionException | InterruptedException e){}
			
			return 0.0;
		});

		exec.shutdownNow();
	}
	public static double Mark7(String msg, IntToDoubleFunction f){
		int n = 10, count = 1, totalCount = 0;
		double dummy = 0.0, runningTime = 0.0, st = 0.0, sst = 0.0;
		do{
			count *= 2;
			st = sst = 0.0;
			for(int j=0; j<n; j++){
				Timer t = new Timer();
				for(int i=0; i<count; i++)
					dummy += f.applyAsDouble(i);
				runningTime = t.check();
				double time = runningTime * 1e9/ count;
				st += time;
				sst += time * time;
				totalCount += count;
			}
		}while(runningTime < 0.25 && count < Integer.MAX_VALUE/2);
		double mean = st/n, sdev = Math.sqrt((sst - mean * mean *n)/ (n -1));
		System.out.printf("%-25s %15.1f ns %10.2f %10d%n", msg, mean, sdev, count);
		return dummy/ totalCount;
	}


}


