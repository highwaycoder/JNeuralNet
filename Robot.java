
class Robot implements Individual {
	
	MapSimulation map;
	NeuralNet brain;
	
	static Item[] seeking;
	static Item[] avoiding;
	static {
		seeking = {
			// TODO: fill this with items
		};
		avoiding = {
			// TODO: fill this with items
		};
	}
	
	int strobeSensor,wallSensor,score;
	int[] coords;
	
	RobotGenome myGenes;
	
	Robot(RobotGenome g) {
		myGenes = g;
		map = new MapSimulation();
		brain = new NeuralNet(g);
		coords = m.getEmptySquare();
		score = 0;
		
	}
	void tick() {
		updateSensors();
	}
	void updateSensors() {
		updateWallSensor();
		updateStrobeSensor();
	}
	void updateWallSensor() {
		
	}
	void updateStrobeSensor() {
		
	}
	RobotGenome mate(RobotGenome partner) {
		
	}
}
