package seven.g3;

import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("serial")
class CharBag extends ArrayList<Character> {
	
	public CharBag(String word) {
		for (char c : word.toCharArray())
			this.add(c);
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
