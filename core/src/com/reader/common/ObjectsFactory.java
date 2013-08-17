package com.reader.common;

import java.io.File;

import com.reader.common.impl.SimpleTextSource;
import com.reader.mapdb.MapDBDatabase;

public class ObjectsFactory {

	private static MapDBDatabase database;

	static {
		database = new MapDBDatabase(new File("/home/zdd/tmp/1"));
	}

	public static TextSource createSimpleSource(String text) {
		SimpleTextSource textSource = new SimpleTextSource(text,
				getDefaultDatabase());
		return textSource;
	}

	public static Database getDefaultDatabase() {
		clear();
		return database;
	}

	public static void clear() {
		database.closeAndRemove();
		database = new MapDBDatabase(new File("/home/zdd/tmp/1"));
	}

}
