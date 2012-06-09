package seven.g3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import seven.ui.Player;

public class MCSimWorker extends Thread {

	private final Logger log = Logger.getLogger(this.getClass());
	
	private int rounds;
	private int money;
	private int max;
	private int[] sharedWins;
	private int[] localWins;
	private List<Character> givenBag;
	private List<Character> givenGame;
	private Character givenLetter;
	private int gameSteps;
	
	Random me = new Random();
	
	

	public MCSimWorker(int rounds, int money, int max, int[] wins, List<Character> bag, List<Character> game, Character letter, int gameSteps) {
		
		log.trace("rounds: " + rounds);
		
		this.rounds = rounds;
		this.money = money;
		this.max = max;
		this.sharedWins = wins;
		this.localWins = new int[wins.length];
		this.givenBag = bag;
		this.givenGame = game;
		this.givenLetter = letter;
		this.gameSteps = gameSteps;
		
		/*this.bag = new ArrayList<Character>(bag);
		this.game = new ArrayList<Character>(game);
		this.letter = new Character(letter);*/
		
	}

	/**
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		monteCarlo();
	}
	
	private void monteCarlo() {


		for (int x = 0; x < rounds; x++) {

			
			int bet = me.nextInt(max) + 1;// FIX THIS CANT GET RANDOM ON 0

			if (simulate(givenBag, givenGame, givenLetter, bet, money)){
				localWins[bet - 1]++;
			}
				
		}

		for(int i = 0; i < localWins.length; i++) {
		log.trace(localWins[i]);
		}
		updateSharedWins();
		log.trace("THREAD DONE");
		
	}

	private synchronized void updateSharedWins() {
	
		for(int i = 0; i < localWins.length; i++) {
			sharedWins[i] = sharedWins[i] + localWins[i];
			
		}
		
	}

	private boolean simulate(List<Character> bag, List<Character> game, Character letter, int bet, int money) {
		Random me = new Random();
		Random players = new Random();
		Random index = new Random();

		int match = players.nextInt(100 / 4) + 1;
		int spent = 0;
		int steps = gameSteps;

		//log.trace("IN SIM");
		
		game = new ArrayList<Character>(game);
		bag = new ArrayList<Character>(bag);

		while (!game.isEmpty() && steps-- != 0) {
			
			if (bet > match) {
				bag.add(letter);
				spent += match;
				money -= match;
			} else if (bet == match && heads()) {
				bag.add(letter);
				spent += (match - 1);
				money -= (match - 1);
			}

			letter = game.get(index.nextInt(game.size()));
			game.remove(letter);

			bet = me.nextInt(money / 4) + 1;
			match = players.nextInt(100 / 4) + 1;

		}
		
		return getWin(bag, spent);

	}



	private boolean getWin(List<Character> bag, int spent) {

		if (bag.size() >= 10) {
			if (spent == 0 || (double) spent / bag.size() < 8)
				return true;

		}

		return false;
	}
	
	private Random coin = new Random();

	private boolean heads() {
		return coin.nextInt(100) % 2 == 0;
	}

}
