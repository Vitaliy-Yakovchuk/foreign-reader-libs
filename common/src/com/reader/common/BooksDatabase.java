package com.reader.common;

import java.util.List;

import com.reader.common.pages.Page;

public interface BooksDatabase {

	public List<BookMetadata> getBooks();

	public void setBook(BookMetadata bookMetadata);

	public void removeBook(BookMetadata bookMetadata);

	public void storeSection(String fileName, int sectionIndex,
			boolean landscape, boolean splited, Page[] section);

	public Page[] loadSection(String fileName, int sectionIndex,
			boolean landscape, boolean splited);

}
