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
package com.reader.common.book.txt;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.mozilla.universalchardet.UniversalDetector;

import com.reader.common.book.AbstractBook;
import com.reader.common.book.Section;
import com.reader.common.book.SectionMetadata;
import com.reader.common.book.Sentence;
import com.reader.common.book.SentenceParserCallback;
import com.reader.common.impl.SentenceParserImpl;

public class TxtBook extends AbstractBook {

	private File file;

	public TxtBook(File file) {
		this.file = file;
	}

	@Override
	public List<SectionMetadata> getSections() throws Exception {
		SectionMetadata metadata = new SectionMetadata();
		metadata.setTitle(file.getName());
		return Arrays.asList(metadata);
	}

	@Override
	public Section getSection(int i) throws Exception {
		byte[] buf = new byte[4096];
		java.io.FileInputStream fis = new java.io.FileInputStream(file);

		UniversalDetector detector = new UniversalDetector(null);
		int nread;
		while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
			detector.handleData(buf, 0, nread);
		}
		detector.dataEnd();
		String encoding = detector.getDetectedCharset();

		detector.reset();

		fis.close();
		fis = new java.io.FileInputStream(file);

		InputStreamReader reader;

		if (encoding != null) {
			reader = new InputStreamReader(fis, encoding);
		} else
			reader = new InputStreamReader(fis);

		int ch;

		Section section = new Section();

		section.getParagraphs().add(file.getName());

		section.setTitle(file.getName());

		StringBuilder sb = new StringBuilder();

		while ((ch = reader.read()) > 0) {
			if (ch == (int) '\n') {
				section.getParagraphs().add(sb.toString());
				sb.setLength(0);
			} else
				sb.append((char) ch);
		}
		if (sb.length() > 0)
			section.getParagraphs().add(sb.toString());

		fis.close();

		return section;
	}

	@Override
	public void scanForSentences(final SentenceParserCallback parser)
			throws Exception {
		SentenceParserImpl impl = new SentenceParserImpl() {

			@Override
			public boolean found(Sentence sentence) {
				return parser.found(sentence);
			}
		};

		byte[] buf = new byte[4096];
		java.io.FileInputStream fis = new java.io.FileInputStream(file);

		UniversalDetector detector = new UniversalDetector(null);
		int nread;
		while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
			detector.handleData(buf, 0, nread);
		}
		detector.dataEnd();
		String encoding = detector.getDetectedCharset();

		detector.reset();

		fis.close();
		fis = new java.io.FileInputStream(file);

		InputStreamReader reader;

		if (encoding != null) {
			reader = new InputStreamReader(fis, encoding);
		} else
			reader = new InputStreamReader(fis);

		int ch;

		StringBuilder sb = new StringBuilder();

		while ((ch = reader.read()) > 0) {

			sb.append((char) ch);
		}
		if (sb.length() > 0)
			impl.addSenteceText(sb.toString());

		fis.close();

	}

}
