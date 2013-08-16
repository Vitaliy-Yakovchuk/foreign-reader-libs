package com.reader.common;

import com.reader.common.persist.WordAttributes;

public abstract class AbstractDatabase implements Database {

	public void startTransaction() {
	};

	public void commitTransaction() {
	};

	public void rollbackTransaction() {
	};

	@Override
	public void put(String[] phrase, WordAttributes wordAttributes) {
		try {
			startTransaction();
			StringBuffer sb = new StringBuffer();
			int l = phrase.length;
			for (int i = 0; i < l; i++) {
				String word = phrase[i];
				sb.append(word);
				String text = sb.toString();
				WordAttributes wa = get(text);
				boolean startsWithPhrase = i < l - 1;
				if (wa == null) {
					wa = new WordAttributes();
					wa.setTransport(startsWithPhrase);
					wa.setStartsWithPhrase(startsWithPhrase);
					if (!startsWithPhrase)
						wa.setColor(wordAttributes.getColor());
					put(text, wa);
				} else if (!wa.isStartsWithPhrase()) {
					wa.setStartsWithPhrase(startsWithPhrase);
					if (!startsWithPhrase)
						wa.setColor(wordAttributes.getColor());
					put(text, wa);
				}
				sb.append(SPACE);
			}
			commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			rollbackTransaction();
		}
	}

	@Override
	public WordAttributes get(String[] phrase) {
		StringBuffer sb = new StringBuffer();
		int l = phrase.length - 1;
		for (int i = 0; i < l; i++) {
			String word = phrase[i];
			sb.append(word);
			sb.append(SPACE);
		}
		sb.append(phrase[l]);
		return get(sb.toString());
	}

	@Override
	public void remove(String[] phrase) {
		StringBuffer sb = new StringBuffer();
		int l = phrase.length - 1;
		for (int i = 0; i < l; i++) {
			String word = phrase[i];
			sb.append(word);
			sb.append(SPACE);
		}
		sb.append(phrase[l]);
		remove(sb.toString());

	}
}
