package com.reader.common;

/**
 * 
 * @author vitaliy.yakovchuk
 * 
 */

public interface TextSource {

	/**
	 * Return text with text processor
	 * 
	 * @param textProcessor
	 */

	public void process(TextProcessor textProcessor);

	/**
	 * Text in property can be upper case and even with whitespace and/or phrase
	 * 
	 * @param text
	 * @param color
	 */

	public void update(TextWithProperties properties);

	/**
	 * Text can be upper case and even with whitespace and/or phrase
	 * 
	 * @param text
	 * @param color
	 */

	public void markColor(String text, String color);
	
	public void markColor(String[] words, String black);

}