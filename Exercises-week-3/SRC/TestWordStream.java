// Week 3
// sestoft@itu.dk * 2015-09-09
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.DoubleStream;
import java.util.function.BinaryOperator;
import java.util.Collection;
public class TestWordStream {
  public static void main(String[] args) {
    String filename = "../words.txt";
	
    //Stream<String> st = readWords(filename);
	//st.limit(100).forEach(System.out::println);
	//Consumer<String> print = System.out::println;
	//readWords(filename).filter(s -> s.length() >= 22).limit(10).forEach(print);
	//readWords(filename).filter(TestWordStream::isPalindrome).forEach(print);
	//readWords(filename).parallel().filter(TestWordStream::isPalindrome).forEach(print);
//	int i= readWords(filename).mapToInt(String::length).max().orElse(-1);
//	System.out.print(i);
	//Map<Integer, List<String>> map = readWords(filename).collect(Collectors.groupingBy(s -> s.length()));
	Stream<String> stream = readWords(filename);
	Map<Map<Character, Integer>, List<String>> mp =  stream.collect(Collectors.groupingBy(TestWordStream::letters));
	

//	Collector<String, ?, Integer> coll =  Collectors.summingInt(String::length);
//	Map<Integer, Integer> map1 = stream.collect(Collectors.groupingBy(String::length, coll));
	//System.out.println(letters("Persistent"));
	//Map<String, Map<Character, Integer>> map2 = stream.map(String::toLowerCase).collect(Collectors.groupingBy(Collectors.group));

	//Function<String, String> toL = String::toLowerCase;
	//Function<String, Map<Character, Integer>> mapChar = TestWordStream::letters;
	//Function<String, Map<Character, Integer>> mpC = toL.andThen(mapChar);
  //  Integer i3 =stream.map(toL.andThen(mapChar)).reduce(0, (i, m) -> i + m.getOrDefault('e', 0), (i1, i2) -> i1 + i2 );
//	System.out.println(i3);
//	Map<Map<Character, Integer>, List<String>> ad = stream.collect(Collectors.groupingBy(TestWordStream::letters));
//	Double i1 = IntStream.range(1, 999_999_999).mapToDouble(x -> 1.0/x).parallel().sum();
//	System.out.print(i1);
//	double count = 0;
//	for(int i = 1; i<=999_999_999; i++)
//		count += 1.0/i;
//	System.out.println(count);
//	final int[] arr = new int[]{0};
//	System.out.println(DoubleStream.generate(() -> { arr[0] += 1; return 1.0/arr[0]; } ).limit(999_999_999).parallel().sum());
		
  }

  public static Stream<String> readWords(String filename) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
	  Stream<String> stream = reader.lines();
	  return stream;
    } catch (IOException exn) { 
      return Stream.<String>empty();
    }
  }

  public static <T> Collection<T> conc(Collection<T> a, Collection<T> b){
  		ArrayList<T> arrl = new ArrayList<>();
		arrl.addAll(a);
		arrl.addAll(b);
		return arrl;
  }

  public static boolean isPalindrome(String s) {
	  char[] ch = s.toCharArray();
	  char[] ch1 = new char[s.length()];
	  for(int i =0 ; i < ch.length; i++){
		  ch1[i] = ch[ch.length -1 - i];
	  }
	  String s1 = new String(ch1);
	  for(int i = 0 ; i < ch.length; i++){
			if(ch[i] != ch1[i])
				return false;
	  }
	  return true; 
  }


  public static Map<Character,Integer> letters(String s) {
    Map<Character,Integer> res = new TreeMap<>();
	char[] arr = s.toCharArray();
	for(Character c : arr){
		res.compute(c, (ch, sum) -> {
			if(sum == null) sum = 1;
			else sum++ ;
			return sum;
		});
	}
    return res;
  }
}
