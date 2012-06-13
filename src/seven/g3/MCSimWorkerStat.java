package seven.g3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import seven.ui.Player;

public class MCSimWorkerStat extends Thread {

	private final Logger log = Logger.getLogger(this.getClass());

	private int rounds;
	private int money;
	private int max;
	private int[] sharedWins;
	private int[] localWins;
	private List<Character> givenBag;
	private List<Character> givenGame;
	private int givenBagSize;
	private int givenGameSize;
	
	private Character givenLetter;
	private int gameSteps;
	private Set<CharBag> makeSeven;
	private int numPlayers;
	private List<BidHist> bidHists;
	//Random ran = new Random();

	Random me = new Random();

	public MCSimWorkerStat(int rounds, int money, int max, int[] wins,
			List<Character> bag, List<Character> game, Character letter,
			int gameSteps, 	List<BidHist> bidHists) {

		log.trace("rounds: " + rounds);

		this.rounds = rounds;
		this.money = money;
		this.max = max;
		this.sharedWins = wins;
		this.localWins = new int[wins.length];
		this.givenBag = bag;
		this.givenGame = game;
		
		givenBagSize = bag.size();
		givenGameSize = game.size();
		
		
		this.givenLetter = letter;
		this.gameSteps = gameSteps;
		this.bidHists = bidHists;
		//this.numPlayers = numPlayers;

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

			int bet = me.nextInt(max - 3) + 3;// 3-max  max cant be less than 3

			if (simulate(givenBagSize, givenGameSize, givenLetter, bet, money)) {
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

	private boolean simulate(int bagSizeIn, int gameSizeIn,
			Character letter, int bet, int money) {
		Random ran = new Random();
		//Random players = new Random();
		//Random index = new Random();
		int numPlayers = bidHists.size();
		List<Integer> stepBids = new ArrayList<Integer>(bidHists.size());

		for(int i = 0; i < bidHists.size(); i ++) {
			stepBids.add(i, bidHists.get(i).getAveBid());
		}
		
		int match;
		int spent = 0;
		int steps = gameSteps;
		//log.trace("steps left: " + gameSteps);

		// log.trace("IN SIM");

		int gameSize = gameSizeIn;
		int bagSize = bagSizeIn;

		while (gameSize != 0 && steps-- != 0) {
			
			
			match = Collections.max(stepBids);
			if (bet > match) {
				bagSize++;
				spent += match;
				money -= match;
			} else if (bet == match && heads()) {
				
				bagSize++;
				spent += (match - 1);
				money -= (match - 1);
			}
			//log.trace("stepBids : " + match + " " + bet);

			gameSize--;

			
			if(money < 95) {
				bet = ran.nextInt(money / 5) + 3;//1/5 + 3 to back off a little and make it min at 3
			} else {
				bet = ran.nextInt(22) + 3;// dont want to bid crazy high even if we have over 100. this was an isseu before we were betting like 50 when we had 200 and blowing tons of money 				
			}
			
			
			for(int i = 0; i < bidHists.size(); i ++) {
				stepBids.set(i, bidHists.get(i).getAveBid());
			}
			//match = ran.nextInt(100 / 6) + 1;

		}

		return getWin(bagSize, spent);

	}
	
	private int greater = 0;
	private int sevenCool = 0;

	private boolean isSeven(int bagSize) {
		double ran = me.nextDouble();
		
		//if(bagSize > 10) log.trace("SHOUDL WIN");
		
		if(bagSize == 7 && ran < 0.0931) {
			return true;
		}
		if(bagSize == 8 && ran < 0.3316) {
			return true;
		}
		if(bagSize == 9 && ran < 0.5906) {
			return true;
		}
		if(bagSize == 10 && ran < 0.7794) {
			return true;
		}
		if(bagSize == 11 && ran < 0.8864) {
			return true;
		}
		if(bagSize == 12 && ran < 0.9586) {
			return true;
		}
		if(bagSize == 13 && ran < 0.9767) {
			return true;
		}
		if(bagSize == 14 && ran < 0.9850) {
			return true;
		}
		if(bagSize == 15 && ran < 0.9949) {
			return true;
		}
		if(bagSize == 16 && ran < 0.9974) {
			return true;
		}
		

	

		return false;
	}

	private boolean getWin(int bagSize, int spent) {


		//log.trace("bagsize" + bag.size());
		if (isSeven(bagSize)) {
			//log.trace("isSeven!");
			if (spent == 0 || (double) spent / bagSize < 8)
				return true;

		}

		return false;
	}

	private Random coin = new Random();

	private boolean heads() {
		return coin.nextInt(100) % 2 == 0;
	}

}
