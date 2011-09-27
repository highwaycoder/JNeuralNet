package JNeuralNet;

import java.util.Random;

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
	
	static final double LEFT_SENSOR_OFFSET = -1/(2*Math.PI);
	static final double RIGHT_SENSOR_OFFSET = 1/(2*Math.PI);
	
	static final int NUM_OFFSPRING = 2;
	
	static Item seeking = Item.EMPTY.setFlag(Item.LANDMINE.getFlags());
	static Item[] avoiding = {
		Item.WALL,
		Item.ROBOT
	};
	
	short leftSensor,rightSensor,wallSensor,leftMotorSpeed,rightMotorSpeed;
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
		short[] outputs = brain.tick(new short[] {leftSensor,rightSensor,wallSensor});
		leftMotorSpeed = outputs[0];
		rightMotorSpeed = outputs[1];
		moveRobot();
		map.draw(); // because drawing stuff is fun, and difficult!
	}
	
	public int getScore() {
		return score;
	}
	
	public RobotGenome getGenome() {
		return myGenes;
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
			Item oldhere = map.getItemAt(oldcoords);
			if(here.isFlagSet(seeking)) {
					score += ScoreModifier.SOUGHT_REWARD.amount;
					here.removeFlag(seeking);
					map.spawnSeekable(seeking);
			}
			for(int i=0;i<avoiding.length;i++) {
				if(here.isFlagSet(avoiding[i])) {
					score += ScoreModifier.CRASHED_PENALTY.amount;
				}
			}
			if(here.isFlagSet(Item.WALL)){
				coords = oldcoords;
			}
			oldhere.removeFlag(Item.ROBOT);
			here.setFlag(Item.ROBOT);
			map.setItemAt(oldcoords,oldhere);
			map.setItemAt(coords,here);
		}
	}
	
	void updateSensors() {
		updateWallSensor();
		// since we've had historical problems with IndexOutOfBoundsExceptions, please leave this try/catch block in place.
		try {
			updateLeftSensor();
			updateRightSensor();
		} catch (IndexOutOfBoundsException e) {
			System.err.println("Error caused by updateSensors: ");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	void updateWallSensor() {
		wallSensor = map.getClosestWall(coords,heading);
	}
	void updateLeftSensor() {
		leftSensor = map.getClosestSeekable(seeking,coords,heading+LEFT_SENSOR_OFFSET);
	}
	void updateRightSensor() {
		rightSensor = map.getClosestSeekable(seeking,coords,heading+RIGHT_SENSOR_OFFSET);
	}
	
	RobotGenome[] mate(RobotGenome partner, int numOffspring) {
		RobotGenome[] childrenGenome = new RobotGenome[numOffspring];
		Random r = new Random();
		for(int i=0;i<numOffspring;i++) {
			double childWeights[] = new double[myGenes.getWeights().length];
			for(int j=0;j<myGenes.getWeights().length;j++) {
				childWeights[j] = (r.nextBoolean())?myGenes.getWeights()[j]:partner.getWeights()[j];
			}
			childrenGenome[i] = new RobotGenome(childWeights);
			childrenGenome[i].mutate();
		}
		return childrenGenome;
	}
}
