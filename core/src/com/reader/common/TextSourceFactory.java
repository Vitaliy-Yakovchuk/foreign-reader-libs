package com.reader.common;

import com.reader.common.impl.SimpleTextSource;

public class TextSourceFactory {

	public static TextSource createSimpleSource(String text) {
		SimpleTextSource textSource = new SimpleTextSource();

		textSource.addSection(text);

		return textSource;
	}

}
