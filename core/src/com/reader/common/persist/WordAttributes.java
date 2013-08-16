package com.reader.common.persist;

import java.io.Serializable;

import com.reader.common.ColorConstants;

public class WordAttributes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8649014296761073022L;

	private String color = ColorConstants.DEFAULT;

	private boolean startsWithPhrase;
	
	private boolean transport;
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public boolean isStartsWithPhrase() {
		return startsWithPhrase;
	}

	public void setStartsWithPhrase(boolean startsWithPhrase) {
		this.startsWithPhrase = startsWithPhrase;
	}

	public boolean isTransport() {
		return transport;
	}

	public void setTransport(boolean transport) {
		this.transport = transport;
	}

}
