package com.reader.common;

import com.reader.common.persist.WordAttributes;

/**
 * Inner very fast key-point database
 * 
 * @author vitaliy.yakovchuk
 * 
 */

public interface Database {

	public static final char SPACE = ' ';
	
	public void put(String word, WordAttributes wordAttributes);

	public WordAttributes get(String word);

	public void remove(String word);

	public void put(String[] frase, WordAttributes wordAttributes);

	public WordAttributes get(String[] frase);

	public void remove(String[] frase);

}
