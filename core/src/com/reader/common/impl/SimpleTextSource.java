package com.reader.common.impl;

import java.io.StringReader;

import com.reader.common.AbstractTextSource;
import com.reader.common.ColorConstants;
import com.reader.common.Database;
import com.reader.common.TextProcessor;
import com.reader.common.TextWithProperties;
import com.reader.common.persist.WordAttributes;

public class SimpleTextSource extends AbstractTextSource {

	private String text;

	private Database database;

	public SimpleTextSource(String text, Database database) {
		this.text = text;
		this.database = database;
	}

	@Override
	public void process(TextProcessor textProcessor) {
		SimpleTextParser parser = new SimpleTextParser() {
			@Override
			public void fillAttributes(TextWithProperties textWithProperties) {
				WordAttributes attrs = database.get(textWithProperties
						.getText());
				if (attrs == null)
					textWithProperties.setColor(ColorConstants.DEFAULT);
				else
					textWithProperties.setColor(attrs.getColor());

			}
		};
		parser.parser(new StringReader(text), textProcessor);
	}

}
