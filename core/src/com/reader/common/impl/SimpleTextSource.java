package com.reader.common.impl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.reader.common.AbstractTextSource;
import com.reader.common.ColorConstants;
import com.reader.common.Database;
import com.reader.common.TextProcessor;
import com.reader.common.TextWithProperties;
import com.reader.common.persist.WordAttributes;

public class SimpleTextSource extends AbstractTextSource {

	private String text;

	private Database database;

	private TextProcessor textProcessor;

	public SimpleTextSource(String text, Database database) {
		this.text = text;
		this.database = database;
	}

	@Override
	public void process(final TextProcessor textProcessor) {
		this.textProcessor = textProcessor;
		final List<TextWithProperties> buffer = new LinkedList<TextWithProperties>();
		final TextWithProperties defaultTextWithProperties = new TextWithProperties();
		SimpleTextParser parser = new SimpleTextParser() {

			@Override
			public void processWord(String word) {
				WordAttributes attrs = database.get(word.toLowerCase());
				if (attrs == null) {
					if (buffer.size() == 0) {
						defaultTextWithProperties.setText(word);
						defaultTextWithProperties
								.setColor(ColorConstants.DEFAULT);
						SimpleTextSource.this.textProcessor
								.got(defaultTextWithProperties);
					} else {
						TextWithProperties textWithProperties = new TextWithProperties();
						textWithProperties.setText(word);
						textWithProperties.setColor(ColorConstants.DEFAULT);
						buffer.add(textWithProperties);
						pushBuff();
					}
				} else if (attrs.isStartsWithPhrase()) {
					TextWithProperties textWithProperties = new TextWithProperties();
					textWithProperties.setText(word);
					textWithProperties.setColor(attrs.getColor());
					buffer.add(textWithProperties);
					pushBuff();
				} else {
					if (buffer.size() == 0) {
						defaultTextWithProperties.setText(word);
						defaultTextWithProperties.setColor(attrs.getColor());
						SimpleTextSource.this.textProcessor
								.got(defaultTextWithProperties);
					} else {
						TextWithProperties textWithProperties = new TextWithProperties();
						textWithProperties.setText(word);
						textWithProperties.setColor(attrs.getColor());
						buffer.add(textWithProperties);
						pushBuff();
					}
				}
			}

			private void pushBuff() {
				if (buffer.size() == 1)
					return;
				String[] strings = new String[buffer.size()];
				for (int i = strings.length - 1; i >= 0; --i)
					strings[i] = buffer.get(i).getText();

				WordAttributes attrs = database.get(strings);
				if (attrs == null)
					while (buffer.size() > 0)
						SimpleTextSource.this.textProcessor.got(buffer
								.remove(0));
				else if (attrs.isTransport())
					return;
				else
					for (TextWithProperties twp : buffer)
						twp.setColor(attrs.getColor());
			}
		};
		parser.parse(new StringReader(text));
		while (buffer.size() > 0)
			textProcessor.got(buffer.remove(0));
		textProcessor.end();
	}

	@Override
	public void update(final TextWithProperties properties) {
		final List<String> words = new ArrayList<String>();
		final List<String> wordsA = new ArrayList<String>();
		SimpleTextParser smallParser = new SimpleTextParser() {

			@Override
			public void processWord(String word) {
				words.add(word.toLowerCase());
				wordsA.add(word);
			}

		};

		smallParser.parse(new StringReader(properties.getText()));

		if (words.size() == 1) {
			String word = words.get(0);
			WordAttributes wordAttributes = database.get(word);
			if (wordAttributes == null)
				wordAttributes = new WordAttributes();
			else if (wordAttributes.getColor().equals(properties.getColor()))
				return;
			wordAttributes.setColor(properties.getColor());
			database.put(word, wordAttributes);
		} else if (words.size() > 1) {
			String[] ss = words.toArray(new String[words.size()]);
			WordAttributes wa = database.get(ss);
			if (wa != null && !wa.isTransport()
					&& wa.getColor().equals(properties.getColor()))
				return;
			wa = new WordAttributes();
			wa.setColor(properties.getColor());
			database.put(ss, wa);
		} else
			return;

		if (textProcessor == null)
			return;

		TextWithProperties twp = new TextWithProperties();
		twp.setColor(properties.getColor());
		twp.setText(add(wordsA));

		textProcessor.updated(twp);
	}

	private String add(List<String> words) {
		StringBuffer sb = null;
		for (String w : words) {
			if (sb == null)
				sb = new StringBuffer();
			else
				sb.append(Database.SPACE);
			sb.append(w);
		}
		return sb.toString();
	}

	@Override
	public void markColor(String[] words, String color) {
		WordAttributes attributes = new WordAttributes();
		attributes.setColor(color);
		database.updateWords(words, attributes);		
	}
}
