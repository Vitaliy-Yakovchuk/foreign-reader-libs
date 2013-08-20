package com.reader.common;

import java.util.List;

/**
 * 
 * @author vitaliy.yakovchuk
 *
 */

public class TextWithProperties {

	private String text;
	
	private String color;
	
	private List<String> words;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public List<String> getWords() {
		return words;
	}

	public void setWords(List<String> words) {
		this.words = words;
	}

}
