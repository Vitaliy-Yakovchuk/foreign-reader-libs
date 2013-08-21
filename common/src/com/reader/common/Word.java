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
		return o.date.compareTo(date);
	}

	@Override
	public String toString() {
		return getText();
	}

}
