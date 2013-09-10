package com.reader.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.reader.common.ObjectsFactory;
import com.reader.common.book.Sentence;
import com.reader.mapdb.MapDBDatabase;
import com.reader.mapdb.WordDBImpl;

public class WordDBImplTest {

	private WordDBImpl wordDB;

	@Before
	public void initDB() throws IOException {
		File file = File.createTempFile("words", "db");
		file.delete();
		try {
			ObjectsFactory.storageFile = File.createTempFile("ssf", "ff");
		} catch (IOException e) {
			e.printStackTrace();
		}
		wordDB = new WordDBImpl(file, new MapDBDatabase(
				ObjectsFactory.storageFile));
	}

	@Test
	public void testAdding() throws IOException {
		Sentence s1 = new Sentence();
		Sentence s2 = new Sentence();
		Sentence s3 = new Sentence();
		Sentence s4 = new Sentence();

		s1.section = 2;

		s1.text = "t1";
		s2.text = "t2";
		s3.text = "t3";
		s4.text = "t4";

		assertTrue(wordDB.addSentence("w1", s1));
		assertTrue(wordDB.addSentence("w1", s2));
		assertTrue(wordDB.addSentence("w1", s3));
		assertFalse(wordDB.addSentence("w1", s1));
		assertTrue(wordDB.addSentence("w1", s4));

		assertTrue(wordDB.addSentence("w2", s2));
		assertTrue(wordDB.addSentence("w2", s1));
		assertTrue(wordDB.addSentence("w2", s3));
		assertFalse(wordDB.addSentence("w2", s1));
		assertTrue(wordDB.addSentence("w2", s4));

		List<Sentence> w1 = wordDB.getSentences("w1");
		List<Sentence> w2 = wordDB.getSentences("w2");

		assertEquals(4, w1.size());
		;
		assertEquals(4, w2.size());
		;

		assertEquals(s1, w1.get(0));
		;
		assertEquals(s2, w1.get(1));
		;
		assertEquals(s3, w1.get(2));
		;
		assertEquals(s4, w1.get(3));
		;

		assertEquals(s2, w2.get(0));
		;
		assertEquals(s1, w2.get(1));
		;
		assertEquals(s3, w2.get(2));
		;
		assertEquals(s4, w2.get(3));
		;
	}

	@After
	public void deleteFiles() {
		delete(wordDB.getDic());
	}

	private void delete(File dic) {
		if (dic.getName().equals(".") || dic.getName().equals(".."))
			return;
		if (dic.isDirectory()) {
			for (File f : dic.listFiles())
				delete(f);
		}
		dic.delete();
	}

}
