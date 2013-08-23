package com.reader.common.book;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Section implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5252307176904384329L;

	private List<String> paragraphs = new LinkedList<String>();

	private String title;

	public List<String> getParagraphs() {
		return paragraphs;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
