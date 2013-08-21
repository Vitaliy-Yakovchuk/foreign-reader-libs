package com.reader.mapdb;

import java.io.Serializable;

public class SectionKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4458174236665199691L;

	private final int index;

	private final String fileName;

	private final boolean landscape;

	private boolean splited;

	public SectionKey(String fileName, int index, boolean landscape,
			boolean splited) {
		this.index = index;
		this.fileName = fileName;
		this.landscape = landscape;
		this.splited = splited;
	}

	public String getFileName() {
		return fileName;
	}

	public int getIndex() {
		return index;
	}

	public boolean isLandscape() {
		return landscape;
	}

	public boolean isSplited() {
		return splited;
	}

}
