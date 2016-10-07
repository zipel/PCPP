import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
public class Uniquifier<T> implements Runnable{
	final ConcurrentHashMap<T, T> map = new ConcurrentHashMap<>();
	final BlockingQueue<T> input;
	final BlockingQueue<T> output;

	public Uniquifier(BlockingQueue<T> input, 
					BlockingQueue<T> output){
		this.input = input;
		this.output = output;
	}
	@Override
	public void run(){
		while(true){
			T t = input.take();
			T result = map.get(t);
			if(result == null){
				map.put(t ,t);
				output.put(result);
			}
		}
	}
}
