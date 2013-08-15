package com.reader.common;

/**
 * 
 * @author vitaliy.yakovchuk
 * 
 */

public interface TextProcessor {

	public void got(TextWithProperties textProperties);
	
	public void updated(TextWithProperties textProperties);
	
	/**
	 * End of document (page, etc)
	 */
	public void end();
}
