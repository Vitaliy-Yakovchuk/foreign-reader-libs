package com.reader.common;

import java.util.List;

import com.reader.common.book.Sentence;
import com.reader.common.persist.WordAttributes;

/**
 * Inner very fast key-point database
 * 
 * @author vitaliy.yakovchuk
 * 
 */

public interface Database {

	public static final char SPACE = ' ';

	public void put(String word, WordAttributes wordAttributes);

	public WordAttributes get(String word);

	public void remove(String word);

	public void put(String[] frase, WordAttributes wordAttributes);

	public WordAttributes get(String[] frase);

	public void remove(String[] frase);

	public void updateWords(String[] words, WordAttributes attributes);

	public List<Word> loadWords(String color);

	public Word toWord(String word);

	public void addSentence(String text, String bookId, int section, int position);

	public List<Sentence> getSentences(String word);

	/**
	 * 0 - known words,
	 * 1 - unknown words
	 * @return
	 */
	public int[] getWordsCount();
}
