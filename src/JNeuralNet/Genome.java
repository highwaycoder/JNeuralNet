package JNeuralNet;

public interface Genome {
	int getNumInputs();
	int[] getShape();
	double[] getWeights();
	void mutate();
}
