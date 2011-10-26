package connectfour;

public class Board {
	private static final int BOARD_HEIGHT = 6;
	private static final int BOARD_WIDTH = 7;
	private static final int WIN_CONDITION = 4;
	
	private int boardHeight;
	private int boardWidth;
	private int turnsTaken;
	
	enum cellState {
		INVALID, PLAYER_A, PLAYER_B, EMPTY
	}
	
	private cellState[][] state;
	private int lastColumn = -1;
	
	public Board(int width,int height) {
		state = new cellState[width][height];
		for(int i=0;i<width;i++) {
			for(int j=0;j<height;j++) {
				state[i][j] = cellState.EMPTY;
			}
		}
		boardHeight = height;
		boardWidth = width;
		turnsTaken = -1;
	}
	
	public Board() {
		this(BOARD_WIDTH,BOARD_HEIGHT);
	}
	
	public cellState[][] getState() {
		return state;
	}

	public void insertToken(int player, byte playerDecision) {
		lastColumn = playerDecision;
		if(turnsTaken==-1) {
			turnsTaken = 0;
		}
		if(playerDecision >= boardWidth) {
			return;
		}
		cellState[] column = state[playerDecision];
		// if the column is full, we can't insert the token!
		if(column[0] != cellState.EMPTY) {
			return;
		}
		for(int i=boardHeight-1; i>=0; i--) {
			if(column[i] == cellState.EMPTY) {
				column[i] = (player == 0) ? cellState.PLAYER_A : cellState.PLAYER_B;
				turnsTaken++;
				break;
			}
		}
		
	}
	public boolean gameWon() {
		int[][] offsetOffsets = new int[][] { {1, -1}, {1, 0}, {1, 1}, {0, 1} };
		if(lastColumn >= boardWidth) {
			return false;
		}
		int rowIndex = -1;
		for(int i=0;i<boardHeight;i++){
			if(state[lastColumn][i] != cellState.EMPTY) {
				rowIndex = i;
				break;
			}
		}
		if(rowIndex == -1) {
			System.err.println("Empty column passed into Board.gameWon()");
			return false;
		}
		
		for(int direction=0;direction<offsetOffsets.length;direction++) {
			int colOffset = 0, rowOffset = 0, tokensFound = 0;
			// sanity check included in while condition:
			while(lastColumn + colOffset >= 0 && lastColumn + colOffset < boardWidth &&
					rowIndex + rowOffset >= 0 && rowIndex + rowOffset < boardHeight &&
					state[lastColumn + colOffset][rowIndex + rowOffset] == state[lastColumn][rowIndex]) {
				if(++tokensFound == WIN_CONDITION) {
					return true;
				}
				colOffset += offsetOffsets[direction][0];
				rowOffset += offsetOffsets[direction][1];
			}
			// hack to resolve counting the initial token twice
			colOffset = -1;
			rowOffset = -1;
			// invert everything and go in the opposite direction
			while(lastColumn - colOffset >= 0 && lastColumn - colOffset < boardWidth &&
					rowIndex - rowOffset >= 0 && rowIndex - rowOffset < boardHeight &&
					state[lastColumn - colOffset][rowIndex - rowOffset] == state[lastColumn][rowIndex]) {
				if(++tokensFound == WIN_CONDITION) {
					return true;
				}
				colOffset += offsetOffsets[direction][0];
				rowOffset += offsetOffsets[direction][1];
			}
		}
		return false;
	}
	public cellState[][] getPlayerBoard(int playerTurn) {
		cellState[][] cloned = state;
		if(playerTurn != 0) {
			for(int i=0;i<cloned.length;i++) {
				for(int j=0;j<cloned[0].length;j++) {
					switch(cloned[i][j]) {
					case PLAYER_A:
						cloned[i][j] = cellState.PLAYER_B;
						break;
					case PLAYER_B:
						cloned[i][j] = cellState.PLAYER_A;
						break;
					default:
						break;
					}
				}
			}
		}
		return cloned;
	}
	public boolean isFull() {
		if(turnsTaken >= (boardHeight * boardWidth)) return true;
		else return false;
	}
	public int getTurnsTaken() {
		return turnsTaken;
	}
	public int getWidth() {
		return boardWidth;
	}
	public int getHeight() {
		return boardHeight;
	}
}
