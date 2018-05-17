package org.nianet.plexil.scriptcontext.utilities;

import java.util.Random;

// Randomly generates integer and Boolean values
public class RandomPlx {

	
	// the only instance of the class
	private static final RandomPlx instance = new RandomPlx();
	
	// returns the only instance of the class
	public static RandomPlx getInstance(){
		return instance;
	}

	public static void main(String[] args) {
		for (int i=0;i<100;i++){
			System.out.println(RandomPlx.getInstance().getFloat(0.2f, 200.3f));
		}
	}

	// random generator
	private final Random rand;

	// private constructor
	private RandomPlx(){
		rand = new Random();
	}

	// randomly returns a Boolean value
	public boolean getBoolean(){
		return getInt(0,1) == 0 ? false : true;
	}

	

	
	// randomly chooses an index in the given array and returns the Boolean in that position
	public boolean getBoolean(boolean[] en){
		return en[getInt(0,en.length-1)];
	}
	
	public float getFloat(){
		return getFloat(Float.MIN_VALUE,Float.MAX_VALUE);
	}
	
	
	// randomly returns an integer in the interval [min,max]
	// (this is the main method)
	public float getFloat(float min,float max){
		float r = 0;

		if(min > max){
			throw new RuntimeException("Wrong bounds for random integer generation");
		}
		else {
			float range = max - min + 1;
			float fraction = (range * rand.nextFloat());
			r = (fraction + min);
		}

		return r;
	}

	// randomly returns an Float in the Floaterval [min,Float.MAX_VALUE]
	public Float getFloatAbove(Float min){
		return getFloat(min,Float.MAX_VALUE);
	}

	// randomly returns an Float in the Floaterval [Float.MIN_VALUE,max]
	public Float getFloatBelow(Float max){
		return getFloat(Float.MIN_VALUE,max);
	}

	
	
	// randomly returns an integer in the interval [Integer.MIN_VALUE,Integer.MAX_VALUE]
	public int getInt(){
		return getInt(Integer.MIN_VALUE,Integer.MAX_VALUE);
	}

	// randomly returns an integer in the interval [min,max]
	// (this is the main method)
	public int getInt(int min, int max){
		int r = 0;

		if(min > max){
			throw new RuntimeException("Wrong bounds for random integer generation");
		}
		else {
			long range = (long)max - (long)min + 1;
			long fraction = (long)(range * rand.nextDouble());
			r = (int)(fraction + min);
		}

		return r;
	}



	
	// randomly chooses an index in the given array and returns the integer in that position
	public int getInt(int[] en){
		return en[getInt(0,en.length-1)];
	}

	// randomly returns an integer in the interval [min,Integer.MAX_VALUE]
	public int getIntAbove(int min){
		return getInt(min,Integer.MAX_VALUE);
	}

	// randomly returns an integer in the interval [Integer.MIN_VALUE,max]
	public int getIntBelow(int max){
		return getInt(Integer.MIN_VALUE,max);
	}
}