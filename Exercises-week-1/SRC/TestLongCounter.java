// For week 1
// sestoft@itu.dk * 2014-08-20, 2015-08-27

import java.io.IOException;

public class TestLongCounter {
	public static void main(String[] args) throws IOException {
  		final LongCounter lc = new LongCounter();
		Thread t1 = new Thread(() -> {
        	for(int i = 0; i < 10000000; i++)
	  		lc.increment();
      		});
    		t1.start();
		Thread t2 = new Thread(() -> {
			for(int i = 0; i < 10000000; i++)
				lc.decrement();
		});
		t2.start();
    		System.out.println("Press Enter to get the current value:");
    		while (true) {
      			System.in.read();         // Wait for enter key
      			System.out.println(lc.get()); 
    		}
  	}
}

class LongCounter {
	private long count = 10000000;
 	public synchronized void increment() {
		count++;
  	}
	
	public synchronized void decrement(){
		count--;
	}
  	public long get() { 
    		return count; 
  	}
}
