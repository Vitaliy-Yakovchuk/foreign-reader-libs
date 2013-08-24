package com.reader.common.book.fb2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.EndTag;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.StartTag;
import net.htmlparser.jericho.StreamedSource;
import net.htmlparser.jericho.Tag;

import com.reader.common.book.AbstractBook;
import com.reader.common.book.Section;
import com.reader.common.book.SectionMetadata;
import com.reader.common.book.Sentence;
import com.reader.common.book.SentenceParserCallback;
import com.reader.common.impl.SentenceParserImpl;

public class FictionBook extends AbstractBook {

	private File file;

	public FictionBook(File file) {
		this.file = file;
	}

	@Override
	public List<SectionMetadata> getSections() throws Exception {
		List<SectionMetadata> res = new ArrayList<SectionMetadata>();
		StreamedSource source = new StreamedSource(new FileInputStream(file));
		try {
			readSections(res, source);
		} catch (Exception exception) {
			throw exception;
		} finally {
			source.close();
		}
		return res;
	}

	private void readSections(List<SectionMetadata> res, StreamedSource source) {
		Iterator<Segment> i = source.iterator();
		while (i.hasNext()) {
			Segment segment = i.next();
			if (segment instanceof StartTag) {
				Tag tag = (Tag) segment;
				if (tag.getName().equals("body")) {
					while (i.hasNext()) {
						segment = i.next();
						if (segment instanceof StartTag) {
							tag = (Tag) segment;
							if (tag.getName().equals("section")) {
								loadSection(res, i, 0);
							}
						}
					}
					break;
				}
			}
		}
	}

	private void loadSection(List<SectionMetadata> res, Iterator<Segment> i,
			int level) {
		Segment segment;
		Tag tag;
		SectionMetadata metadata = new SectionMetadata();
		metadata.setIndex(res.size());
		metadata.setLevel(level);
		metadata.setTitle("");
		res.add(metadata);
		while (i.hasNext()) {
			segment = i.next();
			if (segment instanceof StartTag) {
				tag = (Tag) segment;
				if (tag.getName().equals("title")) {
					StringBuilder title = new StringBuilder();
					while (i.hasNext()) {
						segment = i.next();
						if (segment instanceof Tag) {
							if (((Tag) segment).getName().equals("title"))
								break;
						} else if (segment instanceof CharacterReference) {
							CharacterReference characterReference = (CharacterReference) segment;
							try {
								characterReference.appendCharTo(title);
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							title.append(segment);
						}
					}
					metadata.setTitle(title.toString());
				} else if (tag.getName().equals("section")) {
					loadSection(res, i, level + 1);
				}
			} else if (segment instanceof EndTag) {
				if (((Tag) segment).getName().equals("section"))
					break;
			}
		}
	}

	@Override
	public Section getSection(int i) throws Exception {
		Section section = new Section();
		StreamedSource source = new StreamedSource(new FileInputStream(file));
		try {
			readSection(section, i, source);
		} catch (Exception exception) {
			throw exception;
		} finally {
			source.close();
		}
		return section;
	}

	private void readSection(Section section, int sectionIndex,
			StreamedSource source) {
		int index = 0;
		Iterator<Segment> i = source.iterator();
		while (i.hasNext()) {
			Segment segment = i.next();
			if (segment instanceof StartTag) {
				Tag tag = (Tag) segment;
				if (tag.getName().equals("body")) {
					while (i.hasNext()) {
						segment = i.next();
						if (segment instanceof StartTag) {
							tag = (Tag) segment;
							if (tag.getName().equals("section")) {
								if (index == sectionIndex) {
									while (i.hasNext()) {
										segment = i.next();
										if (segment instanceof StartTag) {
											tag = (Tag) segment;
											if (tag.getName().equals("title")) {
												StringBuilder title = new StringBuilder();
												while (i.hasNext()) {
													segment = i.next();
													if (segment instanceof Tag) {
														if (((Tag) segment)
																.getName()
																.equals("title"))
															break;
													} else if (segment instanceof CharacterReference) {
														CharacterReference characterReference = (CharacterReference) segment;
														try {
															characterReference
																	.appendCharTo(title);
														} catch (IOException e) {
															e.printStackTrace();
														}
													} else {
														title.append(segment);
													}
												}
												String t = title.toString();
												section.setTitle(t);
												section.getParagraphs().add(t);
											} else if (tag.getName()
													.equals("p")) {

												StringBuilder p = new StringBuilder();
												while (i.hasNext()) {
													segment = i.next();
													if (segment instanceof Tag) {
														if (((Tag) segment)
																.getName()
																.equals("p"))
															break;
													} else if (segment instanceof CharacterReference) {
														CharacterReference characterReference = (CharacterReference) segment;
														try {
															characterReference
																	.appendCharTo(p);
														} catch (IOException e) {
															e.printStackTrace();
														}
													} else {
														p.append(segment);
													}
												}
												section.getParagraphs().add(
														p.toString());

											} else if (tag.getName().equals(
													"section"))
												break;
										} else if (segment instanceof EndTag) {
											if (((Tag) segment).getName()
													.equals("section"))
												break;
										}
									}
								}
								index++;
							}
						}
					}
					break;
				}
			}
		}

	}

	@Override
	public void scanForSentences(final SentenceParserCallback parser)
			throws Exception {
		StreamedSource source = new StreamedSource(new FileInputStream(file));
		SentenceParserImpl parserImpl = new SentenceParserImpl() {

			@Override
			public boolean found(Sentence sentence) {
				return parser.found(sentence);
			}
		};

		try {
			readForSentences(source, parserImpl);
		} catch (Exception exception) {
			throw exception;
		} finally {
			source.close();
		}
	}

	private void readForSentences(StreamedSource source,
			SentenceParserImpl parserImpl) {
		int index = 0;
		Iterator<Segment> i = source.iterator();
		while (i.hasNext()) {
			Segment segment = i.next();
			if (segment instanceof StartTag) {
				Tag tag = (Tag) segment;
				if (tag.getName().equals("body")) {
					while (i.hasNext()) {
						segment = i.next();
						if (segment instanceof StartTag) {
							tag = (Tag) segment;
							if (tag.getName().equals("section")) {
								while (i.hasNext()) {
									segment = i.next();
									if (segment instanceof StartTag) {
										tag = (Tag) segment;
										if (tag.getName().equals("title")) {
											StringBuilder title = new StringBuilder();
											while (i.hasNext()) {
												segment = i.next();
												if (segment instanceof Tag) {
													if (((Tag) segment)
															.getName().equals(
																	"title"))
														break;
												} else if (segment instanceof CharacterReference) {
													CharacterReference characterReference = (CharacterReference) segment;
													try {
														characterReference
																.appendCharTo(title);
													} catch (IOException e) {
														e.printStackTrace();
													}
												} else {
													title.append(segment);
												}
											}											
										} else if (tag.getName().equals("p")) {

											StringBuilder p = new StringBuilder();
											while (i.hasNext()) {
												segment = i.next();
												if (segment instanceof Tag) {
													if (((Tag) segment)
															.getName().equals(
																	"p"))
														break;
												} else if (segment instanceof CharacterReference) {
													CharacterReference characterReference = (CharacterReference) segment;
													try {
														characterReference
																.appendCharTo(p);
													} catch (IOException e) {
														e.printStackTrace();
													}
												} else {
													p.append(segment);
												}
											}
											parserImpl.addSenteceText(p.toString());											
										} else if (tag.getName().equals(
												"section"))
											break;
									} else if (segment instanceof EndTag) {
										if (((Tag) segment).getName().equals(
												"section"))
											break;
									}
								}
								index++;
								parserImpl.setSection(index);
							}
						}
					}
					break;
				}
			}
		}
	}
}
