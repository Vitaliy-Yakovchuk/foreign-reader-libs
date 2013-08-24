package com.reader.mapdb;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.reader.common.book.Sentence;
import com.reader.common.book.SentenceParserCallback;

public class WordDBImpl {

	private final File dic;

	public WordDBImpl(File dic) {
		this.dic = dic;
		dic.mkdirs();
	}

	public boolean addSentence(String word, final Sentence sentence)
			throws IOException {
		final boolean[] added = new boolean[] { true };
		File file = getWordFile(word);
		if (file.exists()) {
			scan(file, new SentenceParserCallback() {

				@Override
				public boolean found(Sentence sentence1) {
					if (sentence.equals(sentence1)) {
						added[0] = false;
						return false;
					}
					return true;
				}
			});
		}

		if (added[0]) {
			FileOutputStream fs;
			if (file.exists())
				fs = new FileOutputStream(file, true);
			else
				fs = new FileOutputStream(file);
			DataOutputStream dos = new DataOutputStream(fs);
			dos.writeUTF(sentence.text);
			dos.writeUTF(sentence.bookId);
			dos.writeInt(sentence.section);
			dos.writeInt(-1);// reserved for position
			dos.close();
		}

		return added[0];
	}

	public List<Sentence> getSentences(String word) throws IOException {
		final List<Sentence> res = new ArrayList<Sentence>();
		File wf = getWordFile(word);
		if (wf != null && wf.exists()) {
			scan(wf, new SentenceParserCallback() {

				int c = 0;

				@Override
				public boolean found(Sentence sentence) {
					if (c > 100)
						return false;
					res.add(sentence);
					c++;
					return true;
				}
			});
		}

		return res;
	}

	private void scan(File wordFile,
			SentenceParserCallback sentenceParserCallback) throws IOException {
		DataInputStream dis = new DataInputStream(new FileInputStream(wordFile));

		while (dis.available() > 0) {
			Sentence sentence = new Sentence();
			sentence.text = dis.readUTF();
			sentence.bookId = dis.readUTF();
			sentence.section = dis.readInt();
			dis.readInt();// read position
			if (!sentenceParserCallback.found(sentence))
				break;
		}
		dis.close();
	}

	public File getWordFile(String word) {
		String w = word.toLowerCase().replace('\'', '_');
		File f = new File(dic, w.substring(0, 1));
		f.mkdir();
		return new File(f, w);
	}

	public File getDic() {
		return dic;
	}

}
