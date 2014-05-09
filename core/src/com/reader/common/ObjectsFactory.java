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
package com.reader.common;

import java.io.File;

import com.reader.common.impl.SimpleTextSource;
import com.reader.mapdb.MapDBBooksDatabase;
import com.reader.mapdb.MapDBDatabase;

public class ObjectsFactory {

	private static MapDBDatabase database;

	private static MapDBBooksDatabase booksDatabase;

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

	public static BooksDatabase getDefaultBooksDatabase() {
		if (booksDatabase == null) {
			getDefaultDatabase();
			booksDatabase = new MapDBBooksDatabase(database.getDB());
		}
		return booksDatabase;
	}

}
