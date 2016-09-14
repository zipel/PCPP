interface Histogram {
  public void increment(int bin);
  public int getCount(int bin);
  public int getSpan();
  default public int[] getBins(){
	  throw new UnsupportedOperationException();
  }
}
