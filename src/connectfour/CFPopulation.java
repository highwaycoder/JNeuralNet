package connectfour;

import java.util.Random;

import JNeuralNet.Individual;
import JNeuralNet.Population;

public class CFPopulation implements Population {

	private Player[] players;
	private int numParents;
	private int generations;
	
	CFPopulation(int popSize, int numParents, int generations) {
		players = new Player[popSize];
		for(int i=0;i<popSize;i++) {
			players[i] = new Player();
		}
		this.numParents = numParents;
		this.generations = generations;
	}
	
	public Individual[] getIndividuals() {
		return null;
	}

	public void run(int generations) throws Exception {
		for(int i=0; i<generations; i++) {
			System.out.print("Generation "+i+" started: ");
			for(int player1=0, player2=players.length-1; player1<players.length/2 && player2>=players.length/2; player1++, player2--) {
				Game a = new Game(players[player1], players[player2]);
				a.run();
				Game b = new Game(players[player2], players[player1]);
				b.run();
				
				if(a.winner() == b.winner()) {
					setScores(a.winner(), a.loser());
				}
				else {
					if(a.gemeLength() > b.gameLength) {
						setScores(a.winner(), a.loser());
					}
					else if(a.gemeLength() < b.gameLength){
						setScores(a.loser(), a.winner());
					}
					else {
						Random r = new Random();
						// ici resideth draconis
						boolean next = r.nextBoolean();
						setScores(next?a.winner():a.loser(), next?a.loser():a.winner());
					}
				}
			}
		}
	}

	private void setScores(Player winner, Player loser) {
		winner.score = true;
		loser.score = false;
	}
	
	public void run() throws Exception {
		run(generations);
	}
	
}
