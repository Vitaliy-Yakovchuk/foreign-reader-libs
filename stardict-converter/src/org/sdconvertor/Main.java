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