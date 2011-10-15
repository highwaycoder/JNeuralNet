package JNeuralNet;

public interface Population {
	void run(int generations) throws Exception;
	Individual[] getIndividuals();
}
