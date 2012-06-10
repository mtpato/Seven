package seven.g3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
	private Set<CharBag> makeSeven;

	Random me = new Random();

	public MCSimWorker(int rounds, int money, int max, int[] wins,
			List<Character> bag, List<Character> game, Character letter,
			int gameSteps, Set<CharBag> makeSeven) {

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
		this.makeSeven = makeSeven;

		/*
		 * this.bag = new ArrayList<Character>(bag); this.game = new
		 * ArrayList<Character>(game); this.letter = new Character(letter);
		 */

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

			if (simulate(givenBag, givenGame, givenLetter, bet, money)) {
				localWins[bet - 1]++;
			}

		}

		updateSharedWins();
		
		log.trace("greater: " + greater);
		log.trace("seven: " + sevenCool);
		log.trace("THREAD DONE");

	}

	private synchronized void updateSharedWins() {

		for (int i = 0; i < localWins.length; i++) {
			sharedWins[i] = sharedWins[i] + localWins[i];

		}

	}

	private boolean simulate(List<Character> bag, List<Character> game,
			Character letter, int bet, int money) {
		Random me = new Random();
		Random players = new Random();
		Random index = new Random();

		int match = players.nextInt(100 / 4) + 1;
		int spent = 0;
		int steps = gameSteps;

		// log.trace("IN SIM");

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
	
	private int greater = 0;
	private int sevenCool = 0;

	private boolean isSeven(List<Character> bag) {
		double ran = me.nextDouble();
		
		if(bag.size() == 7 && ran < 0.0931) {
			return true;
		}
		if(bag.size() == 8 && ran < 0.3316) {
			return true;
		}
		if(bag.size() == 9 && ran < 0.5906) {
			return true;
		}
		if(bag.size() == 10 && ran < 0.7794) {
			return true;
		}
		if(bag.size() == 11 && ran < 0.8864) {
			return true;
		}
		if(bag.size() == 12 && ran < 0.9586) {
			return true;
		}
		if(bag.size() == 13 && ran < 0.9767) {
			return true;
		}
		if(bag.size() == 14 && ran < 0.8850) {
			return true;
		}
		if(bag.size() == 15 && ran < 0.9949) {
			return true;
		}
		if(bag.size() == 16 && ran < 0.9974) {
			return true;
		}
		
		
		/*if (bag.size() < 7)
			return false;

		//log.trace("in is sevel past false");

		if (bag.size() >= 10) {
			greater++;
			return true;
		}
			
		
		for (int x = 0; x < 50; x++) {
			List<Character> b = new ArrayList<Character>(bag);
			List<Character> seven = new ArrayList<Character>();

			for (int y = 0; y < 7; y++) {
				Character c = b.get(me.nextInt(b.size()));
				seven.add(c);
				b.remove(c);
			}

			if (makeSeven.contains(seven)) {
				sevenCool++;
				return true;
			}
				
		}*/

		return false;
	}

	private boolean getWin(List<Character> bag, int spent) {

		/*
		 * if (bag.size() >= 10) { if (spent == 0 || (double) spent / bag.size()
		 * < 8) return true;
		 * 
		 * }
		 */

		if (isSeven(bag)) {
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
