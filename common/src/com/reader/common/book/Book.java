package com.reader.common.book;

import java.util.List;

public interface Book extends SentenceParser {

	public List<SectionMetadata> getSections() throws Exception;

	public Section getSection(int i) throws Exception;

}
