package com.reader.common;

import com.reader.common.impl.MemoryDatabase;
import com.reader.common.impl.SimpleTextSource;

public class ObjectsFactory {

	private static MemoryDatabase memoryDatabase = new MemoryDatabase();

	public static TextSource createSimpleSource(String text) {
		SimpleTextSource textSource = new SimpleTextSource(text,
				getDefaultDatabase());
		return textSource;
	}

	public static Database getDefaultDatabase() {
		return memoryDatabase;
	}

	public static void clear() {
		memoryDatabase = new MemoryDatabase();
	}

}
