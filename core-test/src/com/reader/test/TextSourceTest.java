package com.reader.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.reader.common.ColorConstants;
import com.reader.common.Dictionary;
import com.reader.common.TextProcessor;
import com.reader.common.TextSource;
import com.reader.common.TextSourceFactory;
import com.reader.common.TextWithProperties;

import static org.junit.Assert.*;

public class TextSourceTest {

	private TextSource source;

	private boolean[] end;

	private List<String> res;

	@Before
	public void initSimple() {
		String text = "Hello, this is simple text!";
		source = TextSourceFactory.createSimpleSource(text);

		end = new boolean[1];
		res = new ArrayList<String>();
		res.add("hello");
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
				assertEquals("simple", textProperties.getText());
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
		properties.setText("simple");

		source.update(properties);
	}

	@Test
	public void testMarker() {

		res = new ArrayList<String>();
		res.add("hello");
		res.add("this");
		res.add("is");
		res.add("simple text");

		Dictionary dictionary = source.getDictionary();

		dictionary.markColor("this", ColorConstants.WHITE);
		dictionary.markColor("is", ColorConstants.BLACK);
		dictionary.markColor("simple text", ColorConstants.BLACK);

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
				else if ("simple text".equals(textProperties.getText()))
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

}
