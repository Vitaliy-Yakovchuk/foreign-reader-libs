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
package com.reader.common.pages;

import com.reader.common.TextWidth;

public interface Section {

	void setSection(com.reader.common.book.Section section);

	void setCurrentPage(int currentPage);

	int getCurrentPage();

	Page getPage();

	int getPageCount();

	void splitOnPages(TextWidth textWidth, int width, int maxLineCount);

	int getCurrentCharacter();

	void setCurrentPageByCharacteNumber(int i);
	
	com.reader.common.book.Section getSection();

}
