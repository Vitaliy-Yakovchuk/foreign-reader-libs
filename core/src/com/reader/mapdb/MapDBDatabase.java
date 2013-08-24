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
	private ConcurrentNavigableMap<String, byte[]> wordsMap;

	private ConcurrentNavigableMap<String, Date> wordsDated;

	private ConcurrentNavigableMap<String, Date> wordsKnowDated;
	private WordDBImpl wordDB;

	public MapDBDatabase(File dbFile) {
		this.file = dbFile;

		wordDB = new WordDBImpl(new File(dbFile.getParentFile(), "words"));

		db = DBMaker.newFileDB(dbFile).make();
		wordsMap = db.getTreeMap("words");
		wordsDated = db.getTreeMap("dates");
		wordsKnowDated = db.getTreeMap("know_dates");
	}

	@Override
	public void putA(String word, WordAttributes wordAttributes) {
		wordsMap.put(word, bytes(wordAttributes));
		wordsDated.put(word, new Date());
		if (wordAttributes.getColor().equals(ColorConstants.WHITE)) {
			WordAttributes wa = getA(word);
			if (wa != null && wa.getColor().equals(ColorConstants.BLUE))
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
		file.delete();
	}

	@Override
	public void commit() {
		db.commit();
	}

	@Override
	public void rollback() {
		db.rollback();
	}

	@Override
	public List<Word> loadWords(String color) {
		List<Word> l = new LinkedList<Word>();
		WordAttributes attr = new WordAttributes();
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
				l.add(word);
			}
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

}
