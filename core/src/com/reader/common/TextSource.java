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

	public Dictionary getDictionary();

}
