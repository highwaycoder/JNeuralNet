package JNeuralNet;

import java.util.ArrayList;

public class RobotPopulation implements Population {
	Robot[] individuals;
	MapSimulation[] worlds;
	int ppg;
	
	RobotPopulation(int size, int parentsPerGeneration,RobotDisplay robotDisplay) {
		if(parentsPerGeneration > size) {
			System.err.println("More parents than population!! Cannot proceed.");
			System.exit(-1);
		}
		if((1.0*size) / (1.0*parentsPerGeneration)%1.0 != 0) {
			System.err.println("Population size is not evenly divisible among parents. Cannot proceed.");
			System.exit(-1);
		}
		individuals = new Robot[size];
		worlds = new MapSimulation[size];
		ppg = parentsPerGeneration;
		for(int i=0; i<size; i++) {
			worlds[i] = new MapSimulation(Robot.seeking);
			individuals[i] = new Robot(new RobotGenome(),worlds[i]);
		}
		robotDisplay.setWorlds(worlds);
	}
	
	public void run(int cyclesPerGeneration, int generations) throws Exception {
		for(int i=0; i<generations; i++) {
			System.out.print("Generation "+i+" started: ");
			for(int j=0; j<cyclesPerGeneration; j++) {
				for(int k=0; k<individuals.length; k++) {
					// System.out.print(".");
					individuals[k].tick();
				}
			}
			nextGeneration();
		}
	}
	
	public Robot[] getIndividuals() {
		return individuals;
	}

	void nextGeneration() {
		Robot[] sorted = sortIndividuals();
		RobotGenome[] children = new RobotGenome[individuals.length];
		for(int i=0, j=ppg-1; i<ppg; i++, j--) {
			RobotGenome[] subchldrn = sorted[i].mate(sorted[j].getGenome(), individuals.length/ppg);
			for(int k=0; k<individuals.length/ppg; k++) {
				children[i*(individuals.length/ppg)+k] = subchldrn[k];
			}
		}
	}
	
	Robot[] sortIndividuals() {
		ArrayList<Robot> rv = new ArrayList<Robot>();
		for(int i=0; i<individuals.length; i++) {
			int index = 0;
			while(rv.size() > index && rv.get(index).getScore() > individuals[i].getScore()) {
				index++;
			}
			rv.add(index, individuals[i]);
		}
		System.out.print("Fittest child's score: "+rv.get(0).getScore()+"\n");
		return rv.toArray(new Robot[rv.size()]);
	}
}
