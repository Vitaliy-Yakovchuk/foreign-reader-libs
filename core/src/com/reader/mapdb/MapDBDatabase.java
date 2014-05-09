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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentNavigableMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import com.reader.common.AbstractDatabase;
import com.reader.common.ColorConstants;
import com.reader.common.Word;
import com.reader.common.book.Sentence;
import com.reader.common.impl.SimpleTextParser;
import com.reader.common.persist.WordAttributes;

public class MapDBDatabase extends AbstractDatabase {

	private final File file;
	private DB db;
	private DB dbStat;
	private ConcurrentNavigableMap<String, byte[]> wordsMap;

	private ConcurrentNavigableMap<String, Date> wordsDated;

	private ConcurrentNavigableMap<String, Date> wordsKnowDated;

	private ConcurrentNavigableMap<String, Integer> wordsStat;

	private WordDBImpl wordDB;

	private boolean dbStatNeedCommit;

	public MapDBDatabase(File dbFile) {
		this.file = dbFile;

		File dir = dbFile.getParentFile();

		dir.mkdirs();

		wordDB = new WordDBImpl(new File(dir, "words"), this);

		db = DBMaker.newFileDB(dbFile).make();
		dbStat = DBMaker.newFileDB(new File(dir, "wordsstat")).make();
		wordsMap = db.getTreeMap("words");
		wordsDated = db.getTreeMap("dates");
		wordsKnowDated = db.getTreeMap("know_dates");
		wordsStat = dbStat.getTreeMap("wordsstat");
	}

	@Override
	public void putA(String word, WordAttributes wordAttributes) {
		wordsMap.put(word, bytes(wordAttributes));
		wordsDated.put(word, new Date());
		if (wordAttributes.getColor().equals(ColorConstants.WHITE)) {
			WordAttributes wa = getA(word);
			if (wa != null && wa.getColor().equals(ColorConstants.YELLOW))
				wordsKnowDated.put(word, new Date());
		}
	}

	@Override
	public WordAttributes getA(String word) {
		return fromBytes(wordsMap.get(word));
	}

	@Override
	public void removeA(String word) {
		db.delete(word);
	}

	public void closeAndRemove() {
		db.close();
		dbStat.close();
		file.delete();
	}

	@Override
	public void commit() {
		db.commit();
		if (dbStatNeedCommit) {
			dbStat.commit();
			dbStatNeedCommit = false;
		}
	}

	@Override
	public void rollback() {
		db.rollback();
	}

	@Override
	public List<Word> loadWords(String color) {
		List<Word> l = new LinkedList<Word>();
		WordAttributes attr = new WordAttributes();
		dbStatNeedCommit = false;
		attr.setColor(color);
		byte[] bs = bytes(attr);
		for (Entry<String, byte[]> entry : wordsMap.entrySet()) {
			byte[] bs2 = entry.getValue();
			bs[bs.length - 1] = bs2[bs.length - 1];
			bs[bs.length - 2] = bs2[bs.length - 2];
			if (Arrays.equals(bs, bs2)) {
				Word word = new Word();
				String text = entry.getKey();
				word.setText(text);
				word.setColor(color);
				word.setDate(wordsDated.get(text));
				word.setInSentenceCount(getInSentenceCount(text));
				l.add(word);
			}
		}
		if (dbStatNeedCommit) {
			dbStatNeedCommit = false;
			dbStat.commit();
		}
		return l;
	}

	@Override
	public Word toWord(String word) {
		Word word2 = new Word();
		WordAttributes attributes = get(word);
		word2.setColor(attributes.getColor());
		word2.setText(word);
		word2.setDate(wordsDated.get(word));
		return word2;
	}

	public DB getDB() {
		return db;
	}

	@Override
	public List<Sentence> getSentences(String word) {
		try {
			return wordDB.getSentences(word);
		} catch (IOException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Override
	public void addSentence(String text, String bookId, int section,
			int position) {

		final Sentence sentence = new Sentence();
		sentence.text = text;
		sentence.section = section;
		sentence.bookId = bookId;

		SimpleTextParser parser = new SimpleTextParser() {

			@Override
			public void processWord(char[] text, int start, int length) {
				String word = new String(text, start, length).toLowerCase();

				WordAttributes wa = getA(word);
				if (wa != null && wa.getColor().equals(ColorConstants.YELLOW))
					try {
						wordDB.addSentence(word, sentence);
					} catch (IOException e) {
						e.printStackTrace();
					}
			}

		};

		parser.parse(text.toCharArray());
	}

	@Override
	public int[] getWordsCount() {
		int[] res = new int[2];
		WordAttributes attr = new WordAttributes();
		attr.setColor(ColorConstants.WHITE);
		byte[] wt = bytes(attr);
		attr.setColor(ColorConstants.YELLOW);
		byte[] yl = bytes(attr);
		for (Entry<String, byte[]> entry : wordsMap.entrySet()) {
			byte[] bs2 = entry.getValue();
			wt[wt.length - 1] = bs2[wt.length - 1];
			wt[wt.length - 2] = bs2[wt.length - 2];
			if (Arrays.equals(wt, bs2)) {
				res[0]++;
			} else {
				yl[wt.length - 1] = bs2[wt.length - 1];
				yl[wt.length - 2] = bs2[wt.length - 2];
				if (Arrays.equals(yl, bs2))
					res[1]++;
			}
		}
		return res;
	}

	public void setInSentenceCount(String word, int count) {
		Integer wc = wordsStat.get(word);
		if (wc == null || wc.intValue() != count) {
			wordsStat.put(word, count);
			dbStatNeedCommit = true;
		}
	}

	private int getInSentenceCount(String word) {
		Integer i = wordsStat.get(word);
		if (i != null)
			return i;
		try {
			i = 0;
			i = wordDB.getInSentenceCount(word);
		} catch (IOException e) {
			e.printStackTrace();
		}
		wordsStat.put(word, i);
		dbStatNeedCommit = true;
		return i;
	}

}
