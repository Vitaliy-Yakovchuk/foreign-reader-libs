package com.reader.common.pages;

import com.reader.common.TextWidth;

public interface Section {

	void setSection(com.reader.common.book.Section section);

	void setCurrentPage(int currentPage);

	int getCurrentPage();

	Page getPage();

	int getPageCount();

	void splitOnPages(TextWidth textWidth, int width, int maxLineCount);

}
