package com.reader.common.impl;

import java.io.IOException;
import java.io.Reader;

import com.reader.common.AbstractTextParser;

public abstract class SimpleTextParser extends AbstractTextParser {

	@Override
	public void parse(Reader reader) {
		StringBuffer sb = new StringBuffer();
		int character;
		try {
			while ((character = reader.read()) != -1) {
				if (!Character.isUnicodeIdentifierPart(character)) {
					if (sb.length() > 0) {
						processWord(sb.toString());
						sb.setLength(0);
					}
				} else
					sb.appendCodePoint(character);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (sb.length() > 0) {
			processWord(sb.toString());
			sb.setLength(0);
		}
	}

	public abstract void processWord(String word);
}
