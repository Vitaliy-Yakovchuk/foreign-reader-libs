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
package com.reader.common.impl;

import com.reader.common.AbstractTextParser;

public abstract class SimpleTextWithSymbolsParser extends AbstractTextParser {

	@Override
	public void parse(char[] text) {
		int start = 0;
		int length = 0;
		char character;
		int l = text.length;
		int i = 0;
		boolean f = true;
		while (i < l) {
			character = text[i];
			if (Character.isSpaceChar(character) || length > MAX_WORD_LENGTH) {
				if (length > 0) {
					processWord(text, start, length);
					length = 0;
					f = true;
				}
			} else {
				if (f) {
					start = i;
					f = false;
				}
				length++;
			}
			i++;
		}

		if (length > 0) {
			processWord(text, start, length);
		}
	}

	public abstract void processWord(char[] text, int start, int length);
}
