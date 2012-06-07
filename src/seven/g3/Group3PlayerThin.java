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

import seven.ui.Letter;
import seven.ui.LetterGame;
import seven.ui.Player;
import seven.ui.PlayerBids;
import seven.ui.SecretState;

public class Group3PlayerThin implements Player {

	

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

		
		int numWith = getSevenCount(this.currentLetters, c);
		int numWithout = getSevenCount(this.currentLetters);
		
		log.trace("numWith: " + numWith);
		log.trace("numWithOut: " + numWithout);
		
		
		double wWith = 20 * ((numWith * (currentLetters.size() + 1)) + numWith * (7 - (double) currentLetters.size() + 1 / LetterGame.getRemainingLetters().size() ));
		double wWithout = (numWithout * currentLetters.size()) + numWithout * (7 - (double) currentLetters.size() / LetterGame.getRemainingLetters().size() );
		
		log.trace("with: " + wWith);
		log.trace("without: " + wWithout);
		
		if(wWith > wWithout) {
			return 20;
		}
		else {
			return 5;
		}
		
		/*for (String letters : goodLetters)
			if (letters.contains(c + "")) {
				//maxSize = set.size() > maxSize ? set.size() : maxSize;
				collides++;
			}
		
		log.trace(collides);*/

		//int bet = (int) (((double) collides / goodLetters.size()) * (secretstate.getScore() / 2));
		/*int bet = 0;
		
		Random r = new Random();

		return bet == 0 ? r.nextInt(secretstate.getScore() / 2) : bet;*/
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
		//Collections.copy(left, LetterGame.getRemainingLetters());
		
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
