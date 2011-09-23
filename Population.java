package JNeuralNet;

public interface Population {
	void run(int cyclesPerGeneration, int generations) throws Exception;
	Individual[] getIndividuals();
}
