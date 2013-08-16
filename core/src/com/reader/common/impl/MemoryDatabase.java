package com.reader.common.impl;

import java.util.HashMap;

import com.reader.common.AbstractDatabase;
import com.reader.common.persist.WordAttributes;

public class MemoryDatabase extends AbstractDatabase {

	private HashMap<String, WordAttributes> attributes = new HashMap<String, WordAttributes>();

	@Override
	public void put(String word, WordAttributes wordAttributes) {
		WordAttributes wordAttributes2 = copy(wordAttributes);
		attributes.put(word, wordAttributes2);
	}

	private WordAttributes copy(WordAttributes wordAttributes) {
		if (wordAttributes == null)
			return null;
		WordAttributes wordAttributes2 = new WordAttributes();
		wordAttributes2.setColor(wordAttributes.getColor());
		wordAttributes2.setTransport(wordAttributes.isTransport());
		wordAttributes2
				.setStartsWithPhrase(wordAttributes.isStartsWithPhrase());
		return wordAttributes2;
	}

	@Override
	public WordAttributes get(String word) {
		return copy(attributes.get(word));
	}

	@Override
	public void remove(String word) {
		attributes.remove(word);
	}

}
