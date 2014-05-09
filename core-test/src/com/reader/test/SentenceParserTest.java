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
package com.reader.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.reader.common.book.Sentence;
import com.reader.common.book.SentenceParserCallback;
import com.reader.common.book.fb2.FictionBook;

import static org.junit.Assert.*;

public class SentenceParserTest {

	private List<String> sentences = new ArrayList<String>();

	private List<Integer> sentencesSection = new ArrayList<Integer>();

	private FictionBook book;

	@Before
	public void init() throws IOException {
		File tmpFile = File.createTempFile("test-s", ".fb2");
		tmpFile.deleteOnExit();

		InputStream is = getClass().getResourceAsStream(
				"/com/reader/test/test-book.fb2");

		OutputStream os = new FileOutputStream(tmpFile);

		byte[] buff = new byte[1024 * 20];
		int r;
		while ((r = is.read(buff)) > 0) {
			os.write(buff, 0, r);
		}

		is.close();
		os.close();

		book = new FictionBook(tmpFile);

		sentences
				.add("Text of section 1, this is not \"very\" long text of section 1.");
		sentences.add("And this is small text of section 1.");

		sentencesSection.add(0);
		sentencesSection.add(0);

		sentences.add("Text of section 2.");
		sentences.add("This is not very long text of section 2.");
		sentences.add("Small sentence of section 2.");
		sentences.add("section 2 end");

		sentencesSection.add(1);
		sentencesSection.add(1);
		sentencesSection.add(1);
		sentencesSection.add(1);

	}

	@Test
	public void testFindSentences() throws Exception {
		book.scanForSentences(new SentenceParserCallback() {

			@Override
			public boolean found(Sentence sentence) {
				assertEquals((int) sentencesSection.remove(0), sentence.section);
				assertEquals(sentences.remove(0), sentence.text);
				return true;
			}
		});
		assertEquals(0, sentences.size());
	}

}
