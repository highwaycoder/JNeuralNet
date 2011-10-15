package connectfour;

public class CFMain {
	public static final int DEFAULT_POP_SIZE = 12;
	public static final int DEFAULT_NUM_PARENTS = 6;
	public static final int NUM_GENERATIONS = 10;
	
	public static void main(String args[]) {
		int popSize=DEFAULT_POP_SIZE;
		int numParents = DEFAULT_NUM_PARENTS;
		int generations = NUM_GENERATIONS;
		CFPopulation pop = new CFPopulation(popSize,numParents,generations);
		try {
			pop.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
