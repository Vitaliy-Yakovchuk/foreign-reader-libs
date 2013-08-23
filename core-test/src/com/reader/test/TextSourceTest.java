package com.reader.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.reader.common.ColorConstants;
import com.reader.common.TextProcessor;
import com.reader.common.TextSource;
import com.reader.common.ObjectsFactory;
import com.reader.common.TextWithProperties;
import com.reader.common.Word;

import static org.junit.Assert.*;

public class TextSourceTest {

	private TextSource source;

	private boolean[] end;

	private List<String> res;

	@BeforeClass
	public static void init() {
		try {
			ObjectsFactory.storageFile = File.createTempFile("sds", "ff");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Before
	public void initSimple() {

		ObjectsFactory.clear();

		String text = "Hello, this is simple text!";
		source = ObjectsFactory.createSimpleSource(text);

		end = new boolean[1];
		res = new ArrayList<String>();
		res.add("Hello");
		res.add("this");
		res.add("is");
		res.add("simple");
		res.add("text");
	}

	@Test
	public void testSimpleTextSource() {
		source.process(new TextProcessor() {

			@Override
			public void updated(TextWithProperties textProperties) {
				res.remove(0);
				assertEquals(2, textProperties.getWords().size());
				assertEquals("simple", textProperties.getWords().get(0));
				assertEquals("text", textProperties.getWords().get(1));
				textProperties.getColor().equals(ColorConstants.RED);
				assertTrue(end[0]);
			}

			@Override
			public void got(TextWithProperties textProperties) {
				assertEquals(res.remove(0), textProperties.getText());
				assertEquals(ColorConstants.DEFAULT, textProperties.getColor());
				assertFalse(end[0]);
			}

			@Override
			public void end() {
				assertTrue(res.isEmpty());
				end[0] = true;
			}
		});

		assertTrue(end[0]);
		assertTrue(res.isEmpty());

		TextWithProperties properties = new TextWithProperties();
		properties.setText("Simple text");

		properties.setColor(ColorConstants.RED);

		res.add("Simple text");

		source.update(properties);

		assertTrue(res.isEmpty());

		properties = new TextWithProperties();
		properties.setText("simple text");

		properties.setColor(ColorConstants.RED);

		res.add("simple");
		res.add("text");

		source.update(properties);

		assertFalse(res.isEmpty());

	}

	@Test
	public void testMarker() {

		source.markColor("this", ColorConstants.WHITE);
		source.markColor("is", ColorConstants.BLACK);
		source.markColor("simple", ColorConstants.YELLOW);
		source.markColor("text", ColorConstants.YELLOW);

		List<Word> words = source.getKnownWords(ColorConstants.YELLOW);
		assertEquals(2, words.size());

		source.markColor("simple text", ColorConstants.BLACK);

		words = source.getKnownWords(ColorConstants.WHITE);
		assertEquals(1, words.size());
		assertEquals("this", words.get(0).getText());
		assertEquals(ColorConstants.WHITE, words.get(0).getColor());

		words = source.getKnownWords(ColorConstants.BLACK);
		assertEquals(2, words.size());
		assertTrue("is".equals(words.get(0).getText())
				|| "is".equals(words.get(1).getText()));
		assertTrue("simple text".equals(words.get(0).getText())
				|| "simple text".equals(words.get(1).getText()));
		assertEquals(ColorConstants.BLACK, words.get(0).getColor());
		assertEquals(ColorConstants.BLACK, words.get(1).getColor());

		words = source.getKnownWords(ColorConstants.YELLOW);
		assertEquals(2, words.size());
		assertTrue("simple".equals(words.get(0).getText())
				|| "simple".equals(words.get(1).getText()));
		assertTrue("text".equals(words.get(0).getText())
				|| "text".equals(words.get(1).getText()));
		assertEquals(ColorConstants.YELLOW, words.get(0).getColor());
		assertEquals(ColorConstants.YELLOW, words.get(1).getColor());

		source.process(new TextProcessor() {

			@Override
			public void updated(TextWithProperties textProperties) {
				assertEquals("simple", textProperties.getText());
				assertTrue(end[0]);
			}

			@Override
			public void got(TextWithProperties textProperties) {
				assertEquals(res.remove(0), textProperties.getText());
				if ("this".equals(textProperties.getText()))
					assertEquals(ColorConstants.WHITE,
							textProperties.getColor());
				else if ("is".equals(textProperties.getText()))
					assertEquals(ColorConstants.BLACK,
							textProperties.getColor());
				else if ("simple".equals(textProperties.getText()))
					assertEquals(ColorConstants.BLACK,
							textProperties.getColor());
				else if ("text".equals(textProperties.getText()))
					assertEquals(ColorConstants.BLACK,
							textProperties.getColor());
				else
					assertEquals(ColorConstants.DEFAULT,
							textProperties.getColor());
				assertFalse(end[0]);
			}

			@Override
			public void end() {
				assertTrue(res.isEmpty());
				end[0] = true;
			}
		});

		assertTrue(end[0]);
		assertTrue(res.isEmpty());

	}

	@Test
	public void testMarker2() {

		source.markColor("this", ColorConstants.WHITE);
		source.markColor("is", ColorConstants.BLACK);
		source.markColor("simple", ColorConstants.YELLOW);
		source.markColor("text", ColorConstants.YELLOW);
		source.markColor("text simple", ColorConstants.BLACK);

		source.process(new TextProcessor() {

			@Override
			public void updated(TextWithProperties textProperties) {
				assertEquals("simple", textProperties.getText());
				assertTrue(end[0]);
			}

			@Override
			public void got(TextWithProperties textProperties) {
				assertEquals(res.remove(0), textProperties.getText());
				if ("this".equals(textProperties.getText()))
					assertEquals(ColorConstants.WHITE,
							textProperties.getColor());
				else if ("is".equals(textProperties.getText()))
					assertEquals(ColorConstants.BLACK,
							textProperties.getColor());
				else if ("simple".equals(textProperties.getText()))
					assertEquals(ColorConstants.YELLOW,
							textProperties.getColor());
				else if ("text".equals(textProperties.getText()))
					assertEquals(ColorConstants.YELLOW,
							textProperties.getColor());
				else
					assertEquals(ColorConstants.DEFAULT,
							textProperties.getColor());
				assertFalse(end[0]);
			}

			@Override
			public void end() {
				assertTrue(res.isEmpty());
				end[0] = true;
			}
		});

		assertTrue(end[0]);
		assertTrue(res.isEmpty());
	}

	@Test
	public void testMarker3() {

		source.markColor("this", ColorConstants.WHITE);
		source.markColor("hello", ColorConstants.BLACK);
		source.markColor("Hello this", ColorConstants.YELLOW);

		source.process(new TextProcessor() {

			@Override
			public void updated(TextWithProperties textProperties) {
				assertEquals("simple", textProperties.getText());
				assertTrue(end[0]);
			}

			@Override
			public void got(TextWithProperties textProperties) {
				assertEquals(res.remove(0), textProperties.getText());
				if ("Hello".equals(textProperties.getText()))
					assertEquals(ColorConstants.YELLOW,
							textProperties.getColor());
				else if ("this".equals(textProperties.getText()))
					assertEquals(ColorConstants.YELLOW,
							textProperties.getColor());
				else
					assertEquals(ColorConstants.DEFAULT,
							textProperties.getColor());
				assertFalse(end[0]);
			}

			@Override
			public void end() {
				assertTrue(res.isEmpty());
				end[0] = true;
			}
		});

		assertTrue(end[0]);
		assertTrue(res.isEmpty());
	}

	@Test
	public void testMarker4() {
		source.process(new TextProcessor() {

			@Override
			public void updated(TextWithProperties textProperties) {
			}

			@Override
			public void got(TextWithProperties textProperties) {
				if ("this".equals(textProperties.getText()))
					assertEquals(ColorConstants.BLUE, textProperties.getColor());
				assertFalse(end[0]);
			}

			@Override
			public void end() {
				end[0] = true;
			}
		});

		assertTrue(end[0]);

		end[0] = false;

		source.markWord("this");

		source.process(new TextProcessor() {

			@Override
			public void updated(TextWithProperties textProperties) {
			}

			@Override
			public void got(TextWithProperties textProperties) {
				if ("this".equals(textProperties.getText()))
					assertEquals(ColorConstants.YELLOW,
							textProperties.getColor());
				assertFalse(end[0]);
			}

			@Override
			public void end() {
				end[0] = true;
			}
		});

		source.markWord("this");

		end[0] = false;

		source.process(new TextProcessor() {

			@Override
			public void updated(TextWithProperties textProperties) {
			}

			@Override
			public void got(TextWithProperties textProperties) {
				if ("this".equals(textProperties.getText()))
					assertEquals(ColorConstants.WHITE,
							textProperties.getColor());
				assertFalse(end[0]);
			}

			@Override
			public void end() {
				end[0] = true;
			}
		});

		source.markWord("this");
		end[0] = false;

		source.process(new TextProcessor() {

			@Override
			public void updated(TextWithProperties textProperties) {
			}

			@Override
			public void got(TextWithProperties textProperties) {
				if ("this".equals(textProperties.getText()))
					assertEquals(ColorConstants.YELLOW,
							textProperties.getColor());
				assertFalse(end[0]);
			}

			@Override
			public void end() {
				end[0] = true;
			}
		});

	}

}
