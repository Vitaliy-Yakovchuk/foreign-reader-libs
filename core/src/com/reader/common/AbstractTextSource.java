package com.reader.common;

public abstract class AbstractTextSource implements TextSource {
@Override
	public void markColor(String text, String color) {
		TextWithProperties textWithProperties = new TextWithProperties();
		textWithProperties.setText(text);
		textWithProperties.setColor(color);
		update(textWithProperties);
	}
}
