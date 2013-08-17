package com.reader.mapdb;

import java.io.File;
import java.util.concurrent.ConcurrentNavigableMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import com.reader.common.AbstractDatabase;
import com.reader.common.persist.WordAttributes;

public class MapDBDatabase extends AbstractDatabase {

	private final File file;
	private DB db;
	private ConcurrentNavigableMap<String, byte[]> wordsMap;

	public MapDBDatabase(File dbFile) {
		this.file = dbFile;
		db = DBMaker.newFileDB(dbFile).make();
		wordsMap = db.getTreeMap("words");
	}

	@Override
	public void putA(String word, WordAttributes wordAttributes) {
		wordsMap.put(word, bytes(wordAttributes));
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
