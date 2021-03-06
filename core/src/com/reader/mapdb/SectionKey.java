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
package com.reader.mapdb;

import java.io.Serializable;

public class SectionKey implements Serializable, Comparable<SectionKey> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4458174236665199691L;

	private final int index;

	private final int fontSize;

	private final String fileName;

	private final boolean landscape;

	private boolean splited;

	private final int width;

	private final int height;

	private final String fontName;

	public SectionKey(String fileName, int index, boolean landscape,
			boolean splited, String fontName, int fontSize, int width,
			int height) {
		this.index = index;
		this.fileName = fileName;
		this.landscape = landscape;
		this.splited = splited;
		this.fontSize = fontSize;
		this.width = width;
		this.height = height;
		this.fontName = fontName;
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

	public int getFontSize() {
		return fontSize;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getFontName() {
		return fontName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result
				+ ((fontName == null) ? 0 : fontName.hashCode());
		result = prime * result + fontSize;
		result = prime * result + height;
		result = prime * result + index;
		result = prime * result + (landscape ? 1231 : 1237);
		result = prime * result + (splited ? 1231 : 1237);
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SectionKey other = (SectionKey) obj;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (fontName == null) {
			if (other.fontName != null)
				return false;
		} else if (!fontName.equals(other.fontName))
			return false;

		if (fontSize != other.fontSize)
			return false;
		if (height != other.height)
			return false;
		if (index != other.index)
			return false;
		if (landscape != other.landscape)
			return false;
		if (splited != other.splited)
			return false;
		if (width != other.width)
			return false;
		return true;
	}

	@Override
	public int compareTo(SectionKey o) {
		int i;

		i = fileName.compareTo(o.fileName);
		if (i != 0)
			return i;

		i = fontName.compareTo(o.fontName);
		if (i != 0)
			return i;

		if (fontSize < o.fontSize)
			return -1;
		if (fontSize > o.fontSize)
			return -1;

		if (height < o.height)
			return -1;
		if (height > o.height)
			return -1;

		if (index < o.index)
			return -1;
		if (index > o.index)
			return -1;

		if (landscape) {
			if (!o.landscape)
				return -1;
		} else if (o.landscape)
			return 1;

		if (splited) {
			if (!o.splited)
				return -1;
		} else if (o.splited)
			return 1;

		if (width < o.width)
			return -1;
		if (width > o.width)
			return -1;

		return 0;

	}

}
