package JNeuralNet;

public interface Individual {
	int getScore();
	Genome getGenome();
	void tick() throws Exception;
}
