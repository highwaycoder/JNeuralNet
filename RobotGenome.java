package JNeuralNet;
import java.util.Random;


class RobotGenome implements Genome {

	public static final int NUM_INPUTS = 3;
	public static final int NUM_LAYERS = 2;
	public static final double MUTATION_RATE = 0.05;
	static int[] shape;
	double[] weights;
	static {
		shape = new int[NUM_LAYERS];
		shape[0] = 5;
		shape[1] = 2;
		// ...
		
	}
	// alias NUM_OUTPUTS to be the number of neurons in the uppermost layer
	private static final int NUM_OUTPUTS = shape[shape.length-1];

	RobotGenome() {
		weights = new double[(NUM_INPUTS * shape[0]) + (shape[0] * shape[1]) /* ... */]; 
		Random myRand = new Random();
		for(int i=0;i<weights.length;i++) {
			weights[i] = myRand.nextDouble()*2-1;
		}
	}
	RobotGenome(double[] w) {
		weights = w;
	}
	public int[] getShape() {
		return shape;
	}
	public double[] getWeights() {
		return weights;
	}
	public void mutate() {
		Random r = new Random();
		for(int i=0;i<weights.length;i++){
			if(r.nextDouble()<MUTATION_RATE)
				weights[i] += r.nextDouble();
			while(weights[i]>1.0)
				weights[i] = 1.0;
			while(weights[i]<-1.0)
				weights[i] = -1.0;
		}	
	}
}
