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
