public class TestTimeMemoizer{
	public static void main(String[] args){
		Benchmark.Mark7("Memozeir1", i -> { 
	      ExerciseFactorizer.exerciseFactorizer(new Memoizer1<Long,long[]>(new Factorizer()));
		  return 0.0;
		});
		
		Benchmark.Mark7("Memozeir2", i -> { 
	      ExerciseFactorizer.exerciseFactorizer(new Memoizer2<Long,long[]>(new Factorizer()));
		  return 0.0;
		});
		Benchmark.Mark7("Memozeir3", i -> { 
	      ExerciseFactorizer.exerciseFactorizer(new Memoizer3<Long,long[]>(new Factorizer()));
		  return 0.0;
		});
		Benchmark.Mark7("Memozeir4", i -> { 
	      ExerciseFactorizer.exerciseFactorizer(new Memoizer4<Long,long[]>(new Factorizer()));
		  return 0.0;
		});
		Benchmark.Mark7("Memozeir5", i -> { 
	      ExerciseFactorizer.exerciseFactorizer(new Memoizer5<Long,long[]>(new Factorizer()));
		  return 0.0;
		});
		Benchmark.Mark7("Memozeir0", i -> { 
	      ExerciseFactorizer.exerciseFactorizer(new Memoizer0<Long,long[]>(new Factorizer()));
		  return 0.0;
		});
	}
			
}
