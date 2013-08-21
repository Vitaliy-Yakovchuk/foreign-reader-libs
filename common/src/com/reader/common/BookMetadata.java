package com.reader.common;

import java.io.Serializable;
import java.util.Date;

public class BookMetadata implements Serializable, Comparable<BookMetadata> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5110961974956474613L;

	private String fileName;

	private String name;

	private float ready;

	private int fontSize;

	private String fontName;

	private Date lastOpen;

	private int lastPosition;

	private int lastSection;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Ready percent
	 * 
	 * @return
	 */
	public float getReady() {
		return ready;
	}

	public void setReady(float ready) {
		this.ready = ready;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public Date getLastOpen() {
		return lastOpen;
	}

	public void setLastOpen(Date lastOpen) {
		this.lastOpen = lastOpen;
	}

	@Override
	public int compareTo(BookMetadata o) {
		return o.lastOpen.compareTo(lastOpen);
	}

	@Override
	public String toString() {
		return name;
	}

	public int getLastPosition() {
		return lastPosition;
	}

	public void setLastPosition(int lastPosition) {
		this.lastPosition = lastPosition;
	}

	public int getLastSection() {
		return lastSection;
	}

	public void setLastSection(int lastSection) {
		this.lastSection = lastSection;
	}
}
