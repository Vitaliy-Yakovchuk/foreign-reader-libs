package com.reader.test;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.reader.common.ColorConstants;
import com.reader.common.Database;
import com.reader.common.ObjectsFactory;
import com.reader.common.persist.WordAttributes;

import static org.junit.Assert.*;

public class DatabaseSpeedTest {

	@BeforeClass
	public static void init() {
		try {
			ObjectsFactory.storageFile = File.createTempFile("ssf", "ff");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@After
	public void clear() {

	}

	@Test
	public void simpleTest() {
		Database database = ObjectsFactory.getDefaultDatabase();
		WordAttributes wa1 = new WordAttributes();
		wa1.setColor("color1");
		wa1.setStartsWithPhrase(true);
		WordAttributes wa2 = new WordAttributes();
		wa2.setColor("color2");
		wa2.setTransport(true);

		String word1 = "w1";
		String word2 = "w2";

		database.put(word1, wa1);
		database.put(word2, wa2);

		assertEquals(wa2, database.get(word2));
		assertEquals(wa1, database.get(word1));
	}

	@Test
	public void testSpeed() {
		Database database = ObjectsFactory.getDefaultDatabase();
		String[] randomWords = new String[100000];

		for (int i = 0; i < 100000; i++) {
			randomWords[i] = String.valueOf(Math.random());
		}

		String[] phrase1 = new String[3];
		String[] phrase2 = new String[3];
		String[] phrase11 = new String[4];
		String[] phrase12 = new String[6];

		int off = 2034;

		phrase1[0] = randomWords[off + 0];
		phrase1[1] = randomWords[off + 1];
		phrase1[2] = randomWords[off + 2];

		phrase2[0] = randomWords[off + 30];
		phrase2[1] = randomWords[off + 31];
		phrase2[2] = randomWords[off + 32];

		phrase11[0] = randomWords[off + 0];
		phrase11[1] = randomWords[off + 1];
		phrase11[2] = randomWords[off + 2];
		phrase11[3] = randomWords[off + 3];

		phrase12[0] = randomWords[off + 0];
		phrase12[1] = randomWords[off + 1];
		phrase12[2] = randomWords[off + 2];
		phrase12[3] = randomWords[off + 3];
		phrase12[4] = randomWords[off + 4];
		phrase12[5] = randomWords[off + 5];

		long ct = System.currentTimeMillis();

		WordAttributes wa = new WordAttributes();
		wa.setColor(ColorConstants.WHITE);

		database.updateWords(randomWords, wa);

		final String w = randomWords[off];

		wa = database.get(w);
		wa.setColor(ColorConstants.BLACK);
		database.put(w, wa);

		for (String word : randomWords) {
			wa = database.get(word);
			if (word.equals(w))
				assertEquals(wa.getColor(), ColorConstants.BLACK);
			else
				assertEquals(wa.getColor(), ColorConstants.WHITE);
		}

		String yellow = ColorConstants.YELLOW;

		WordAttributes wordAttributes = new WordAttributes();
		wordAttributes.setColor(yellow);

		database.put(phrase1, wordAttributes);
		database.put(phrase2, wordAttributes);

		wordAttributes.setColor(ColorConstants.BLUE);
		database.put(phrase12, wordAttributes);

		wa = database.get(w);

		assertEquals(wa.getColor(), ColorConstants.BLACK);

		assertTrue(wa.isStartsWithPhrase());
		assertFalse(wa.isTransport());

		wa = database.get(new String[] { phrase1[0], phrase1[1] });

		assertTrue(wa.isStartsWithPhrase());
		assertTrue(wa.isTransport());

		wa = database.get(new String[] { phrase1[0], phrase1[1], phrase1[2] });

		assertEquals(ColorConstants.YELLOW, wa.getColor());

		assertTrue(wa.isStartsWithPhrase());
		assertFalse(wa.isTransport());

		wa = database.get(new String[] { phrase2[0], phrase2[1] });

		assertTrue(wa.isStartsWithPhrase());
		assertTrue(wa.isTransport());

		wa = database.get(new String[] { phrase2[0], phrase2[1], phrase2[2] });

		assertEquals(ColorConstants.YELLOW, wa.getColor());

		assertFalse(wa.isStartsWithPhrase());
		assertFalse(wa.isTransport());

		wa = database.get(phrase11);

		assertTrue(wa.isStartsWithPhrase());
		assertTrue(wa.isTransport());

		wa = database.get(phrase12);

		assertEquals(wa.getColor(), ColorConstants.BLUE);

		assertFalse(wa.isStartsWithPhrase());
		assertFalse(wa.isTransport());

		assertTrue(System.currentTimeMillis() - ct < 5000);
	}
}
