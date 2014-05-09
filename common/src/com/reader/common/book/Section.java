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
	
	private int ignoreParagraphCount;

	public List<String> getParagraphs() {
		return paragraphs;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIgnoreParagraphCount() {
		return ignoreParagraphCount;
	}

	public void setIgnoreParagraphCount(int ignoreParagraphCount) {
		this.ignoreParagraphCount = ignoreParagraphCount;
	}
}
