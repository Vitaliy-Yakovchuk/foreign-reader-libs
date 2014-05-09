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
package com.reader.common.pages;

import java.io.Serializable;
import java.util.Arrays;

public class Page implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7445349484170945159L;

	private int maxLine = 0;

	private int ignoreLineCount;

	public Page(char[] text, int maxLineCount) {
		this.text = text;
		if (maxLineCount == Integer.MAX_VALUE) {
			startLines = new int[20];
			lengthLines = new int[20];
			end = new boolean[20];
		} else {
			startLines = new int[maxLineCount];
			lengthLines = new int[maxLineCount];
			end = new boolean[maxLineCount];
		}
	}

	public transient char[] text;

	public int[] startLines;

	public int[] lengthLines;

	public boolean[] end;

	public String getText() {
		int s = startLines[0];
		int l = 0;
		for (int i = 0; i < lengthLines.length; i++) {
			int j = startLines[i] + lengthLines[i] - s;
			if (l < j)
				l = j;
		}
		return new String(text, s, l);
	}

	public int getMaxLineCount() {
		return startLines.length;
	}

	public void updateMaxLine(int line) {
		if (line >= startLines.length) {
			startLines = Arrays.copyOf(startLines, line + 20);
			lengthLines = Arrays.copyOf(lengthLines, line + 20);
			end = Arrays.copyOf(end, line + 20);
		}
		maxLine = line;
	}

	public int getMaxLine() {
		return maxLine;
	}

	public int getIgnoreLineCount() {
		return ignoreLineCount;
	}

	public void setIgnoreLineCount(int ignoreLineCount) {
		this.ignoreLineCount = ignoreLineCount;
	}

}
