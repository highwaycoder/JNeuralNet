package connectfour;

public class Game {
	
	boolean gameWon;
	byte lastColumn;
	int turnsTaken;
	int playerTurn;
	Player[] players;
	Board board;
	
	Game(Player a, Player b) {
		players = new Player[] { a, b };
		board = new Board();
		gameWon = false;
		// initialise turnsTaken to be negative so that a zero-length game can be caught
		turnsTaken = -1;
	}
	
	private void takeTurn(int player, byte playerDecision) {
		board.insertToken(player,playerDecision);
	}
	
	
	public void run() {
		playerTurn = 0;
		// turnsTaken only zero when we call run() so we know when we've called run()
		turnsTaken = 0;
		while(!gameWon) {
			takeTurn(playerTurn, players[playerTurn].takeTurn(board.getPlayerBoard(playerTurn)));
			turnsTaken++;
			gameWon = board.gameWon();
			if(!gameWon) // don't mess with playerTurn if the game has been won, we need it later
				playerTurn = (++playerTurn) % 2;
			// sanity check: don't keep looping if the board is full!
			if(board.isFull())
				break;
		}
	}
	public Player winner() {
		if(turnsTaken<=0) {
			System.err.println("Game not played yet, can't decide winner");
			return null;
		}
		if(!gameWon) {
			return null;
		}
		// the last player to take a turn necessarily won the game (see comment in run())
		return players[playerTurn];
	}
	
	public Player loser() {
		if(turnsTaken<=0) {
			System.err.println("Game not played yet, can't decide loser");
			return null;
		}
		if(!gameWon) {
			return null;
		}
		// since the last player won, we want the player who didn't win (warning: hack)
		return players[playerTurn==0?1:0];
	}

	public int gameLength() {
		if(turnsTaken>0) 
			return turnsTaken;
		if(turnsTaken<0)
			System.err.println("gameLength() called without calling run() first");
		if(turnsTaken==0)
			System.err.println("gameLength() returned zero (run() was called but game was not played :s)");
		return -1;
	}
}
