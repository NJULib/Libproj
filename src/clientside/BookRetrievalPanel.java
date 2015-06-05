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

		// 连接服务器
		ServerInfo serverInfo = new ServerInfo();
		libClient = new LibClient(serverInfo.getHost(), serverInfo
				.getPort());

		selectionLabel = new JLabel("检索方式"); // 标签
		fieldComboBox = new JComboBox(); // 分类检索下拉列表
		fieldComboBox.addItem("请选择...");
		fieldComboBox.addItem("书名");
		fieldComboBox.addItem("ISBN号");
		fieldComboBox.addItem("作者");
		fieldComboBox.addItem("出版");
		fieldComboBox.addItemListener(new FieldSelectedListener());

		keywordText = new JTextField("java", 20); // 关键字
		keywordText.addMouseListener(new KeywordClickedListener());
		keywordText.addKeyListener(new KeywodKeyListener());

		retrievalButton = new JButton("检索");
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

		detailsButton = new JButton("详细...");
		detailsButton.addActionListener(new DetailsActionListener());
		detailsButton.setEnabled(false);
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout());
		bottomPanel.add(detailsButton);
		JButton close = new JButton("关闭窗口");
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
				//没有检索到书的时候，应清空图书列表区
				Object[] noData = new Object[0];
				bookListBox.setListData(noData);
				fieldComboBox.setSelectedIndex(0);
				detailsButton.setEnabled(false);
				JOptionPane.showMessageDialog(null, "对不起，没有找到您要的图书！");
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "网络故障: " + e, "网络问题",
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
		bookHashTable.put("书名", "name");
		bookHashTable.put("作者", "authors");
		bookHashTable.put("出版", "publisher");
		bookHashTable.put("ISBN号", "isbn");

		if (fieldSelected == null || fieldSelected.startsWith("请选择")) {
			JOptionPane.showMessageDialog(null, "请选择检索方式");
			return;
		}

		String keyword = keywordText.getText();
		if (keyword == null || keyword.equals("")) {
			JOptionPane.showMessageDialog(null, "检索关键字不能为空");
			return;
		}

		String field = bookHashTable.get(fieldSelected);
		ProcessBookList(field, keyword);
	}

	protected void log(Object msg) {
		System.out.println(CurrDateTime.currDateTime() + "BookRetrievalPanel类: " + msg);
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
