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
		int sectionIndex = 0;
		while (true) {
			boolean b = true;
			SectionKey key = new SectionKey(bookMetadata.getFileName(),
					sectionIndex, true, true);
			if (books.remove(key) != null)
				b = false;

			key = new SectionKey(bookMetadata.getFileName(), sectionIndex,
					true, false);
			if (books.remove(key) != null)
				b = false;

			key = new SectionKey(bookMetadata.getFileName(), sectionIndex,
					false, true);
			if (books.remove(key) != null)
				b = false;

			key = new SectionKey(bookMetadata.getFileName(), sectionIndex,
					false, false);
			if (books.remove(key) != null)
				b = false;

			if (b)
				break;
			sectionIndex++;
		}
		db.commit();
	}

	@Override
	public BookMetadata getBook(String fileName) {
		return books.get(fileName);
	}

}
