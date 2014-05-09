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

	public void markWord(String text);

	public void markColor(String[] words, String color);

	public List<Word> getKnownWords(String color);

}
