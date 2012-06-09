package seven.g3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;



public class StatSim {
	
	public static void main(String[] args) {
		double i = new StatSim().runSim(10000, 16);
		
		System.out.println("percent: "+ i);
		
	}

	private List<Character> genChars(Character c, int num) {
		List<Character> l = new ArrayList<Character>();
		for (int x = 0; x < num; x++)
			l.add(c);
		return l;
	}

	private class LetterCount {
		Character c;
		int count;

		public LetterCount(Character c, int count) {
			this.c = c;
			this.count = count;
		}
	}

	private List<LetterCount> counts = Arrays.asList(new LetterCount('A', 9),
			new LetterCount('B', 2), new LetterCount('C', 2), new LetterCount(
					'D', 4), new LetterCount('E', 12), new LetterCount('F', 2),
			new LetterCount('G', 3), new LetterCount('H', 2), new LetterCount(
					'I', 9), new LetterCount('J', 1), new LetterCount('K', 1),
			new LetterCount('L', 4), new LetterCount('M', 2), new LetterCount(
					'N', 6), new LetterCount('O', 8), new LetterCount('P', 2),
			new LetterCount('Q', 1), new LetterCount('R', 6), new LetterCount(
					'S', 4), new LetterCount('T', 6), new LetterCount('U', 4),
			new LetterCount('V', 2), new LetterCount('W', 2), new LetterCount(
					'X', 1), new LetterCount('Y', 2), new LetterCount('Z', 1));

	private List<Character> charBag = new ArrayList<Character>();
	private List<Word> makeSeven = new ArrayList<Word>();

	{
		for (LetterCount c : counts)
			charBag.addAll(genChars(c.c, c.count));
	}

	{
		try {
			BufferedReader r = new BufferedReader(new FileReader(
					"textFiles/dictionary.txt"));
			String line = r.readLine(); // skip first line

			while (null != (line = r.readLine())) {
				line = line.trim();
				line = line.substring(line.indexOf(',') + 1);

				if (line.length() == 7)
					makeSeven.add(new Word(line));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public double runSim(int sims, int n) {
		Random r = new Random();
		int count = 0;

		for (int x = 0; x < sims; x++) {
			List<Character> bag = new ArrayList<Character>(charBag);
			List<Character> myBag = new ArrayList<Character>();

			for (int y = 0; y < n; y++) {
				Character c = bag.get(r.nextInt(bag.size()));

				bag.remove(c);
				myBag.add(c);
			}

			if (makeSeven(myBag))
				count++;
		}

		return (double) count / sims;
	}

	public boolean makeSeven(List<Character> bag) {
		char c[] = new char[bag.size()];

		for (int i = 0; i < bag.size(); i++)
			c[i] = bag.get(i);

		Word letters = new Word(new String(c));

		for (Word w : makeSeven)
			if (letters.contains(w))
				return true;

		return false;
	}
	
}
