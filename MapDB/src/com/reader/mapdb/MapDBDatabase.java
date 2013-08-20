package com.reader.mapdb;

import java.io.File;
import java.util.Date;
import java.util.concurrent.ConcurrentNavigableMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import com.reader.common.AbstractDatabase;
import com.reader.common.persist.WordAttributes;

public class MapDBDatabase extends AbstractDatabase {

	private final File file;
	private DB db;
	private ConcurrentNavigableMap<String, byte[]> wordsMap;

	private ConcurrentNavigableMap<String, Date> wordsDated;

	public MapDBDatabase(File dbFile) {
		this.file = dbFile;
		db = DBMaker.newFileDB(dbFile).make();
		wordsMap = db.getTreeMap("words");
		wordsDated = db.getTreeMap("dates");
	}

	@Override
	public void putA(String word, WordAttributes wordAttributes) {
		wordsMap.put(word, bytes(wordAttributes));
		wordsDated.put(word, new Date());
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

}
