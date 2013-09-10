package com.reader.common.book;

public interface SentenceParser {

	public static final int MAX_SENTENCE_LENGTH = 3000;

	public void scanForSentences(SentenceParserCallback parser) throws Exception;

}
