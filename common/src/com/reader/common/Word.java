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

import java.io.Serializable;
import java.util.Date;

public class Word implements Serializable, Comparable<Word> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5891395738530189390L;

	private String text;

	private String color;

	private Date date;

	private int inSentenceCount;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int compareTo(Word o) {
		if (inSentenceCount < o.inSentenceCount)
			return 1;
		if (inSentenceCount > o.inSentenceCount)
			return -1;
		return 0;
	}

	@Override
	public String toString() {
		return getText() + " (" + getInSentenceCount() + ")";
	}

	public int getInSentenceCount() {
		return inSentenceCount;
	}

	public void setInSentenceCount(int inSentenceCount) {
		this.inSentenceCount = inSentenceCount;
	}

}
