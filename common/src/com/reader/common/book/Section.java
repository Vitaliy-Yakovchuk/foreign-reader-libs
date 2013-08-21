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

	public List<String> getParagraphs() {
		return paragraphs;
	}
}
