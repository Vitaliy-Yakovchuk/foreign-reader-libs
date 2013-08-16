package com.reader.common.impl;

import java.io.IOException;
import java.io.Reader;

import com.reader.common.AbstractTextParser;
import com.reader.common.TextProcessor;
import com.reader.common.TextWithProperties;

public abstract class SimpleTextParser extends AbstractTextParser {

	@Override
	public void parser(Reader reader, TextProcessor processor) {
		TextWithProperties textWithProperties = new TextWithProperties();
		StringBuffer sb = new StringBuffer();
		int character;
		try {
			while ((character = reader.read()) != -1) {
				if (Character.isWhitespace(character)) {
					if (sb.length() > 0) {
						textWithProperties.setText(sb.toString());
						fillAttributes(textWithProperties);
						processor.got(textWithProperties);
						sb.setLength(0);
					}
				} else
					sb.appendCodePoint(character);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (sb.length() > 0) {
			textWithProperties.setText(sb.toString());
			sb.setLength(0);
		}
	}

	public abstract void fillAttributes(TextWithProperties textWithProperties);

}
