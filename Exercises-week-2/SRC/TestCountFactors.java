// For week 2
// sestoft@itu.dk * 2014-08-29

class TestCountFactors {
  public static void main(String[] args) {
    final int range = 5_000_000;
    int count = 0;
	long start = System.nanoTime();
    for (int p=0; p<range; p++)
      count += countFactors(p);
    System.out.printf("Total number of factors is %9d%n", count);
	System.out.println("Time:" + (System.nanoTime() - start));
  }

  public static int countFactors(int p) {
    if (p < 2) 
      return 0;
    int factorCount = 1, k = 2;
    while (p >= k * k) {
      if (p % k == 0) {
		factorCount++;
		p /= k;
      } else 
		k++;
    }
    return factorCount;
  }
}
