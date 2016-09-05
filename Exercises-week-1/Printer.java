public class Printer{
	static public void print(){
		synchronized(Printer.class){
			System.out.print("-");
			try{Thread.sleep(50);} catch (InterruptedException e){} 
			System.out.print("|");
		}
	}
	
	public static void main(String[] args){
		Printer p = new Printer();
		Thread[] threads = new Thread[2];
		for(int i = 0; i < 2; i++){
			Thread t = new Thread(() -> {
				while(true)
					Printer.print();
			});
			threads[i] = t;
		}
		for(Thread thread : threads)
			thread.start();
		
	}

}
