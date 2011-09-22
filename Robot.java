package JNeuralNet;

class Robot implements Individual {
	
	MapSimulation map;
	NeuralNet brain;
	
	enum ScoreModifier {
		MOVE_PENALTY	(-1),
		CRASHED_PENALTY	(-5),
		SOUGHT_REWARD	(1);
		int amount; 
		
		ScoreModifier(int amount){
			this.amount = amount;
		}
	}
	
	
	static Item[] seeking = {
		Item.LANDMINE
	};
	static Item[] avoiding = {
		Item.WALL,
		Item.ROBOT
	};
	
	short strobeSensor,wallSensor,leftMotorSpeed,rightMotorSpeed;
	int score;
	int[] coords;
	double heading;
	
	RobotGenome myGenes;
	
	Robot(RobotGenome g) {
		myGenes = g;
		map = new MapSimulation(seeking);
		brain = new NeuralNet(g);
		coords = map.getEmptySquare();
		score = 0;
		leftMotorSpeed=0;
		rightMotorSpeed=0;
	}
	public void tick() throws Exception {
		updateSensors();
		short[] outputs = brain.tick(new short[] {strobeSensor,wallSensor});
		leftMotorSpeed = outputs[0];
		rightMotorSpeed = outputs[1];
		moveRobot();
	}
	
	void moveRobot() {
		heading += (rightMotorSpeed - leftMotorSpeed);
		while(heading>2*Math.PI) heading -= 2*Math.PI;
		while(heading<-2*Math.PI) heading += 2*Math.PI;
		int[] oldcoords = coords;
		coords[0] += Math.cos(heading);
		coords[1] += Math.sin(heading);
		if(!(coords[0]==oldcoords[0] && coords[1]==oldcoords[1])){
			score += ScoreModifier.MOVE_PENALTY.amount;
			Item here = map.getItemAt(coords);
			for(int i=0;i<seeking.length;i++) {
				if(here.isFlagSet(seeking[i])) {
					score += ScoreModifier.SOUGHT_REWARD.amount;
				}
			}
			for(int i=0;i<avoiding.length;i++) {
				if(here.isFlagSet(avoiding[i])) {
					score += ScoreModifier.CRASHED_PENALTY.amount;
				}
			}
			if(here.isFlagSet(Item.WALL)){
				coords = oldcoords;
			}
		}
	}
	
	void updateSensors() {
		updateWallSensor();
		updateStrobeSensor();
	}
	void updateWallSensor() {
		wallSensor = map.getClosestWall(coords,heading);
	}
	void updateStrobeSensor() {
		strobeSensor = map.getClosestSeekable(seeking,coords);
	}
	RobotGenome mate(RobotGenome partner) {
		
	}
}
