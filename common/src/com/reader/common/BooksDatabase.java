package com.reader.common;

import java.util.List;

public interface BooksDatabase {

	public List<BookMetadata> getBooks();

	public void setBook(BookMetadata bookMetadata);

	public void removeBook(BookMetadata bookMetadata);

	public BookMetadata getBook(String fileName);
}
