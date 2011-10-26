package connectfour;

import java.util.Random;

import JNeuralNet.Genome;
import JNeuralNet.Individual;
import JNeuralNet.NeuralNet;

public class Player implements Individual {

	NeuralNet brain;
	private short SOME_THRESHOLD = 0;
	public boolean score;
	private int fitness;
	private Genome myGenes;
	public boolean trueWinner = false;
	
	Player() {
		this(new PlayerGenome());
	}
	
	Player(Genome g) {
		myGenes = g;
		brain = new NeuralNet(g);
		score = false;
	}
	
	public Genome getGenome() {
		return myGenes;
	}

	public int getScore() {

		return 0;
	}

	public void tick() throws Exception {
	
		
	}

	public byte takeTurn(Board.cellState[][] board) {
		byte rv = 0;
		
		try {
			short[] outputs = brain.tick(boardID(board));
			System.out.println("Brain output: "+ outputs[0] + ", " + outputs[1] + ", " + outputs[2] + ", " + outputs[3]);
			for(int i=0;i<outputs.length;i++) {
				if(outputs[i] > SOME_THRESHOLD ) {
					rv |= 1 << i;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Player decision: "+rv);
		return rv;
	}

	public short[] boardID(Board.cellState[][] board) {
		short[] rv = new short[(board.length * board[0].length * 2) / (Short.SIZE-1)];
		for(int i=0; i<board.length; i++) {
			for(int j=0; j<board[i].length; j++) {
				rv[((i * board[0].length)+j) / ((Short.SIZE-1)/2)] |= board[i][j].ordinal() << ((i * board[0].length)+j) % (Short.SIZE-1);
			}
		}
		System.out.println("Input: " + rv[0] + " " + rv[1] + " " + rv[2] + " " + rv[3] + " " + rv[4]);
		return rv;
	}

	public Player mate(Player player) {
		Genome mumGenome = this.getGenome();
		Genome dadGenome = player.getGenome();
		Random r = new Random();
		double[] childWeights = new double[mumGenome.getWeights().length];
		for(int i=0;i<childWeights.length;i++)
			childWeights[i] = (r.nextBoolean()?mumGenome.getWeights()[i]:dadGenome.getWeights()[i]);
		return new Player(new PlayerGenome(childWeights));
	}
	
	public void updateFitness(int f) {
		fitness = f;
	}
	public int getFitness() {
		return fitness;
	}
	
}
