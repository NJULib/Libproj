/**
 * 
 */
package clientside;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import serverside.entity.BookDetails;
import util.CurrDateTime;

import java.util.Hashtable;

/**
 * @author zhu
 * 
 */
public class BookRetrievalPanel extends JPanel {

	private static final long serialVersionUID = 7613975895176217206L;

	protected JLabel selectionLabel;
	protected JComboBox fieldComboBox;
	protected JPanel topPanel;
	protected JList bookListBox;
	protected JScrollPane bookScrollPane;
	protected JButton retrievalButton;
	protected JTextField keywordText;
	protected JButton detailsButton;
	protected JPanel bottomPanel;
	protected MainFrame parentFrame;
	protected ArrayList bookArrayList;
	protected LibClient libClient;
	protected String fieldSelected;

	public BookRetrievalPanel(MainFrame theParentFrame) {
		parentFrame = theParentFrame;
		this.setLayout(new BorderLayout());

		// ���ӷ�����
		ServerInfo serverInfo = new ServerInfo();
		libClient = new LibClient(serverInfo.getHost(), serverInfo
				.getPort());

		selectionLabel = new JLabel("������ʽ"); // ��ǩ
		fieldComboBox = new JComboBox(); // ������������б�
		fieldComboBox.addItem("��ѡ��...");
		fieldComboBox.addItem("����");
		fieldComboBox.addItem("ISBN��");
		fieldComboBox.addItem("����");
		fieldComboBox.addItem("����");
		fieldComboBox.addItemListener(new FieldSelectedListener());

		keywordText = new JTextField("java", 20); // �ؼ���
		keywordText.addMouseListener(new KeywordClickedListener());
		keywordText.addKeyListener(new KeywodKeyListener());

		retrievalButton = new JButton("����");
		retrievalButton.addActionListener(new RetrievalActionListener());

		topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		keywordText.setSize(topPanel.getWidth() / 2, topPanel.getWidth());

		topPanel.add(selectionLabel);
		topPanel.add(fieldComboBox);
		topPanel.add(keywordText);
		topPanel.add(retrievalButton);
		this.add(BorderLayout.NORTH, topPanel);

		bookListBox = new JList();
		bookListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		bookListBox.addListSelectionListener(new BookSelectionListener());
		bookListBox.addMouseListener(new BookListMouseClickListener());
		bookScrollPane = new JScrollPane(bookListBox);
		this.add(BorderLayout.CENTER, bookScrollPane);

		detailsButton = new JButton("��ϸ...");
		detailsButton.addActionListener(new DetailsActionListener());
		detailsButton.setEnabled(false);
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout());
		bottomPanel.add(detailsButton);
		JButton close = new JButton("�رմ���");
		close.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				parentFrame.tabbedPane.remove(parentFrame.bookQueryPanel);
			}
			
		});
		bottomPanel.add(close);
		this.add(BorderLayout.SOUTH, bottomPanel);
	}

	protected void ProcessBookList(String theField, String theKeyword) {
		try {
			bookArrayList = libClient.getBookList(theField, theKeyword);
			if (bookArrayList.size() > 0) {
				Object[] theData = bookArrayList.toArray();
				bookListBox.setListData(theData);
			} else {
				//û�м��������ʱ��Ӧ���ͼ���б���
				Object[] noData = new Object[0];
				bookListBox.setListData(noData);
				fieldComboBox.setSelectedIndex(0);
				detailsButton.setEnabled(false);
				JOptionPane.showMessageDialog(null, "�Բ���û���ҵ���Ҫ��ͼ�飡");
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "�������: " + e, "��������",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private void DisplayBookDetailsDialog() {
		int index = bookListBox.getSelectedIndex();
		BookDetails book = (BookDetails) bookArrayList.get(index);
		BookDetailsDialog bookDetailsDialog = new BookDetailsDialog(
				parentFrame, book, libClient);
		bookDetailsDialog.setVisible(true);
	}

	private void RetrievalResults() {
		Hashtable<String, String> bookHashTable = new Hashtable<String, String>();
		bookHashTable.put("����", "name");
		bookHashTable.put("����", "authors");
		bookHashTable.put("����", "publisher");
		bookHashTable.put("ISBN��", "isbn");

		if (fieldSelected == null || fieldSelected.startsWith("��ѡ��")) {
			JOptionPane.showMessageDialog(null, "��ѡ�������ʽ");
			return;
		}

		String keyword = keywordText.getText();
		if (keyword == null || keyword.equals("")) {
			JOptionPane.showMessageDialog(null, "�����ؼ��ֲ���Ϊ��");
			return;
		}

		String field = bookHashTable.get(fieldSelected);
		ProcessBookList(field, keyword);
	}

	protected void log(Object msg) {
		System.out.println(CurrDateTime.currDateTime() + "BookRetrievalPanel��: " + msg);
	}

	class FieldSelectedListener implements ItemListener {

		public void itemStateChanged(ItemEvent event) {

			if (event.getStateChange() == ItemEvent.SELECTED) {
				fieldSelected = (String) fieldComboBox.getSelectedItem();
			}
		}
	}

	class KeywordClickedListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				keywordText.setSelectionStart(0);
				keywordText.setSelectionEnd(keywordText.getText().length());
			}
		}
	}

	class KeywodKeyListener extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				RetrievalResults();
			}
		}
	}

	class RetrievalActionListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			RetrievalResults();
		}
	}

	class DetailsActionListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {

			DisplayBookDetailsDialog();
		}
	}

	class BookSelectionListener implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent event) {

			if (bookListBox.isSelectionEmpty()) {
				detailsButton.setEnabled(false);
			} else {
				detailsButton.setEnabled(true);
			}
		}
	}

	class BookListMouseClickListener extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			if(!detailsButton.isEnabled()){
				return;
			}
			if (e.getClickCount() == 2) {
				DisplayBookDetailsDialog();
			}
		}

	}
}
