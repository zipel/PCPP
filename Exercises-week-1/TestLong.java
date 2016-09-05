import java.io.IOException;


public class TestLong{
	public static void main(String[] args) throws IOException{
		final LongCounter lc = new LongCounter();
		Thread[] th = new Thread[2];
		for(int j = 0; j < 2; j++){
			Thread t1 = new Thread(() -> {
				for(int i = 0; i< 10000000; i++)
					lc.increment();

			});
			th[j] = t;
		}	
		th[0].start(); th[1].start();
		while(true){
			System.in.read();
			System.out.println(lc.get());
		}
		
	}
}
