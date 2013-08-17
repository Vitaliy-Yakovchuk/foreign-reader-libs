package com.reader.common;

import java.io.File;

import com.reader.common.impl.SimpleTextSource;
import com.reader.mapdb.MapDBDatabase;

public class ObjectsFactory {

	private static MapDBDatabase database;

	public static File storageFile;

	public static TextSource createSimpleSource(String text) {
		SimpleTextSource textSource = new SimpleTextSource(text,
				getDefaultDatabase());
		return textSource;
	}

	public static Database getDefaultDatabase() {
		if (database == null) {
			database = new MapDBDatabase(storageFile);
		}
		return database;
	}

	public static void clear() {
		if (database == null)
			return;
		database.closeAndRemove();
		database = new MapDBDatabase(storageFile);
	}

}
