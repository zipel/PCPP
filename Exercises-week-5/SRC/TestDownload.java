// For week 5
// sestoft@itu.dk * 2014-09-19

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Executor;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
public class TestDownload {

  private static final String[] urls = 
  { "http://www.itu.dk", "http://www.di.ku.dk", "http://www.miele.de",
    "http://www.microsoft.com", "http://www.amazon.com", "http://www.dr.dk",
    "http://www.vg.no", "http://www.tv2.dk", "http://www.google.com",
    "http://www.ing.dk", "http://www.dtu.dk", "http://www.eb.dk", 
    "http://www.nytimes.com", "http://www.guardian.co.uk", "http://www.lemonde.fr",   
    "http://www.welt.de", "http://www.dn.se", "http://www.heise.de", "http://www.wsj.com", 
    "http://www.bbc.co.uk", "http://www.dsb.dk", "http://www.bmw.com", "https://www.cia.gov"  };

  private static ExecutorService exec = Executors.newWorkStealingPool();

  public static void main(String[] args) throws IOException {
    String url = "https://www.wikipedia.org/";
    String page = getPage(url, 10);
    System.out.printf("%-30s%n%s%n", url, page);
	benchmarkURL(5, urls, 200);
	
  }

  public static void benchmarkParallel(String[] urls, final int maxLines)
  throws InterruptedException{
	  final ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
	  for(String url : urls){
		  	exec.execute(() -> {
				try{map.put(url, getPage(url, maxLines));
				}catch(IOException e){}
			});
			exec.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
	  }
  }

  public static void benchmarkURL(int numberOfLoops, String[] urls, int maxLines)
  	throws IOException {
	for(int i=0; i<numberOfLoops; i++){
		Map<String, String> map; 
		Timer timer = new Timer();
		map = getPageAsMap(urls, 200);
		System.out.println(timer.check());
		map.forEach((urL, bd) -> System.out.println(urL + ": "+ bd.length()));
	}
  }

  public static String getPage(String url, int maxLines) throws IOException {
    // This will close the streams after use (JLS 8 para 14.20.3):
    try (BufferedReader in 
         = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
      StringBuilder sb = new StringBuilder();
      for (int i=0; i<maxLines; i++) {
        String inputLine = in.readLine();
        if (inputLine == null)
          break;
        else
          sb.append(inputLine).append("\n");
      }
      return sb.toString();
    }
  }
  public static Map<String, String>  getPageAsMap(String[] urls, int maxLines)
  	throws IOException {
	  Map<String, String> map = new HashMap<>();
	  String body;
	  for(String url : urls){
		  body = getPage(url, maxLines);
		  map.put(url, body);
	  }
	  return map;
  }
}

