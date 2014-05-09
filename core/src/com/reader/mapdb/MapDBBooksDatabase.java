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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;

import org.mapdb.DB;

import com.reader.common.AbstractBooksDatabase;
import com.reader.common.BookMetadata;

public class MapDBBooksDatabase extends AbstractBooksDatabase {

	private ConcurrentNavigableMap<String, BookMetadata> books;

	private DB db;


	public MapDBBooksDatabase(DB db) {
		this.db = db;
		books = db.getTreeMap("books");
	}

	@Override
	public List<BookMetadata> getBooks() {
		List<BookMetadata> l = new ArrayList<BookMetadata>();
		l.addAll(books.values());

		Collections.sort(l);

		return l;
	}

	@Override
	public void setBook(BookMetadata bookMetadata) {
		books.put(bookMetadata.getFileName(), bookMetadata);
		db.commit();
	}

	@Override
	public void removeBook(BookMetadata bookMetadata) {
		books.remove(bookMetadata.getFileName());
		db.commit();
	}

	@Override
	public BookMetadata getBook(String fileName) {
		return books.get(fileName);
	}
	
}
