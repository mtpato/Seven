package seven.g3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.io.*;

import org.apache.log4j.Logger;

import seven.ui.Letter;
import seven.ui.LetterGame;
import seven.ui.Player;
import seven.ui.PlayerBids;
import seven.ui.SecretState;

public class g3player implements Player {

	
	/*
	 * This player bids randomly.
	 */

	// an array of words to be used for making decisions
	private static final Word[] wordlist;

	// for logging
	private Logger log = Logger.getLogger(this.getClass());

	// the set of letters that this player currently has
	private ArrayList<Character> currentLetters;
	
	// unique ID
	private int myID;
	
	// for generating random numbers
	private Random random = new Random();
	
	private Map<String, Integer> goalWords; 
		
	
	/* This code initializes the word list */
	static {
		BufferedReader r;
		String line = null;
		ArrayList<Word> wtmp = new ArrayList<Word>(55000);
		try {
			// you can use textFiles/dictionary.txt if you want the whole list
			r = new BufferedReader(new FileReader("textFiles/super-small-wordlist.txt"));
			while (null != (line = r.readLine())) {
				wtmp.add(new Word(line.trim()));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wordlist = wtmp.toArray(new Word[wtmp.size()]);
	}

	
    /*
     * This is called once at the beginning of a Game.
     * The id is what the game considers to be your unique identifier
     * The number_of_rounds is the total number of rounds to be played in this game
     * The number_of_players is, well, the number of players.
     */
	public void newGame(int id, int number_of_rounds, int number_of_players) {
		myID = id;
		goalWords = new HashMap<String,Integer>();
		fillHash();
		
	}
	
	private Map<Character, Integer> count;
	
	private int genCounts(List<Character> ours) {
		List<Letter> bag = LetterGame.getRemainingLetters();
		
		if (ours.size() == 7) //&& makes 7 letter word
			return 1;
		
		for (Letter c : bag) {
			List<Character> l = new ArrayList<Character>(ours);
			l.add(c.getCharacter());
			
			count.put(c.getCharacter(), genCounts(l));		
		}
		
		return 0;
	}


	private void fillHash() {
		BufferedReader r;
		String line = null;
		ArrayList<Word> wtmp = new ArrayList<Word>(55000);
		try {
			// you can use textFiles/dictionary.txt if you want the whole list
			r = new BufferedReader(new FileReader("textFiles/7letterWords.txt"));
			while (null != (line = r.readLine())) {
				
				
				line = sort(line);
				Integer i = goalWords.get(line);
				
				if(goalWords.containsKey(line)) {
					 i = new Integer(i + 1);
				}	
				
				goalWords.put(line, i);			
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	private String sort(String line) {
		
		char[] list = line.toCharArray();
		//Collections.sort(list);
		Arrays.sort(list, 0, list.length);
		
		return String.valueOf(list);
	}


	/*
	 * This method is called at the beginning of a new round.
	 * The secretState contains your current score and the letters that were secretly given to you in this round
	 * The current_round indicates the current round number (0-based)
	 */
	public void newRound(SecretState secretState, int current_round) {

		// be sure to reinitialize the list at the start of the round
		
		myBag = new ArrayList<Character>();
	}
	
	private int playerScore = 100;
	

	/*
	 * This method is called when there is a new letter available for bidding.
	 * bidLetter = the Letter that is being bid on
	 * playerBidList = the list of all previous bids from all players
	 * playerList = the class names of the different players
	 * secretState = your secret state (which includes the score)
	 */
	public int getBid(Letter bidLetter, ArrayList<PlayerBids> playerBidList, ArrayList<String> playerList, SecretState secretState) {
		
		playerScore = secretState.getScore();
		return playerScore / 6;
	}

	
	private boolean wantLetter(Letter bidLetter) {
		
		
		int i = getLetterScore(bidLetter);
		log.trace(i);
		return false;
		
	}


	private int getLetterScore(Letter bidLetter) {
		
		
		
		
		// TODO Auto-generated method stub
		return 0;
	}

	List<Character> myBag;
	
	/*
	 * This method is called after a bid. It indicates whether or not the player
	 * won the bid and what letter was being bid on, and also includes all the
	 * other players' bids. 
	 */
    public void bidResult(boolean won, Letter letter, PlayerBids bids) {
    	if (won) {
    		myBag.add(letter.getCharacter());
    	}
    	else {
    		//logger.trace("My ID is " + myID + " and I lost the bid for " + letter);
    	}
    }

    /*
     * This method is called after all the letters have been purchased in the round.
     * The word that you return will be scored for this round.
     */
	public String getWord() {
		char c[] = new char[myBag.size()];
		for (int i = 0; i < c.length; i++) {
			c[i] = myBag.get(i);
		}
		String s = new String(c);
		Word ourletters = new Word(s);
		Word bestword = new Word("");
		for (Word w : wordlist) {
			if (ourletters.contains(w)) {
				if (w.score > bestword.score) {
					bestword = w;
				}

			}
		}
		log.trace("My ID is " + myID + " and my word is " + bestword.word);
		
		return bestword.word;
	}

	/*
	 * This method is called at the end of the round
	 * The ArrayList contains the scores of all the players, ordered by their ID
	 */
	public void updateScores(ArrayList<Integer> scores) {
		
	}




}
