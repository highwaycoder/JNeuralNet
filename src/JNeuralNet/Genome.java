package JNeuralNet;

public interface Genome {
	int[] getShape();
	double[] getWeights();
	void mutate();
}
