package seven.g3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import seven.ui.Letter;
import seven.ui.LetterGame;
import seven.ui.Player;
import seven.ui.PlayerBids;
import seven.ui.SecretState;

public class Group3PlayerThin implements Player {

	/*
	 * @SuppressWarnings("serial") /private class CharBag extends
	 * ArrayList<Character> { public CharBag(String word) { for (char c :
	 * word.toCharArray()) this.add(c); }
	 * 
	 * public CharBag(CharBag c) { super(c); }
	 * 
	 * public CharBag() { }
	 * 
	 * @Override public boolean equals(Object obj) { if (obj instanceof CharBag)
	 * return equals((CharBag) obj);
	 * 
	 * return false; }
	 * 
	 * @Override public int hashCode() { Collections.sort(this); return
	 * super.hashCode(); }
	 * 
	 * public boolean equals(CharBag c) { Collections.sort(this);
	 * Collections.sort(c); return super.equals(c); } }
	 */

	/*
	 * private class MakeSeven { public boolean yes; public int count; public
	 * List<Character> characters;
	 * 
	 * public MakeSeven(boolean yes, int count, List<Character> characters) {
	 * this.yes = yes; this.count = count; this.characters = characters; } }
	 */

	private final Logger log = Logger.getLogger(this.getClass());

	// private final Set<CharBag> sevenBags = new HashSet<CharBag>();
	// private CharBag myBag = new CharBag();

	// private List<Word> sevenList = new ArrayList<Word>();

	private List<Set<Character>> makeSeven = new ArrayList<Set<Character>>();
	private int myID;
	private List<Character> currentLetters;
	private List<Word> wordlist = new ArrayList<Word>();
	
	private Set<String> goodLetters= new HashSet<String>();

	{
		try {
			BufferedReader r = new BufferedReader(new FileReader("textFiles/dictionary.txt"));
			String line = r.readLine(); // skip first line

			while (null != (line = r.readLine())) {
				line = line.trim();
				line = line.substring(line.indexOf(',') + 1);

				wordlist.add(new Word(line));

				if (line.length() == 7) {
					Set<Character> row = new HashSet<Character>();

					for (char c : line.toCharArray())
						row.add(c);

					makeSeven.add(row);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// private List<Character> myBag = new ArrayList<Character>();

	/*
	 * private class MakeSeven { public int count; public Word w;
	 * 
	 * public MakeSeven(int count, Word w) { this.count = count; this.w = w; } }
	 * 
	 * private class BettingTable { public Map<Character, Integer> map = new
	 * HashMap<Character, Integer>(); public int max = Integer.MIN_VALUE; }
	 * 
	 * private BettingTable buildBettingTable(List<Character> bagList,
	 * List<Character> remainingChars) { BettingTable tbl = new BettingTable();
	 * String bag = new String();
	 * 
	 * for (Character c : bagList) bag += c;
	 * 
	 * for (Character c : remainingChars) { List<Character> r = new
	 * ArrayList<Character>(remainingChars); r.remove(c);
	 * 
	 * MakeSeven s = makeSeven(new Word(bag + c), r, 0);
	 * 
	 * if (s.count > tbl.max) tbl.max = s.count;
	 * 
	 * tbl.map.put(c, s.count); }
	 * 
	 * log.trace("Done!");
	 * 
	 * return tbl; }
	 * 
	 * private MakeSeven makeSeven(Word bag, List<Character> rest, int depth) {
	 * if (depth == 1) return new MakeSeven(0, null);
	 * 
	 * for (Word w : sevenList) if (bag.contains(w)) return new MakeSeven(1, w);
	 * 
	 * MakeSeven ret = new MakeSeven(0, null);
	 * 
	 * for (Character c : rest) { List<Character> r = new
	 * ArrayList<Character>(rest); r.remove(c);
	 * 
	 * MakeSeven s = makeSeven(new Word(bag.word + c), r, depth + 1);
	 * 
	 * if (s.count > 0 && s.w.word.indexOf(c) > -1) {// fix this later ret.count
	 * += s.count; ret.w = s.w; } }
	 * 
	 * return ret; }
	 */

	@Override
	public void newGame(int id, int number_of_rounds, int number_of_players) {
		myID = id;
	}

	@Override
	public void newRound(SecretState secretState, int current_round) {
		currentLetters = new ArrayList<Character>();

		for (Letter l : secretState.getSecretLetters())
			currentLetters.add(l.getCharacter());
	}

	@Override
	public int getBid(Letter bidLetter, ArrayList<PlayerBids> PlayerBidList, ArrayList<String> PlayerList, SecretState secretstate) {
		Character c = bidLetter.getCharacter();
		int maxSize = Integer.MIN_VALUE;
		int collides = 0;

		for (Set<Character> set : makeSeven)
			if (set.contains(c)) {
				maxSize = set.size() > maxSize ? set.size() : maxSize;
				collides++;
			}

		int bet = (int) (((double) collides / makeSeven.size()) * (secretstate.getScore() / 2));

		Random r = new Random();

		return bet == 0 ? r.nextInt(secretstate.getScore() / 2) : bet;
	}

	@Override
	public void bidResult(boolean won, Letter letter, PlayerBids bids) {
		if (won) {
			List<Set<Character>> newSet = new ArrayList<Set<Character>>();
			Character c = letter.getCharacter();

			for (Set<Character> set : makeSeven)
				if (set.contains(c)) {
					set.remove(c);
					newSet.add(set);
				}

			makeSeven = newSet;
			currentLetters.add(letter.getCharacter());
		}
	}

	@Override
	public String getWord() {
		char c[] = new char[currentLetters.size()];
		for (int i = 0; i < c.length; i++) {
			c[i] = currentLetters.get(i);
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

	@Override
	public void updateScores(ArrayList<Integer> scores) {
	}

}
