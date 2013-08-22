package com.reader;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

public class WordNetView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7086350402809132325L;
	private JTextPane htmlView;

	public WordNetView() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setExtendedState(MAXIMIZED_BOTH);
		final JTextField field = new JTextField();
		field.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				ch();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				ch();
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				ch();
			}

			private void ch() {
				updateWord(field.getText());
			}
		});
		getContentPane().add(field, BorderLayout.NORTH);
		htmlView = new JTextPane();
		htmlView.setContentType("text/html;");
		getContentPane().add(new JScrollPane(htmlView), BorderLayout.CENTER);
	}

	protected void updateWord(String text) {
		String path = "D:/Ramus Work/workspace/dict";
		// URL url = new URL(" file ", null, path);

		// construct the dictionary object and open it
		IDictionary dict = new Dictionary(new File(path));
		try {
			dict.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (text.length() == 0)
			return;
		IIndexWord idxWord = dict.getIndexWord(text, POS.NOUN);
		showWord(idxWord, dict);
	}

	private void showWord(IIndexWord idxWord, IDictionary dict) {
		StringBuffer sb = new StringBuffer();

		sb.append("<html><body>");

		if (idxWord != null) {
			List<IWordID> wordIDs = idxWord.getWordIDs();
			for (IWordID wordID : wordIDs) {
				IWord word = dict.getWord(wordID);
				sb.append("<h4>");
				sb.append(word.getLemma());
				sb.append("</h4>");
				sb.append("<p>");
				sb.append(word.getSynset().getGloss());
				sb.append("</p>");
				List<IWord> words = word.getSynset().getWords();
				if (words != null && words.size() > 0) {
					sb.append("S: ");
					for (IWord word2 : words) {
						sb.append(word2.getLemma());
						sb.append(", ");
					}
				}
			}
			htmlView.setText(sb.toString());
		}

		sb.append("</body></html>");
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new WordNetView().setVisible(true);
			}
		});
	}

}
