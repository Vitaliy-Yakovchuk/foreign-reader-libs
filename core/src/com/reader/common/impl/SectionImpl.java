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
package com.reader.common.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.reader.common.TextWidth;
import com.reader.common.book.Section;
import com.reader.common.impl.SimpleTextWithSymbolsParser;
import com.reader.common.pages.AbstractSection;
import com.reader.common.pages.Page;

public class SectionImpl extends AbstractSection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5843547669613571263L;

	private final char[] t;

	private List<Page> pages = new ArrayList<Page>();

	private int currentPage;

	private com.reader.common.book.Section section;

	public SectionImpl(com.reader.common.book.Section section) {
		this.section = section;
		StringBuilder builder = new StringBuilder();
		for (String s : section.getParagraphs()) {
			builder.append(s);
			builder.append(' ');
		}
		t = new char[builder.length()];
		builder.getChars(0, t.length, t, 0);
	}

	public SectionImpl(com.reader.common.book.Section section, char[] text) {
		this.section = section;
		t = text;
	}

	@Override
	public void splitOnPages(final TextWidth textWidth, final int width,
			final int maxLineCount) {
		pages.clear();

		final int spaceWidth = textWidth.getWidth(new char[] { 't' }, 0, 1);

		pages.add(new Page(t, maxLineCount));

		final Iterator<String> paragraphs = section.getParagraphs().iterator();

		final int[] ignoreParagraphCount = new int[] { section
				.getIgnoreParagraphCount() };

		class SimpleTextWithSymbolsParserA extends SimpleTextWithSymbolsParser {
			private int line;

			private int lineWidth;

			private Page page;

			{
				page = pages.get(0);
			}

			private int st;

			private int len;

			private String p;

			private int pIndex;

			@Override
			public void processWord(char[] txt, int start, int length) {
				int w = textWidth.getWidth(txt, start, length);
				if (lineWidth > 0)
					lineWidth += spaceWidth;

				lineWidth += w;
				if (paragraphChanged(txt, start, length)) {
					add(true);
					lineWidth = w;
					st = start;
					len = 0;
					line++;
					if (line + 1 < maxLineCount)
						line++;
				} else if (lineWidth > width) {
					add(false);
					lineWidth = w;
					st = start;
					len = 0;
					line++;
				}
				len = start - st + length;
			}

			private boolean paragraphChanged(char[] txt, int start, int length) {
				if (p == null) {
					p = paragraphs.next();
					ignoreParagraphCount[0]--;
				}

				boolean res = false;

				while (true) {
					if (pIndex >= p.length()) {
						res = true;
						p = paragraphs.next();
						ignoreParagraphCount[0]--;
						if (p.length() == 0)
							break;
						pIndex = 0;
						while (p.charAt(pIndex) != txt[start]) {
							pIndex++;
						}
						break;
					}
					if (p.charAt(pIndex) == txt[start])
						break;
					pIndex++;
				}
				pIndex += length;
				return res;
			}

			private void add(boolean end) {
				if (line >= maxLineCount) {
					page = new Page(t, maxLineCount);
					pages.add(page);
					line = 0;
				}

				page.updateMaxLine(line);

				if (ignoreParagraphCount[0] >= 0)
					page.setIgnoreLineCount(line + 1);

				page.startLines[line] = st;
				page.lengthLines[line] = len;
				page.end[line] = end;

			}
		}

		SimpleTextWithSymbolsParserA parser = new SimpleTextWithSymbolsParserA();

		parser.parse(t);

		parser.add(true);
	}

	@Override
	public int getPageCount() {
		return pages.size();
	}

	@Override
	public Page getPage() {
		return pages.get(currentPage);
	}

	@Override
	public int getCurrentPage() {
		return currentPage;
	}

	@Override
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	@Override
	public void setSection(com.reader.common.book.Section section) {
		this.section = section;
	}

	@Override
	public int getCurrentCharacter() {
		return getPage().startLines[0];
	}

	@Override
	public void setCurrentPageByCharacteNumber(int i) {
		for (int j = 1; j < pages.size(); j++) {
			if (pages.get(j).startLines[0] > i) {
				currentPage = j - 1;
				break;
			}
		}
	}

	public List<Page> getPages() {
		return pages;
	}

	public char[] getT() {
		return t;
	}

	@Override
	public Section getSection() {
		return section;
	}
}
