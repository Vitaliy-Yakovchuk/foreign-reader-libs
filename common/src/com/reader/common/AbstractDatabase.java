package com.reader.common;

import java.nio.charset.Charset;

import com.reader.common.persist.WordAttributes;

public abstract class AbstractDatabase implements Database {

	private static Charset charset = Charset.forName("UTF-8");

	public void commit() {
	};

	public void rollback() {
	};

	@Override
	public void put(String[] phrase, WordAttributes wordAttributes) {
		try {
			StringBuffer sb = new StringBuffer();
			int l = phrase.length;
			for (int i = 0; i < l; i++) {
				String word = phrase[i];
				sb.append(word);
				String text = sb.toString();
				WordAttributes wa = getA(text);
				boolean startsWithPhrase = i < l - 1;
				if (wa == null) {
					wa = new WordAttributes();
					wa.setTransport(startsWithPhrase);
					wa.setStartsWithPhrase(startsWithPhrase);
					if (!startsWithPhrase)
						wa.setColor(wordAttributes.getColor());
					putA(text, wa);
				} else if (!wa.isStartsWithPhrase()) {
					wa.setStartsWithPhrase(startsWithPhrase);
					if (!startsWithPhrase)
						wa.setColor(wordAttributes.getColor());
					putA(text, wa);
				}
				sb.append(SPACE);
			}
			commit();
		} catch (Exception e) {
			e.printStackTrace();
			rollback();
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
		return getA(sb.toString());
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
		removeA(sb.toString());
	}

	public byte[] bytes(WordAttributes attributes) {
		byte[] color = bytes(attributes.getColor());
		byte[] res = new byte[color.length + 3];
		res[0] = (byte) color.length;
		for (int i = color.length - 1; i >= 0; --i) {
			res[i + 1] = color[i];
		}
		res[res.length - 2] = ((attributes.isStartsWithPhrase()) ? (byte) 1
				: (byte) 0);
		res[res.length - 1] = ((attributes.isTransport()) ? (byte) 1 : (byte) 0);
		return res;
	}

	public WordAttributes fromBytes(byte[] bs) {
		if (bs == null)
			return null;
		WordAttributes wa = new WordAttributes();
		wa.setColor(new String(bs, 1, bs[0], charset));
		wa.setStartsWithPhrase(bs[bs.length - 2] == (byte) 1);
		wa.setTransport(bs[bs.length - 1] == (byte) 1);
		return wa;
	}

	public byte[] bytes(String string) {
		return string.getBytes(charset);
	}

	public String fromBytesToString(byte[] bs) {
		return new String(bs, charset);
	}

	@Override
	public WordAttributes get(String word) {
		return getA(word);
	}

	@Override
	public void put(String word, WordAttributes wordAttributes) {
		putA(word, wordAttributes);
		commit();
	}

	@Override
	public void remove(String word) {
		removeA(word);
		commit();
	}

	@Override
	public void updateWords(String[] words, WordAttributes attributes) {
		for (String word : words) {
			WordAttributes wordAttributes = getA(word);
			if (wordAttributes == null)
				putA(word, attributes);
			else {
				wordAttributes.setColor(attributes.getColor());
				putA(word, wordAttributes);
			}
		}
		commit();
	}

	public abstract void putA(String word, WordAttributes wordAttributes);

	public abstract WordAttributes getA(String word);

	public abstract void removeA(String word);

}
