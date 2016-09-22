import java.util.function.IntFunction;
import java.util.Arrays;
public class Exercise3_2{
	public static void main(String[] args){
		final int N = 10_000_001;
		final int[] arr = new int[N];
		Arrays.parallelSetAll(arr, i -> isPrime(i) ? 1 : 0);
		Arrays.parallelPrefix(arr, (i1, i2) -> i1 + i2);
		System.out.println(arr[10_000_000]);
		for(int i = 0; i < N; i+=10){
			System.out.println( arr[i]/(i/Math.log(i)));
		}

		
	
	}
	private static boolean isPrime(int n) {
		int k = 2;
		while (k * k <= n && n % k != 0)
			k++;
		return n >= 2 && k * k > n;
	}
}
