package com.reader.common.pages;

import java.io.Serializable;

public class Page implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7445349484170945159L;

	public Page(char[] text, int maxLineCount) {
		this.text = text;
		startLines = new int[maxLineCount];
		lengthLines = new int[maxLineCount];
		end = new boolean[maxLineCount];
	}

	public transient char[] text;

	public final int[] startLines;

	public final int[] lengthLines;

	public final boolean[] end;

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

}
