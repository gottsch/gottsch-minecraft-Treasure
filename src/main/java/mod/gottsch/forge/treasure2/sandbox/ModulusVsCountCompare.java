package mod.gottsch.forge.treasure2.sandbox;

import java.util.Random;

import org.apache.commons.lang3.time.StopWatch;

public class ModulusVsCountCompare {

	public static void main(String[] args) {
		countCompareTest();
		modulusTest();
	}

	public static void countCompareTest() {
		Random random = new Random();
		int gameTime = random.nextInt(1000000);
		int count = 100001;
		StopWatch watch = new StopWatch();
		watch.start();
		for (int i = 0; i < 10000000; i++) {
			if ( count > 100000 ) {

			}
			random.nextInt(1000000);
		}
		watch.stop();
		System.out.println("Count and Compare Time Elapsed: " + watch.getTime());
	}
	
	public static void modulusTest() {
		Random random = new Random();
		int gameTime = 10000000;//random.nextInt(1000000);
		StopWatch watch = new StopWatch();
		watch.start();
		for (int i = 0; i < 10000000; i++) {
			if ( gameTime % 20 == 0 ) {

			}
			random.nextInt(1000000);
		}
		watch.stop();
		System.out.println("Modulus Time Elapsed: " + watch.getTime());
	}
}
