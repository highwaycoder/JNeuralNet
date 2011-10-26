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
			System.out.print("Generation "+(i+1)+" started: ");
			for(int player1=0, player2=players.length-1; player1<players.length/2; player1++, player2--) {
				Game a = new Game(players[player1], players[player2]);
				a.run();
				Game b = new Game(players[player2], players[player1]);
				b.run();
				
				if(a.winner() != null && b.winner() != null) {
					if(a.winner() == b.winner()) {
						setScores(a.winner(), a.loser());
					}
					else if(a.gameLength() > b.gameLength()) {
						setScores(a.winner(), a.loser());
					}
					else if(a.gameLength() < b.gameLength()){
						setScores(a.loser(), a.winner());
					}
				}
				else if(a.winner() != null) {
						setScores(a.winner(), a.loser());
				} 
				else if (b.winner() != null) {
					setScores(b.winner(), b.loser());
				}
				else {
					// choosing a winner in this case is arbitrary, but should really be randomised
					Random r = new Random();
					// ici resideth draconis
					boolean wintest = r.nextBoolean();
					// varning: här är drakar
					setScores(wintest?a.winner():a.loser(), wintest?a.loser():a.winner());
				}
				// update fitness scores
				if(a.winner() != null) {
					a.winner().updateFitness(a.gameLength());
				}
				if(b.winner() != null) {
					b.winner().updateFitness(b.gameLength());
				}
			} // for(int player1=0, player2=players.length-1; player1<players.length/2 && player2>=players.length/2; player1++, player2--)
			nextGeneration(); // genetic thingy doesn't work without this!
		} // for(int i=0; i<generations; i++)
	} // public void run(int generations) throws Exception

	private void nextGeneration() {
		// do some fancy recombination stuff with the winners
		Player[] winners = new Player[numParents];
		int fittest = 0;
		int numTrueWinners = 0;
		for(int i=0,j=0;i<players.length;i++) {
			if(numTrueWinners == numParents) break; // don't get more than numParents worth of winners!
			if(players[i].trueWinner) winners[j++] = players[i];
			numTrueWinners=j;
		}
		for(int i=0;i<players.length;i++) {
			if(players[i].getFitness() > fittest)
				fittest = players[i].getFitness();
		}
		System.out.print("Quickest game: "+fittest+" turns long. ");
		System.out.println("Winners: "+numTrueWinners);
		if(numTrueWinners < numParents) {
			for(int i=numTrueWinners;i<winners.length;i++) {
				winners[i] = new Player(); // fill the rest of the winners with random data
			}
		}
		for(int childIndex=0,mumIndex=0,dadIndex=winners.length-1;childIndex<players.length;childIndex++,mumIndex++,dadIndex--) {
			// mumIndex and dadIndex should be circular:
			if(mumIndex >= numParents) {
				mumIndex=0;
				dadIndex=winners.length-1;
			}
			players[childIndex] = winners[mumIndex].mate(winners[dadIndex]);
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
