/*******************************************************************************
 * Copyright 2013 Vitaliy Yakovchuk
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.reader;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.reader.common.ColorConstants;
import com.reader.common.Database;
import com.reader.common.ObjectsFactory;
import com.reader.common.book.Book;
import com.reader.common.book.Section;
import com.reader.common.book.SectionMetadata;
import com.reader.common.book.BookLoader;
import com.reader.common.book.Sentence;
import com.reader.common.book.SentenceParserCallback;

public class MainFrame extends JFrame {

	private final class SectionModel extends AbstractListModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8890469830818041022L;

		@Override
		public int getSize() {
			if (sections == null)
				return 0;
			else
				return sections.size();
		}

		@Override
		public Object getElementAt(int index) {
			if (sections == null) {
				return "Sections";
			}
			return sections.get(index);
		}

		public void update() {
			fireIntervalRemoved(sectionsList, 0, oldListSize);
			oldListSize = sections.size();
			fireIntervalAdded(sectionsList, 0, oldListSize);

		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -498112595603179774L;

	public static final String path = "..";

	private JTextPane htmlView;

	private JList files;

	private Book book;

	private List<SectionMetadata> sections;

	private SectionModel sectionsModel;

	private JList sectionsList;

	private int oldListSize = 0;

	public MainFrame() {
		JPanel base = new JPanel(new BorderLayout());
		setContentPane(base);
		base.add(createFiles(), BorderLayout.WEST);
		JPanel book = new JPanel(new BorderLayout());
		base.add(book, BorderLayout.CENTER);
		book.add(createSections(), BorderLayout.WEST);
		htmlView = new JTextPane();
		htmlView.setContentType("text/html;");
		book.add(new JScrollPane(htmlView), BorderLayout.CENTER);
		JButton b = new JButton("Scan");

		ObjectsFactory.storageFile = new File("/home/zdd/tmp/1.db");

		ObjectsFactory.createSimpleSource("").markColor("alternating", ColorConstants.YELLOW);
		ObjectsFactory.createSimpleSource("").markColor("found", ColorConstants.YELLOW);

		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Book book = BookLoader
							.loadBook(new File(
									"/home/zdd/tmp/Doyle_Arthur__The_Adventures_of_Sherlock_Holmes.fb2"));
					book.scanForSentences(new SentenceParserCallback() {

						Database database = ObjectsFactory.getDefaultDatabase();

						@Override
						public boolean found(Sentence sentence) {
							database.addSentence(sentence.text, "n",
									sentence.section, -1);
							return false;
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		base.add(b, BorderLayout.NORTH);
	}

	private Component createSections() {
		sectionsModel = new SectionModel();
		sectionsList = new JList(sectionsModel);
		sectionsList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				int i = sectionsList.getSelectedIndex();
				if (i >= 0) {
					try {
						Section s = book.getSection(i);
						showSection(s);
					} catch (Exception e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(MainFrame.this,
								e1.getLocalizedMessage());
					}
				}

			}
		});

		JScrollPane jScrollPane = new JScrollPane(sectionsList);
		jScrollPane.setMinimumSize(new Dimension(150, 150));
		return jScrollPane;
	}

	protected void showSection(Section s) {
		StringBuffer html = new StringBuffer();
		html.append("<html><body>");
		html.append("<h2>");
		if (s.getParagraphs().size() > 0)
			html.append(s.getParagraphs().get(0));

		html.append("</h2>");
		for (int i = 1; i < s.getParagraphs().size(); i++) {
			html.append("<p>");
			html.append(s.getParagraphs().get(i));
			html.append("</p>");
		}

		html.append("</body></html>");

		htmlView.setText(html.toString());
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				htmlView.scrollRectToVisible(new Rectangle());
			}
		});
	}

	private Component createFiles() {

		File file = new File(path);

		final List<File> files1 = new ArrayList<File>();

		for (File f : file.listFiles()) {
			if (f.isFile() && f.getName().toLowerCase().endsWith(".fb2"))
				files1.add(f);
			if (f.isFile() && f.getName().toLowerCase().endsWith(".txt"))
				files1.add(f);
		}

		files = new JList(new AbstractListModel() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 8890469830818041022L;

			@Override
			public int getSize() {
				return files1.size();
			}

			@Override
			public Object getElementAt(int index) {
				return files1.get(index).getName();
			}
		});

		files.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				int i = files.getSelectedIndex();
				if (i >= 0) {
					selectFile(files1.get(i));
				}
			}
		});
		return new JScrollPane(files);
	}

	protected void selectFile(File file) {
		try {
			book = BookLoader.loadBook(file);
			sections = book.getSections();
			sectionsModel.update();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
		}

	}

	public static void start(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception e) {
				}

				MainFrame frame = new MainFrame();
				frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

				frame.setVisible(true);
			}
		});
	}

}
