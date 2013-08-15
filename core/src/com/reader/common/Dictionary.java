package com.reader.common;

import com.reader.common.persist.WordAttributes;

public interface Dictionary {

	public void markColor(String[] worlds, String color);

	public void markColor(String world, String color);

	public WordAttributes get(String world);

	public WordAttributes[] get(String[] world);

}
