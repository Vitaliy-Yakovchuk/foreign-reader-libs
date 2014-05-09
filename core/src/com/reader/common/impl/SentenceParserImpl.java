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

import static com.reader.common.book.SentenceParser.MAX_SENTENCE_LENGTH;

import com.reader.common.book.Sentence;
import com.reader.common.book.SentenceParserCallback;

public abstract class SentenceParserImpl implements SentenceParserCallback {

	private StringBuilder builder = new StringBuilder();

	private boolean waitForSentenceEnd;

	private int section;

	private String bookId;

	private boolean isSentence;

	public void addSenteceText(String text) {
		int length = text.length();
		int pos = 0;
		if (waitForSentenceEnd) {
			while (pos < length && !isSentenceEnd(text.charAt(pos)))
				pos++;
			if (pos >= length)
				return;
			waitForSentenceEnd = false;
			pos++;
		}

		boolean isSpase = false;

		while (pos < length) {
			char c = text.charAt(pos);
			if (Character.isWhitespace(c)) {
				if (!isSentence || isSpase){
					pos++;
					continue;
				}
				isSpase = true;
				builder.append(' ');
			} else {
				isSentence = true;
				isSpase = false;
				builder.append(c);
				if (isSentenceEnd(c)) {
					found();
					isSentence = false;
				}
			}

			if (builder.length() >= MAX_SENTENCE_LENGTH) {
				builder.setLength(0);
				waitForSentenceEnd = true;
				while (pos < length && !isSentenceEnd(text.charAt(pos)))
					pos++;
				if (pos >= length)
					return;
				waitForSentenceEnd = false;
				isSentence = false;
			}
			pos++;
		}
	}

	private boolean isSentenceEnd(char c) {
		return c == '.' || c == '?' || c == '!';
	}

	public void setSection(int section) {
		if (this.section != section) {
			found();
		}
		this.section = section;
	}

	private void found() {
		if (builder == null || builder.length() == 0)
			return;
		Sentence sentence = new Sentence();
		sentence.section = section;
		sentence.bookId = bookId;
		sentence.text = builder.toString();
		builder.setLength(0);
		found(sentence);
		waitForSentenceEnd = false;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
}
