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

import com.googlecode.toolkits.stardict.StarDict;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

public class StarDictTest extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7086350402809132325L;
	private JTextPane htmlView;
	private StarDict dict;

	public StarDictTest() {
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
		String path = "D:/PF/GoldenDict/content/dictd_www.mova.org_sokrat_ruen";

		dict = new StarDict(path);
	}

	protected void updateWord(String text) {
		String x = dict.getExplanation(text);

		StringBuffer sb = new StringBuffer();

		sb.append("<html><body>");
		sb.append(x);
		sb.append("</body></html>");
		htmlView.setText(sb.toString());
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new StarDictTest().setVisible(true);
			}
		});
	}

}
