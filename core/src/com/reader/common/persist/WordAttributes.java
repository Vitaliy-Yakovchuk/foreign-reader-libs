package com.reader.common.persist;

import java.io.Serializable;

public class WordAttributes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8649014296761073022L;

	private String color;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
