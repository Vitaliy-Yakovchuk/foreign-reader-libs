package com.reader.common.impl;

import com.reader.common.AbstractTextParser;

public abstract class SimpleTextParser extends AbstractTextParser {

	@Override
	public void parse(char[] text) {
		int start = 0;
		int length = 0;
		char character;
		int l = text.length;
		int i = 0;
		boolean f = true;
		while (i < l) {
			character = text[i];
			if (!isTextPart(character) || length > MAX_WORD_LENGTH) {
				if (length > 0) {
					processWord(text, start, length);
					length = 0;
					f = true;
				}
			} else {
				if (f) {
					start = i;
					f = false;
				}
				length++;
			}
			i++;
		}

		if (length > 0) {
			processWord(text, start, length);
		}
	}

	public static boolean isTextPart(char c) {
		if (c == '\'')
			return true;
		return Character.isUnicodeIdentifierPart(c);
	}

	public static boolean isTextPart(int c) {
		return Character.isUnicodeIdentifierPart(c);
	}

	public abstract void processWord(char[] text, int start, int length);
}
