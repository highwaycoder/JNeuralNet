package connectfour;

import JNeuralNet.Genome;
import JNeuralNet.Individual;
import JNeuralNet.NeuralNet;
import connectfour.Game.boardLocationState;

public class Player implements Individual {

	NeuralNet brain;
	private short SOME_THRESHOLD = 0;
	
	Player() {
		brain = new NeuralNet(new PlayerGenome());
	}
	
	Player(Genome g) {
		brain = new NeuralNet(g);
	}
	
	public Genome getGenome() {
		return null;
	}

	public int getScore() {

		return 0;
	}

	public void tick() throws Exception {
	
		
	}

	public byte takeTurn(boardLocationState[][] board) {
		short[] inputs = new short[2];
		int boardID = boardID(board);
		byte rv = 0;
		inputs[0] = (short)(boardID >> 16);
		inputs[1] = (short)(boardID);
		try {
			short[] outputs = brain.tick(inputs);
			for(int i=0;i<outputs.length;i++) {
				if(outputs[i] > SOME_THRESHOLD ) {
					rv |= 1 << i;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rv;
	}
	
	public int boardID(boardLocationState[][] board) {
		int rv = 0;
		for(int i=0; i<board.length; i++) {
			for(int j=0; j<board[i].length; j++) {
				rv |= board[i][j].ordinal() << i*j; 
			}
		}
		return rv;
	}
	
}
