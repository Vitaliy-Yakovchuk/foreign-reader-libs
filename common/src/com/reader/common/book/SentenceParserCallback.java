package com.reader.common.book;

public interface SentenceParserCallback {

	/**
	 * If return <code>false</code> stop scanning
	 * @param sentence
	 * @return
	 */
	
	public boolean found(Sentence sentence);

}
