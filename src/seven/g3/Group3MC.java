package seven.g3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import seven.g3.Group3Player.CharBag;
import seven.ui.Letter;
import seven.ui.LetterGame;
import seven.ui.Player;
import seven.ui.PlayerBids;
import seven.ui.SecretState;

public class Group3MC implements Player {
	
	@SuppressWarnings("serial")
	private class CharBag extends ArrayList<Character> {
		public CharBag(String word) {
			for (char c : word.toCharArray())
				this.add(c);
		}
		
		public CharBag(List<Character> list) {
			super.addAll(list);
		}

		public CharBag(CharBag c) {
			super(c);
		}

		public CharBag() {
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof CharBag)
				return equals((CharBag) obj);

			return false;
		}

		@Override
		public int hashCode() {
			Collections.sort(this);
			return super.hashCode();
		}

		public boolean equals(CharBag c) {
			Collections.sort(this);
			Collections.sort(c);
			return super.equals(c);
		}
	}
	
	Set<CharBag> sevenLetters = new HashSet<CharBag>();

	

	private final Logger log = Logger.getLogger(this.getClass());



	private List<Set<Character>> makeSeven = new ArrayList<Set<Character>>();
	private int myID;
	private List<Character> currentLetters;
	private List<Word> wordlist = new ArrayList<Word>();
	
	
	private Set<String> goodLetters= new HashSet<String>();

	{
		try {
			BufferedReader r = new BufferedReader(new FileReader("textFiles/7letterWords.txt"));
			String line = r.readLine(); // skip first line

			while (null != (line = r.readLine())) {
				line = line.trim();
				line = line.substring(line.indexOf(',') + 1);

				wordlist.add(new Word(line));

				if (line.length() == 7) {//thin
					char[] letters = line.toCharArray();
					Arrays.sort(letters);

					goodLetters.add(String.valueOf(letters));
				}
				
				/*if (line.length() == 7) {//thick
					Set<Character> row = new HashSet<Character>();

					for (char c : line.toCharArray())
						row.add(c);

					makeSeven.add(row);
				}*/
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//log.trace("thin: " + goodLetters.size());
		//log.trace("thick: " + makeSeven.size());
	}



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
		char c = bidLetter.getCharacter();
		int maxSize = Integer.MIN_VALUE;
		int collides = 0;

		
		runMC();
		
		
		return 0;
	}

	private void runMC() {
		char[] mine;
		char[] left;
		char letter;
		boolean choice;
		int money;
		
		int bid;
		
		int wins = searchBranch(mine, left, letter, choice, bid, money);

		
		
	}
	
	private int score(List<Character> word) {
		return 100;
	}

	private boolean searchBranch(List<Character> mine, List<Character> left, int money) {
		
		if(mine.size() == 7) {
			if (sevenLetters.contains(new CharBag(mine))) {
				if(money + score(mine) >= 100) {	
					return true;
				}
			}
			
			return false;
				
		}
		
		
		
		                     
		
		
		return searchBranch(mine[]);
	}

	private int getSevenCount(List<Character> currentLetters, char c) {
		List<Character> temp = new ArrayList<Character>();
		
		for(Character l :currentLetters ) temp.add(l);
		
		temp.add(c);
		return getSevenCount(temp);
	}

	private int getSevenCount(List<Character> letters) {
		String subString = "";
		
		for(Character c : letters) {
			
			subString = subString + c.charValue();
		}
		
		int count = 0;
		ArrayList<Letter> left = new ArrayList<Letter>(LetterGame.getRemainingLetters());
		/*ArrayList<Letter> left = new ArrayList<Letter>();
		left.add(new Letter(new Character('a'), 1));
		left.add(new Letter(new Character('b'), 2));
		left.add(new Letter(new Character('c'), 3));
		left.add(new Letter(new Character('d'), 4));*/
		
		int needed = 7 - letters.size();
		
		HashSet<String> subSets = getSubSets(needed, left);	
		 
		for(String s : subSets) {
			
			char[] temp = (subString + s).toCharArray();
			Arrays.sort(temp);
			
			if (goodLetters.contains(String.copyValueOf(temp))){
				count++;
			}
		}
		
		
		
		/*ArrayList<Letter> test = new ArrayList<Letter>();
		test.add(new Letter(new Character('a'), 1));
		test.add(new Letter(new Character('b'), 2));
		test.add(new Letter(new Character('c'), 3));
		test.add(new Letter(new Character('d'), 4));
		
		HashSet<String> subSets = getSubSets(3, test);*/	
		
		
		
		return count;
	}

	private HashSet<String> getSubSets(int needed, ArrayList<Letter> left) {
		HashSet<String> subSets = new HashSet<String>();
		log.trace("GETTING SETS");
		for (int i = 0; i < left.size(); i++) {
			if (needed > 1) {
				for (int j = i + 1; j < left.size(); j++) {					
					if (needed > 2) {
						for (int k = j + 1; k < left.size(); k++) {
							if (needed > 3) {
								
								for (int l = k + 1; l < left.size(); l++) {
									if(needed > 4) {
										for (int m = l + 1; m < left.size(); m++) {
											if(needed > 5) {
												log.trace("going round");
												for (int n = m + 1; n < left.size(); n++) {
													subSets.add(left.get(i).getCharacter().charValue()
															+ ""
															+ left.get(j).getCharacter().charValue()
															+ left.get(k).getCharacter().charValue()
															+ left.get(l).getCharacter().charValue()
															+ left.get(m).getCharacter().charValue()
															+ left.get(n).getCharacter().charValue());
												}
											}else {
												subSets.add(left.get(i).getCharacter().charValue()
														+ ""
														+ left.get(j).getCharacter().charValue()
														+ left.get(k).getCharacter().charValue()
														+ left.get(l).getCharacter().charValue()
														+ left.get(m).getCharacter().charValue());
											}
										}
		
									} else {
										subSets.add(left.get(i).getCharacter().charValue()
												+ ""
												+ left.get(j).getCharacter().charValue()
												+ left.get(k).getCharacter().charValue()
												+ left.get(l).getCharacter().charValue());
									}
									
									subSets.add(left.get(i).getCharacter().charValue()
											+ ""
											+ left.get(j).getCharacter().charValue()
											+ left.get(k).getCharacter().charValue());
								}
							} else {
								subSets.add(left.get(i).getCharacter().charValue()
										+ ""
										+ left.get(j).getCharacter().charValue()
										+ left.get(k).getCharacter().charValue());
							}
							
							
						}
					} else {
						subSets.add(left.get(i).getCharacter().charValue() + ""
								+ left.get(j).getCharacter().charValue());
					}

				}
			} else {
				subSets.add(left.get(i).getCharacter().charValue() + "");
			}

		}

		
		log.trace("DONE GETTING SETS");
		return subSets;
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
