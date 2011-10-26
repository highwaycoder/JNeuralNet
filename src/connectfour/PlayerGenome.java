package connectfour;

import java.util.Random;

import JNeuralNet.Genome;

public class PlayerGenome implements Genome {

	static final int NUM_INPUTS = 5;
	static final int NUM_OUTPUTS = 4;
	static final int NUM_LAYERS = 2;
	static final int[] shape;
	static {
		shape = new int[NUM_LAYERS];
		shape[0] = 5;
		shape[1] = NUM_OUTPUTS;
	}
	static final int SHAPE_SUM = shape[0] + shape[1];
	static final double MUTATION_RATE = 0.05;

	double[] myWeights;
	
	public PlayerGenome(double[] w) {
		myWeights = w;
	}
	
	public PlayerGenome() {
		Random r = new Random();
		myWeights = new double[NUM_INPUTS * shape[0] + shape[1] * shape[0]];
		for(int i=0;i<SHAPE_SUM;i++) {
			// bound between -1.0 and 1.0
			myWeights[i] = r.nextDouble()*2-1;
		}
		// System.out.println("Weights: " + Arrays.toString(myWeights));
	}

	public int[] getShape() {
		return shape;
	}

	public double[] getWeights() {
		return myWeights;
	}

	public void mutate() {
		Random r = new Random();
		for(int i=0;i<myWeights.length;i++) {
			// nextDouble is bounded by -1 and 1, so for a semantically-pure mutation rate we must /2:
			if(Math.abs(r.nextDouble()) <= MUTATION_RATE/2) {
				myWeights[i] = r.nextDouble();
			}
		}
	}
	public int getNumInputs() {
		return NUM_INPUTS;
	}
}
