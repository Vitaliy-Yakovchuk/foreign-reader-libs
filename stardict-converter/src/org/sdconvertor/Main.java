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
package org.sdconvertor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentNavigableMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.sdconvertor.db.DictionaryManager;
import org.sdconvertor.loader.DictLoader;
import org.sdconvertor.loader.InfoLoader;
import org.sdconvertor.structure.Constant;
import org.sdconvertor.utility.Utility;

public class Main {

	public static void main(String[] args) throws IOException {
		HashMap<String, File> fileMap = Utility.prepareDictionaryFiles(args[0]);

		InfoLoader infoLoader = new InfoLoader(fileMap.get("ifo"),
				Constant.UTF8);

		infoLoader.loadData();

		File[] dictFiles = { fileMap.get("idx"), fileMap.get("dict") };
		final DB db = DBMaker.newFileDB(new File(args[1])).make();

		final ConcurrentNavigableMap<String, String> meenings = db
				.getTreeMap("word_meanings");

		DictionaryManager dictManager = new DictionaryManager() {
			public void addBatch(String word, String meening) {
				StringBuilder sb = new StringBuilder();
				boolean f = true;
				boolean t = false;
				for (int i = 0; i < meening.length(); i++) {
					if (meening.charAt(i) == '\n') {
						if (!t) {
							t = true;
							if (f) {
								sb.append(": ");
								f = false;
							} else
								sb.append("; ");
						}
					} else {
						sb.append(meening.charAt(i));
						t = false;
					}
				}
				meening = sb.toString().trim();
				if (meening.endsWith(";"))
					meening = meening.substring(0, meening.length() - 1);
				meenings.put(word, meening);
			};
		};
		DictLoader indexLoader = new DictLoader(dictManager, dictFiles,
				Constant.UTF8);
		indexLoader.loadData();
		db.commit();
		db.compact();
		db.close();
	}

}
