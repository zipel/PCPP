import java.util.concurrent.*;
public class MyAtomicInteger{
	private int count;

	public MyAtomicInteger(){
		this(0);
	}

	public MyAtomicInteger(int count){
		this.count = count;
	}

	public synchronized int addAndGet(int amount){
		count += amount;
		return count;
	}
	public synchronized int get(){
		return count;
	}
}
