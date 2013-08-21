package com.reader.common.book;

import java.io.File;

import com.reader.common.book.Book;
import com.reader.common.book.fb2.FictionBook;
import com.reader.common.book.txt.TxtBook;

public class BookLoader {

	/**
	 * Return null if application cannot load this file type
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 *             if error occur (for example if file contains wrong data).
	 */

	public static Book loadBook(File file) throws Exception {
		if (file.getName().toLowerCase().endsWith(".fb2"))
			return new FictionBook(file);
		if (file.getName().toLowerCase().endsWith(".txt"))
			return new TxtBook(file);
		return null;
	}

}
