package com.reader.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.reader.common.ColorConstants;
import com.reader.common.Database;
import com.reader.common.ObjectsFactory;
import com.reader.common.persist.WordAttributes;

import static org.junit.Assert.*;

public class DatabaseSpeedTest {

	@Test
	public void testSpeed() {
		Database database = ObjectsFactory.getDefaultDatabase();
		List<String> randomWords = new ArrayList<String>();

		for (int i = 0; i < 100000; i++) {
			randomWords.add(String.valueOf(Math.random()));
		}

		String[] phrase1 = new String[3];
		String[] phrase2 = new String[3];
		String[] phrase11 = new String[4];
		String[] phrase12 = new String[6];

		int off = 2034;

		phrase1[0] = randomWords.get(off + 0);
		phrase1[1] = randomWords.get(off + 1);
		phrase1[2] = randomWords.get(off + 2);

		phrase2[0] = randomWords.get(off + 30);
		phrase2[1] = randomWords.get(off + 31);
		phrase2[2] = randomWords.get(off + 32);

		phrase11[0] = randomWords.get(off + 0);
		phrase11[1] = randomWords.get(off + 1);
		phrase11[2] = randomWords.get(off + 2);
		phrase11[3] = randomWords.get(off + 3);

		phrase12[0] = randomWords.get(off + 0);
		phrase12[1] = randomWords.get(off + 1);
		phrase12[2] = randomWords.get(off + 2);
		phrase12[3] = randomWords.get(off + 3);
		phrase12[4] = randomWords.get(off + 4);
		phrase12[5] = randomWords.get(off + 5);

		long ct = System.currentTimeMillis();

		WordAttributes wa = new WordAttributes();
		wa.setColor(ColorConstants.WHITE);

		for (String w : randomWords)
			database.put(w, wa);

		final String w = randomWords.get(off);

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

		assertTrue(System.currentTimeMillis() - ct < 3000);
	}
}
