package com.reader.common.impl;

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
			public void processWord(char[] text, int start, int length) {
				String word = new String(text, start, length);
				WordAttributes attrs = database.get(word.toLowerCase());
				if (attrs == null) {
					if (buffer.size() == 0) {
						defaultTextWithProperties.setText(word);
						defaultTextWithProperties
								.setColor(ColorConstants.DEFAULT);
						got(SimpleTextSource.this.textProcessor,
								defaultTextWithProperties);
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
						got(SimpleTextSource.this.textProcessor,
								defaultTextWithProperties);
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
					strings[i] = buffer.get(i).getText().toLowerCase();

				WordAttributes attrs = database.get(strings);
				if (attrs == null)
					while (buffer.size() > 0)
						got(SimpleTextSource.this.textProcessor,
								buffer.remove(0));
				else if (attrs.isTransport())
					return;
				else
					for (TextWithProperties twp : buffer)
						twp.setColor(attrs.getColor());
			}
		};
		parser.parse(text.toCharArray());
		while (buffer.size() > 0)
			got(textProcessor, buffer.remove(0));
		textProcessor.end();
	}

	private void got(TextProcessor processor, TextWithProperties textProperties) {
		for (int i = textProperties.getText().length() - 1; i >= 0; --i)
			if (Character.isDigit(textProperties.getText().charAt(i))) {
				textProperties.setColor(ColorConstants.WHITE);
				break;
			}
		processor.got(textProperties);
	}

	@Override
	public void update(final TextWithProperties properties) {
		final List<String> words = new ArrayList<String>();
		SimpleTextParser smallParser = new SimpleTextParser() {

			@Override
			public void processWord(char[] t, int start, int l) {
				words.add(new String(t, start, l).toLowerCase());
			}

		};

		smallParser.parse(properties.getText().toCharArray());

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
		twp.setWords(words);

		textProcessor.updated(twp);
	}

	@Override
	public void markColor(String[] words, String color) {
		WordAttributes attributes = new WordAttributes();
		attributes.setColor(color);
		database.updateWords(words, attributes);
	}
}
