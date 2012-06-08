package seven.g3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import seven.ui.Letter;
import seven.ui.LetterGame;
import seven.ui.Player;
import seven.ui.PlayerBids;
import seven.ui.SecretState;

public class Group3PlayerMonteCarlo implements Player {

	private final Logger log = Logger.getLogger(this.getClass());
	private List<Word> wordList = new ArrayList<Word>();

	{
		try {
			BufferedReader r = new BufferedReader(new FileReader(
					"textFiles/dictionary.txt"));
			String line = r.readLine(); // skip first line

			while (null != (line = r.readLine())) {
				line = line.trim();
				line = line.substring(line.indexOf(',') + 1);

				wordList.add(new Word(line));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static final int SIMULATION_ROUNDS = 1000000;

	private int playerScore = 0;

	private List<Character> myLetters;

	private int monteCarlo(List<Character> bag, List<Character> game,
			Character letter) {

		int money = playerScore;

		int max = money / 4;

		if(max == 0) max = 1;

		int[] wins = new int[max];

		Random me = new Random();

		for (int x = 0; x < SIMULATION_ROUNDS; x++) {

			
			int bet = me.nextInt(max) + 1;// FIX THIS CANT GET RANDOM ON 0

			if (simulate(bag, game, letter, bet, money))
				wins[bet - 1]++;
		}

		int winner = 0;

		int best = Integer.MIN_VALUE;

		for (int x = 0; x < max; x++) {
			log.trace((x + 1) + ": " + wins[x]);
			if (wins[x] > best) {
				winner = x + 1;
				best = wins[x];
			}

		}

		return winner;
	}

	private boolean simulate(List<Character> bag, List<Character> game,
			Character letter, int bet, int money) {
		Random me = new Random();
		Random players = new Random();
		Random index = new Random();

		int match = players.nextInt(100 / 4) + 1;
		int spent = 0;
		int steps = gameSteps;

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

	private int gameSteps = 0;

	private boolean getWin(List<Character> bag, int spent) {

		if (bag.size() >= 10) {

			if (spent == 0 || (double) spent / bag.size() < 8)
				return true;

		}

		return false;
	}

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

	private Random coin = new Random();

	private boolean heads() {
		return coin.nextInt(100) % 2 == 0;
	}

	@Override
	public void newGame(int id, int number_of_rounds, int number_of_players) {
		gameSteps = 8 * number_of_players;// FIX THIS
	}

	@Override
	public void newRound(SecretState secretState, int current_round) {
		playerScore = secretState.getScore();
		myLetters = new ArrayList<Character>();
	}

	@Override
	public int getBid(Letter bidLetter, ArrayList<PlayerBids> PlayerBidList,
			ArrayList<String> PlayerList, SecretState secretstate) {
		List<Letter> list = LetterGame.getRemainingLetters();
		List<Character> gameLetters = new ArrayList<Character>();

		playerScore = secretstate.getScore();

		for (Letter l : list)
			gameLetters.add(l.getCharacter());

		return monteCarlo(myLetters, gameLetters, bidLetter.getCharacter());
	}

	@Override
	public void bidResult(boolean won, Letter letter, PlayerBids bids) {
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
	}

}
