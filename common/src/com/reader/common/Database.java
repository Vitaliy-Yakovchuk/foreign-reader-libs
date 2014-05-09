/*******************************************************************************
 * Copyright 2013 Vitaliy Yakovchuk
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
