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
		return getText();
	}

	public int getInSentenceCount() {
		return inSentenceCount;
	}

	public void setInSentenceCount(int inSentenceCount) {
		this.inSentenceCount = inSentenceCount;
	}

}
