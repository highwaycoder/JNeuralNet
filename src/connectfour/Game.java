package connectfour;

public class Game {
	static final int BOARD_WIDTH = 7;
	static final int BOARD_HEIGHT = 6;
	enum boardLocationState {
		EMPTY, PLAYER_A, PLAYER_B
	}
	boolean gameWon;
	byte lastColumn;
	int turnsTaken;
	Player[] players;
	boardLocationState[][] board;
	Game(Player a, Player b) {
		players = new Player[] { a, b };
		board = new boardLocationState[BOARD_WIDTH][BOARD_HEIGHT];
		for(int i=0; i<BOARD_WIDTH; i++) {
			for(int j=0;j<BOARD_HEIGHT;j++) {
				board[i][j] = boardLocationState.EMPTY;
			}
		}
		gameWon = false;
		turnsTaken = 0;
	}
	
	private void takeTurn(int player, byte playerDecision) {
		lastColumn = playerDecision;
		if(playerDecision >= BOARD_WIDTH) {
			return;
		}
		boardLocationState[] column = board[playerDecision];
		// if the column is full, we can't insert the token!
		if(column[0] != boardLocationState.EMPTY) {
			return;
		}
		for(int i=BOARD_HEIGHT-1; i>=0; i--) {
			if(column[i] == boardLocationState.EMPTY) {
				column[i] = (player == 0) ? boardLocationState.PLAYER_A : boardLocationState.PLAYER_B;
				break;
			}
		}
	}
	
	private boolean isGameWon() {
		int[][] offsetOffsets = new int[][] { {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1} };
		if(lastColumn >= BOARD_WIDTH) {
			return false;
		}
		int rowIndex = -1;
		for(int i=0;i<BOARD_HEIGHT;i++){
			if(board[lastColumn][i] != boardLocationState.EMPTY) {
				rowIndex = i;
				break;
			}
		}
		if(rowIndex == -1) {
			System.err.println("Empty column passed into isGameWon");
			return false;
			
		}
		for(int direction=0; direction<offsetOffsets.length; direction++){
			int colOffset = 0, rowOffset = 0;
			if(lastColumn + (3*offsetOffsets[direction][0]) >= BOARD_WIDTH || lastColumn + (3*offsetOffsets[direction][0]) < 0) {
				continue;
			}
			if(rowIndex + (3*offsetOffsets[direction][1]) >= BOARD_HEIGHT || rowIndex + (3*offsetOffsets[direction][1]) < 0) {
				continue;
			}
			while(board[lastColumn + colOffset][rowIndex + rowOffset] == board[lastColumn][rowIndex]) {
				if(Math.abs(colOffset) == 3 || Math.abs(rowOffset) == 3) {
					return true;
				}
				colOffset += offsetOffsets[direction][0];
				rowOffset += offsetOffsets[direction][1];
				
			}
		}
		return false;
	}
	
	public void run() {
		int playerTurn = 0;
		while(!gameWon) {
			takeTurn(playerTurn, players[playerTurn].takeTurn(board.clone()));
			turnsTaken++;
			playerTurn = (++playerTurn) % 2;
			gameWon = isGameWon();
		}
		
	}
}
