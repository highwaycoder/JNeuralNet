package JNeuralNet;
import java.util.Random;


class RobotGenome implements Genome {

	private static final int NUM_INPUTS = 2;
	private static final int NUM_OUTPUTS = 2;
	private static final int NUM_LAYERS = 2;
	static int[] shape;
	double[] weights;
	static {
		shape = new int[NUM_LAYERS];
		shape[0] = 5;
		shape[1] = 2;
		// ...
		
	}
	
	RobotGenome() {
		weights = new double[NUM_INPUTS * shape[0] + shape[0] * shape[1] /* ... */];
		Random myRand = new Random();
		for(int i=0;i<weights.length;i++) {
			weights[i] = myRand.nextDouble()*2-1;
		}
	}
	RobotGenome(double[] w) {
		weights = w;
	}
	public Robot getIndividual() {
		return new Robot(this);
	}
	public int[] getShape() {
		return shape;
	}
	public double[] getWeights() {
		return weights;
	}
}
