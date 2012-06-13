package seven.g3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import seven.ui.Letter;
import seven.ui.LetterGame;
import seven.ui.Player;
import seven.ui.PlayerBids;
import seven.ui.SecretState;

public class Group3PlayerMonteCarlo implements Player {

	private final Logger log = Logger.getLogger(this.getClass());
	private List<Word> wordList = new ArrayList<Word>();
	private Set<CharBag> makeSeven = new HashSet<CharBag>();

	
	{
		try {
			BufferedReader r = new BufferedReader(new FileReader(
					"textFiles/dictionary.txt"));
			String line = r.readLine(); // skip first line

			while (null != (line = r.readLine())) {
				line = line.trim();
				line = line.substring(line.indexOf(',') + 1);

				wordList.add(new Word(line));
				
				if (line.length() == 7)
					makeSeven.add(new CharBag(line));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static final int SIMULATION_ROUNDS = 100000;
	
	private static final String PLAYERNAME = "Group3PlayerMonteCarlo";

	private int playerScore = 0;

	private List<Character> myLetters;
	
	private List<BidHist> bidHists;
	
	private int numberOfPlayers;

	private int monteCarlo(List<Character> bag, List<Character> game, Character letter) {

		int money = playerScore;

		int max;
		if (money < 95) {
			max = (money / 5) + 4;
		} else {
			max = 25;
		}
		

		if(max == 0) max = 1;

		int[] wins = new int[max];

		Random me = new Random();
		
		//thread here
		
		MCSimWorkerStat[] workers = new MCSimWorkerStat[1];//4 workers 
		
		for(int i = 0; i < workers.length; i++) {
			workers[i] = new MCSimWorkerStat(SIMULATION_ROUNDS/workers.length, 
										 money, 
										 max, 
										 wins, 
										 bag, 
										 game, 
										 letter,
										 gameSteps,
										 bidHists);
			
			workers[i].start();
		}
		
		
		
		for(MCSimWorkerStat w : workers) {
			try {
				w.join();
				log.trace("JOIN");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		log.trace("all Joined");


		int winner = 0;

		int best = Integer.MIN_VALUE;

		for (int x = 3; x < max; x++) {
			log.trace((x + 1) + ": " + wins[x]);
			if (wins[x] > best) {
				winner = x + 1;
				best = wins[x];
			}

		}

		return winner;
	}



	private int gameSteps = 0;


	/*
	 * private int getScore(List<Character> bag) { char[] chars = new
	 * char[bag.size()];
	 * 
	 * for (int x = 0; x < bag.size(); x++) chars[x] = bag.get(x);
	 * 
	 * Word word = new Word(new String(chars)); int best = Integer.MIN_VALUE;
	 * 
	 * for (Word w : wordList) if (w.contains(word)) if (w.score > best) best =
	 * w.score;
	 * 
	 * return best; }
	 */





	@Override
	public void newGame(int id, int number_of_rounds, int number_of_players) {
		bidHists = new ArrayList<BidHist>();
		
		numberOfPlayers = number_of_players;
		
		
		
		gameSteps = 8 * number_of_players;// FIX THIS
	}

	@Override
	public void newRound(SecretState secretState, int current_round) {
		gameSteps = 8 * (numberOfPlayers);
		playerScore = secretState.getScore();
		myLetters = new ArrayList<Character>();
		
		for (Letter l : secretState.getSecretLetters())
			myLetters.add(l.getCharacter());
		
		gameSteps -= (myLetters.size() * (bidHists.size() + 1));
	}

	@Override
	public int getBid(Letter bidLetter, ArrayList<PlayerBids> PlayerBidList,
			ArrayList<String> PlayerList, SecretState secretstate) {
		
		gameSteps--;//how many steps left is one less
		log.trace("GAME STEPS IN MAIN: " + gameSteps);
		if(bidHists.isEmpty()) {
			for(int i = 0; i < PlayerList.size(); i++) {
				if(!PlayerList.get(i).contains(PLAYERNAME)) {
					bidHists.add(new BidHist(i, PlayerList.get(i)));
				}
				
				
			}
		}

		List<Letter> list = LetterGame.getRemainingLetters();
		List<Character> gameLetters = new ArrayList<Character>();
		
		if (myLetters.size() >= 12)
			return 1;

		playerScore = secretstate.getScore();

		for (Letter l : list)
			gameLetters.add(l.getCharacter());

		int bid = monteCarlo(myLetters, gameLetters, bidLetter.getCharacter());
	
		return bid;
	}

	@Override
	public void bidResult(boolean won, Letter letter, PlayerBids bids) {
		
		//NEED TO FIND THE WINNER AND DEDUCT THE RIGHT AMOUNT FROM THEIR BANK IN THE BIDHIST CLASS
		for(BidHist b: bidHists) {
			b.addBet(bids.getBidvalues().get(b.getpIndex()));	
			
			if(bids.getWinnerID() == b.getpIndex()) {
				b.addMoney((bids.getWinAmmount() * -1));
			}
					
		}
		
		
		
		for(BidHist b: bidHists) {
			log.trace(b.getpIndex() + " " + b.getpName() + ": " + b.getMinBid() + " " + b.getAveBid() + " " + b.getMaxBid());
					
		}
		
		
		
		if (won)
			myLetters.add(letter.getCharacter());
	}

	@Override
	public String getWord() {
		char c[] = new char[myLetters.size()];
		for (int i = 0; i < c.length; i++) {
			c[i] = myLetters.get(i);
		}
		String s = new String(c);
		Word ourletters = new Word(s);
		Word bestword = new Word("");
		for (Word w : wordList) {
			if (ourletters.contains(w)) {
				if (w.score > bestword.score) {
					bestword = w;
				}

			}
		}

		log.trace("My ID is me and my word is " + bestword.word);

		return bestword.word;
	}

	@Override
	public void updateScores(ArrayList<Integer> scores) {
		for(BidHist b: bidHists) {
			b.setMoney(scores.get(b.getpIndex()));
		}
			
		
	}

}
